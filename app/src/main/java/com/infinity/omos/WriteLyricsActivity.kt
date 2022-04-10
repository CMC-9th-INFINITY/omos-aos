package com.infinity.omos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.infinity.omos.adapters.LyricsListAdapter
import com.infinity.omos.data.SaveRecord
import com.infinity.omos.data.Update
import com.infinity.omos.databinding.ActivityWriteLyricsBinding
import com.infinity.omos.etc.GlobalFunction
import com.infinity.omos.etc.GlobalFunction.Companion.changeList
import com.infinity.omos.utils.AWSConnector
import com.infinity.omos.utils.CustomDialog
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.WriteLyricsViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_write_record.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class WriteLyricsActivity : AppCompatActivity() {

    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var cropResult: ActivityResultLauncher<Intent>
    private val viewModel: WriteLyricsViewModel by viewModels()
    private lateinit var binding: ActivityWriteLyricsBinding

    private var category = ""
    private var isPublic = true
    private var musicId = ""
    private var recordContents = ""
    private var recordImageUrl = ""
    private var tempImageUrl = ""
    private var recordTitle = ""
    private var postId = -1

    private var isModify = false

    private lateinit var mAdapter: LyricsListAdapter
    private var userId = GlobalApplication.prefs.getInt("userId")
    private var imageFile: File? = null
    lateinit var awsConnector: AWSConnector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_write_lyrics)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        awsConnector = AWSConnector(this)

        category = intent.getStringExtra("category")!!
        musicId = intent.getStringExtra("musicId")!!
        viewModel.musicTitle.value = intent.getStringExtra("musicTitle")
        viewModel.artists.value = intent.getStringExtra("artists")
        viewModel.albumImageUrl.value = intent.getStringExtra("albumImageUrl")
        viewModel.category.value = category

        // 수정 데이터
        isModify = intent.getBooleanExtra("modify", false)
        if (isModify){
            viewModel.isPublic.value = intent.getBooleanExtra("isPublic", false)
            recordTitle = intent.getStringExtra("recordTitle")!!
            binding.etRecordTitle.setText(recordTitle)
            binding.tvTitleCount.text = binding.etRecordTitle.length().toString()
            postId = intent.getIntExtra("postId", -1)

            // 글 대표 이미지
            recordImageUrl = intent.getStringExtra("recordImageUrl")!!
            tempImageUrl = recordImageUrl
            Glide.with(binding.imgRecordTitle.context)
                .load(recordImageUrl)
                .into(binding.imgRecordTitle)
        }

        var lyrics = intent.getStringExtra("lyrics")!!
        mAdapter = LyricsListAdapter(this)
        mAdapter.setLyrics(changeList(lyrics), true)
        binding.rvLyrics.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        var interpret = intent.getStringExtra("interpret")
        if (interpret != null){
            mAdapter.setInterpret(changeList(interpret))
            val text = interpret.replace("\n", "")
            binding.tvContentsCount.text = text.length.toString()
        }

        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy MM dd", Locale.getDefault())
        viewModel.createdDate.value = dateFormat.format(currentTime)

        initToolBar()

        viewModel.isPublic.observe(this) { state ->
            state?.let {
                state?.let {
                    isPublic = if (it){
                        binding.btnPrivate.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_public))
                        true
                    } else{
                        binding.btnPrivate.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_private))
                        false
                    }
                }
            }
        }

        btn_sticker.setOnClickListener {
            Toast.makeText(this, "스티커", Toast.LENGTH_SHORT).show()
        }

        // 이미지 넣기
        binding.btnGallery.setOnClickListener {
            if (tempImageUrl == ""){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                result.launch(intent)
            } else{
                val dlg = CustomDialog(this)
                dlg.showImageDialog()
                dlg.setOnOkClickedListener {
                    when(it){
                        "yes" -> {
                            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            result.launch(intent)
                        }

                        "no" -> {
                            tempImageUrl = ""
                            Glide.with(binding.imgRecordTitle.context)
                                .load(tempImageUrl)
                                .into(binding.imgRecordTitle)
                        }
                    }
                }
            }

        }

        // 콜백
        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            when(it.resultCode){
                RESULT_OK -> {
                    cropImage(it.data?.data)
                }

                RESULT_CANCELED ->{

                }

                else -> {
                    Log.d("WriteLyrics","error")
                }
            }
        }

        cropResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            when(it.resultCode){
                RESULT_OK -> {
                    CropImage.getActivityResult(it.data)?.let { cropResult ->
                        val imageUri = cropResult.uri
                        Glide.with(binding.imgRecordTitle.context)
                            .load(imageUri)
                            .into(binding.imgRecordTitle)
                        imageFile = imageUri.toFile()
                        tempImageUrl = "exist"
                    }
                }

                RESULT_CANCELED ->{

                }

                else -> {
                    Log.d("WriteRecord","error")
                }
            }
        }

        viewModel.getStateSaveRecord().observe(this) { record ->
            record?.let {
                if (it.state) {
                    val intent1 = Intent("RECORD_UPDATE")
                    intent1.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                    sendBroadcast(intent1)

                    val intent2 = Intent("PROFILE_UPDATE")
                    intent2.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                    sendBroadcast(intent2)

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("save", true)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "저장 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getStateUpdateRecord().observe(this) { state ->
            state?.let {
                val intent = Intent("RECORD_UPDATE")
                intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                sendBroadcast(intent)
                finish()
            }
        }

        binding.etRecordTitle.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tvTitleCount.text = binding.etRecordTitle.length().toString()
            }
        })

        mAdapter.setChangeTextLengthListener(object: LyricsListAdapter.ChangeTextLengthListener{
            override fun changeLength(size: Int) {
                binding.tvContentsCount.text = size.toString()
            }
        })
    }

    private fun cropImage(uri: Uri?){
        var width = img_record_title.measuredWidth
        var height = img_record_title.measuredHeight
        val intent = CropImage
            .activity(uri)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setAspectRatio(width, height)
            .getIntent(this)

        cropResult.launch(intent)
    }

    private fun initToolBar(){
        toolbar.title = ""
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_action_next, menu)
        var actionNext = menu.findItem(R.id.action_next)
        actionNext.title = "완료"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_next -> {
                recordTitle = binding.etRecordTitle.text.toString()
                recordContents = mAdapter.getContents()
                if (recordContents == "" || recordTitle == ""){
                    Toast.makeText(this, "제목 또는 내용을 기입하세요.", Toast.LENGTH_SHORT).show()
                } else{
                    category = GlobalFunction.categoryKrToEng(this, category)

                    if (!isModify){

                        val currentTime = System.currentTimeMillis()
                        if (imageFile != null){
                            // 이미지 파일 s3 업로드
                            awsConnector.uploadFile("record/$userId$currentTime.png", imageFile!!)
                            viewModel.saveRecord(SaveRecord(category, isPublic, musicId, recordContents, "${BuildConfig.S3_BASE_URL}record/$userId$currentTime.png", recordTitle, userId))
                        } else{
                            // 레코드 이미지 없을 때,
                            viewModel.saveRecord(SaveRecord(category, isPublic, musicId, recordContents, "", recordTitle, userId))
                        }
                    } else{
                        // 레코드 수정 상태

                        if (imageFile != null){
                            // 이미지 파일 s3 업로드
                            if (recordImageUrl == ""){
                                val currentTime = System.currentTimeMillis()
                                awsConnector.uploadFile("record/$userId$currentTime.png", imageFile!!)
                                recordImageUrl = "${BuildConfig.S3_BASE_URL}record/$userId$currentTime.png"
                            } else{
                                val s3Url = recordImageUrl.replace(BuildConfig.S3_BASE_URL, "")
                                awsConnector.uploadFile(s3Url, imageFile!!)
                            }
                        }

                        // 이미지 삭제 시
                        if (tempImageUrl == "" && recordImageUrl != ""){
                            val s3Url = recordImageUrl.replace(BuildConfig.S3_BASE_URL, "").split("/")
                            viewModel.deleteS3Image(s3Url[0], s3Url[1])
                            viewModel.updateRecord(postId, Update(recordContents, isPublic, "", recordTitle))
                        } else {
                            viewModel.updateRecord(postId, Update(recordContents, isPublic, recordImageUrl, recordTitle))
                        }
                    }

                    Toast.makeText(this, "완료", Toast.LENGTH_SHORT).show()
                }
                true
            }
            android.R.id.home -> {
                showWarning()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        showWarning()
    }

    private fun showWarning(){
        recordTitle = binding.etRecordTitle.text.toString()
        recordContents = mAdapter.getContents()
        if (recordContents != "" || recordTitle != ""){
            val dlg = CustomDialog(this)
            dlg.show("작성 중인 내용이 삭제됩니다.\n그래도 그만하시겠습니까?", "확인")
            dlg.setOnOkClickedListener {
                when(it){
                    "yes" -> {
                        finish()
                    }
                }
            }
        } else{
            finish()
        }
    }
}