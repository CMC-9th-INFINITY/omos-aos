package com.infinity.omos.ui.view

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import com.infinity.omos.R
import com.infinity.omos.databinding.ViewOmosDialogBinding
import com.infinity.omos.utils.Width.Companion.screenWidth

class OmosDialog(private val activity: Activity) {

    private val dlg = Dialog(activity)
    private lateinit var binding: ViewOmosDialogBinding

    fun showDialog(
        title: String,
        okText: String,
        cancelText: String = activity.getString(R.string.cancel),
        cancelVisible: Boolean = true,
        gravity: Int = Gravity.CENTER,
        sizeRatio: Double = 0.8,
        onOkClickListener: (() -> Unit)?
    ) {
        binding = ViewOmosDialogBinding.inflate(activity.layoutInflater)
        initDialog(sizeRatio)
        initProperty(title, okText, cancelText, cancelVisible, gravity)
        initListener(onOkClickListener)

        dlg.show()
    }

    private fun initDialog(sizeRatio: Double) = with(dlg) {
        setContentView(binding.root)

        // 다이얼로그 가로 크기 조절
        val params = window?.attributes
        params?.width = (context.screenWidth() * sizeRatio).toInt()
        window?.attributes = params

        setCancelable(false) // 다이얼로그 외부 영역 터치 불가
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initProperty(
        title: String,
        okText: String,
        cancelText: String,
        visible: Boolean,
        gravity: Int
    ) = with(binding) {
        tvTitle.text = title
        tvOk.text = okText
        tvCancel.text = cancelText
        cancelVisible = visible
        tvTitle.gravity = gravity
    }

    private fun initListener(onOkClickListener: (() -> Unit)?) = with(binding) {
        tvOk.setOnClickListener {
            onOkClickListener?.invoke()
            dlg.dismiss()
        }

        tvCancel.setOnClickListener {
            dlg.dismiss()
        }
    }
}