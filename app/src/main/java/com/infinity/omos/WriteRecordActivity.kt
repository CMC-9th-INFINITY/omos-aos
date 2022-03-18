package com.infinity.omos

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.infinity.omos.data.SaveRecord
import com.infinity.omos.data.Update
import com.infinity.omos.databinding.ActivitySelectCategoryBinding
import com.infinity.omos.databinding.ActivityWriteRecordBinding
import com.infinity.omos.etc.GlobalFunction
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.SelectCategoryViewModel
import com.infinity.omos.viewmodels.WriteRecordViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register_nick.*
import kotlinx.android.synthetic.main.activity_register_nick.toolbar
import kotlinx.android.synthetic.main.activity_write_record.*
import java.text.SimpleDateFormat
import java.util.*

class WriteRecordActivity : AppCompatActivity() {

    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var cropResult: ActivityResultLauncher<Intent>
    private val viewModel: WriteRecordViewModel by viewModels()
    private lateinit var binding: ActivityWriteRecordBinding

    private var category = ""
    private var isPublic = true
    private var musicId = ""
    private var recordContents = ""
    private var recordImageUrl = ""
    private var recordTitle = ""
    private var postId = -1

    private var isModify = false

    private var userId = GlobalApplication.prefs.getInt("userId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_write_record)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        category = intent.getStringExtra("category")!!
        musicId = intent.getStringExtra("musicId")!!
        viewModel.musicTitle.value = intent.getStringExtra("musicTitle")
        viewModel.artists.value = intent.getStringExtra("artists")
        viewModel.albumImageUrl.value = intent.getStringExtra("albumImageUrl")
        viewModel.category.value = category

        var currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy MM dd", Locale.getDefault())
        viewModel.createdDate.value = dateFormat.format(currentTime)

        if (viewModel.category.value == resources.getString(R.string.a_line)){
            record_contents.visibility = View.GONE
            aline_contents.visibility = View.VISIBLE
            binding.tvLimitContents.text = "/50"
        }

        // 수정 데이터
        isModify = intent.getBooleanExtra("modify", false)
        if (isModify){
            viewModel.isPublic.value = intent.getBooleanExtra("isPublic", false)
            recordTitle = intent.getStringExtra("recordTitle")!!
            recordContents = intent.getStringExtra("recordContents")!!
            binding.etRecordTitle.setText(recordTitle)
            binding.tvTitleCount.text = binding.etRecordTitle.length().toString()
            binding.tvContentsCount.text = recordContents.length.toString()
            postId = intent.getIntExtra("postId", -1)

            if (viewModel.category.value == resources.getString(R.string.a_line)){
                binding.alineContents.setText(recordContents)
            } else{
                binding.recordContents.setText(recordContents)
            }
        }

        initToolBar()

        // 공개/비공개 설정
        viewModel.isPublic.observe(this, Observer { state ->
            state?.let {
                isPublic = if (it){
                    binding.btnPrivate.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_public))
                    true
                } else{
                    binding.btnPrivate.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_private))
                    false
                }
            }
        })

        btn_sticker.setOnClickListener {
            Toast.makeText(this, "스티커", Toast.LENGTH_SHORT).show()
        }

        // 이미지 넣기
        btn_gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            result.launch(intent)
        }

        // 콜백
        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            when(it.resultCode){
                RESULT_OK -> {
                    cropImage(it.data?.data)
                }

                RESULT_CANCELED ->{
                    Toast.makeText(this, "취소", Toast.LENGTH_LONG).show()
                }

                else -> {
                    Log.d("WriteRecord","error")
                }
            }
        }

        cropResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            when(it.resultCode){
                RESULT_OK -> {
                    CropImage.getActivityResult(it.data)?.let { cropResult ->
                        if(Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                cropResult.uri
                            )
                            val bd = BitmapDrawable(resources, bitmap)
                            img_record_title.setImageDrawable(bd)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, cropResult.uri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            val bd = BitmapDrawable(resources, bitmap)
                            img_record_title.setImageDrawable(bd)
                        }
                    }
                }

                RESULT_CANCELED ->{
                    Toast.makeText(this, "취소", Toast.LENGTH_LONG).show()
                }

                else -> {
                    Log.d("WriteRecord","error")
                }
            }
        }

        viewModel.getStateSaveRecord().observe(this) { record ->
            record?.let {
                if (it.state) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "저장 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getStateUpdateRecord().observe(this) { state ->
            state?.let {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }

        binding.etRecordTitle.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tvTitleCount.text = binding.etRecordTitle.length().toString()
            }
        })

        binding.recordContents.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tvContentsCount.text = binding.recordContents.length().toString()
            }
        })

        binding.alineContents.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tvContentsCount.text = binding.alineContents.length().toString()
            }
        })
    }

    private fun cropImage(uri: Uri?){
        var width = binding.imgRecordTitle.measuredWidth
        var height = binding.imgRecordTitle.measuredHeight
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
                recordContents = if (category == resources.getString(R.string.a_line)){
                    // 한 줄 감상 내용인지 구분
                    binding.alineContents.text.toString()
                } else {
                    binding.recordContents.text.toString()
                }

                if (recordContents == "" || recordTitle == ""){
                    Toast.makeText(this, "제목 또는 내용을 기입하세요.", Toast.LENGTH_SHORT).show()
                } else{
                    category = GlobalFunction.categoryKrToEng(this, category)
                    if (!isModify){
                        viewModel.saveRecord(SaveRecord(category, isPublic, musicId, recordContents, recordImageUrl, recordTitle, userId))

                        val intent = Intent("RECORD_UPDATE")
                        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent)
                    } else{
                        viewModel.updateRecord(postId, Update(recordContents, isPublic, recordImageUrl, recordTitle))

                        val intent = Intent("RECORD_UPDATE")
                        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent)
                    }

                    Toast.makeText(this, "완료", Toast.LENGTH_SHORT).show()
                }
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}