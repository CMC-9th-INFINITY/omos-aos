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
import com.infinity.omos.databinding.ActivitySelectCategoryBinding
import com.infinity.omos.databinding.ActivityWriteRecordBinding
import com.infinity.omos.viewmodels.SelectCategoryViewModel
import com.infinity.omos.viewmodels.WriteRecordViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register_nick.*
import kotlinx.android.synthetic.main.activity_register_nick.toolbar
import kotlinx.android.synthetic.main.activity_write_record.*

class WriteRecordActivity : AppCompatActivity() {

    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var cropResult: ActivityResultLauncher<Intent>
    private val viewModel: WriteRecordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityWriteRecordBinding>(this, R.layout.activity_write_record)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.category.value = intent.getStringExtra("category")
        if (viewModel.category.value == resources.getString(R.string.a_line)){
            record_contents.visibility = View.GONE
            oneline_contents.visibility = View.VISIBLE
        }

        initToolBar()

        // 공개/비공개 설정
        viewModel.isPrivate.observe(this, Observer { state ->
            state?.let {
                if (it){
                    btn_private.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_private))
                } else{
                    btn_private.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_public))
                }
            }
        })

        btn_sticker.setOnClickListener {
            Toast.makeText(this, "스티커", Toast.LENGTH_SHORT).show()
        }

        // 이미지 넣기
        btn_gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
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
    }

    private fun cropImage(uri: Uri?){
        var width = img_record_title.measuredWidth
        var height = img_record_title.measuredHeight
        val intent = CropImage
            .activity(uri)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setMinCropResultSize(width, height)
            .setMaxCropResultSize(width, height)
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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_next -> {
                Toast.makeText(this, "다음", Toast.LENGTH_SHORT).show()
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