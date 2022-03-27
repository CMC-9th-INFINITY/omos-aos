package com.infinity.omos.ui.setting

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.databinding.DataBindingUtil
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.bumptech.glide.Glide
import com.infinity.omos.BuildConfig
import com.infinity.omos.R
import com.infinity.omos.data.Profile
import com.infinity.omos.databinding.ActivityChangeProfileBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.ui.bottomnav.MyPageFragment
import com.infinity.omos.ui.onboarding.LoginActivity
import com.infinity.omos.utils.AWSConnector
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.ChangeProfileViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.toolbar
import kotlinx.android.synthetic.main.activity_register_nick.*
import kotlinx.android.synthetic.main.activity_write_record.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class ChangeProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeProfileBinding
    private val viewModel: ChangeProfileViewModel by viewModels()
    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var cropResult: ActivityResultLauncher<Intent>

    private val userId = GlobalApplication.prefs.getInt("userId")
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_profile)
        binding.lifecycleOwner = this
        Log.d("jaemin", MyPageFragment.myProfileUrl)
        binding.data = Profile(MyPageFragment.myNickname, MyPageFragment.myProfileUrl, userId)

        initToolBar()

        binding.btnProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            result.launch(intent)
        }

        // 콜백
        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_OK -> {
                    cropImage(it.data?.data)
                }

                RESULT_CANCELED -> {

                }

                else -> {
                    Log.d("WriteRecord", "error")
                }
            }
        }

        cropResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_OK -> {
                    CropImage.getActivityResult(it.data)?.let { cropResult ->
                        val imageUri = cropResult.uri
                        Glide.with(binding.imgProfile.context)
                            .load(imageUri)
                            .error(R.drawable.ic_profile)
                            .fallback(R.drawable.ic_profile)
                            .into(binding.imgProfile)
                        imageFile = imageUri.toFile()
                    }
                }

                RESULT_CANCELED -> {

                }

                else -> {
                    Log.d("WriteRecord", "error")
                }
            }
        }

        val awsConnector = AWSConnector(this)
        binding.btnComplete.setOnClickListener {
            if (imageFile != null){
                // 이미지 파일 s3 업로드
                awsConnector.uploadFile("profile/$userId.png", imageFile!!)
            }

            viewModel.updateProfile(binding.etNick.text.toString(), "${BuildConfig.S3_BASE_URL}profile/${userId}.png", userId)
        }

        viewModel.getStateUpdateProfile().observe(this) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.DONE -> {
                        MyPageFragment.myNickname = binding.etNick.text.toString()
                        MyPageFragment.myProfileUrl = "${BuildConfig.S3_BASE_URL}profile/${userId}.png"
                        val intent = Intent("PROFILE_UPDATE")
                        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent)
                        finish()

                        Toast.makeText(this, "완료", Toast.LENGTH_SHORT).show()
                    }
                    Constant.ApiState.ERROR -> {
                        // 이미 있는 닉네임일 때,
                        LoginActivity.showErrorMsg(
                            this,
                            binding.etNick,
                            binding.tvErrorNick,
                            resources.getString(R.string.exist_nick),
                            binding.linearNick
                        )
                    }
                    else -> {
                        Toast.makeText(this, "에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 특수문자 띄어쓰기 입력 제한
        binding.etNick.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isLetterOrDigit(source[i])) {
                    Toast.makeText(this, "특수문자 및 공백은 입력이 제한됩니다.", Toast.LENGTH_SHORT).show()
                    return@InputFilter ""
                }
            }
            null
        })

        // 완료버튼 활성화
        binding.etNick.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etNick.length() > 0) {
                    binding.btnComplete.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@ChangeProfileActivity,
                            R.color.orange
                        )
                    )
                    binding.btnComplete.setTextColor(
                        ContextCompat.getColor(
                            this@ChangeProfileActivity,
                            R.color.white
                        )
                    )
                    binding.btnComplete.isEnabled = true
                } else {
                    binding.btnComplete.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@ChangeProfileActivity,
                            R.color.light_gray
                        )
                    )
                    binding.btnComplete.setTextColor(
                        ContextCompat.getColor(
                            this@ChangeProfileActivity,
                            R.color.dark_gray
                        )
                    )
                    binding.btnComplete.isEnabled = false
                }
            }
        })
    }

    private fun cropImage(uri: Uri?) {
        var width = binding.btnProfile.measuredWidth
        var height = binding.btnProfile.measuredHeight
        val intent = CropImage
            .activity(uri)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setAspectRatio(width, height)
            .getIntent(this)

        cropResult.launch(intent)
    }

    private fun initToolBar() {
        toolbar.title = "프로필 변경"
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}