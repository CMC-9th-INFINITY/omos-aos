package com.infinity.omos

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.infinity.omos.databinding.ActivitySelectCategoryBinding
import com.infinity.omos.databinding.ActivityWriteRecordBinding
import com.infinity.omos.viewmodels.SelectCategoryViewModel
import com.infinity.omos.viewmodels.WriteRecordViewModel
import kotlinx.android.synthetic.main.activity_register_nick.*
import kotlinx.android.synthetic.main.activity_register_nick.toolbar
import kotlinx.android.synthetic.main.activity_write_record.*

class WriteRecordActivity : AppCompatActivity() {

    private lateinit var result: ActivityResultLauncher<Intent>
    private val viewModel: WriteRecordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityWriteRecordBinding>(this, R.layout.activity_write_record)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        initToolBar()

        btn_gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            result.launch(intent)
        }

        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                var currentImageUri = it.data?.data
                try {
                    currentImageUri?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                currentImageUri
                            )
                            val bd = BitmapDrawable(resources, bitmap)
                            img_record_title.setImageDrawable(bd)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, currentImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            val bd = BitmapDrawable(resources, bitmap)
                            img_record_title.setImageDrawable(bd)
                        }
                    }
                }catch(e:Exception) {
                    e.printStackTrace()
                }
            } else if(it.resultCode == RESULT_CANCELED){
                Toast.makeText(this, "취소", Toast.LENGTH_LONG).show()
            } else{
                Log.d("WriteRecord","error")
            }
        }
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