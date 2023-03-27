package com.infinity.omos.ui.view

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.infinity.omos.databinding.ViewOmosDialogBinding
import com.infinity.omos.utils.Width.Companion.screenWidth

class OmosDialog(private val activity: Activity) {

    private val dlg = Dialog(activity)
    private lateinit var binding: ViewOmosDialogBinding

    fun showDialog(
        title: String,
        okText: String,
        cancelText: String = "취소",
        cancelVisible: Boolean = true,
        onOkClickListener: (() -> Unit)?
    ) {
        binding = ViewOmosDialogBinding.inflate(activity.layoutInflater)
        initDialog()
        initProperty(title, okText, cancelText, cancelVisible)
        initListener(onOkClickListener)

        dlg.show()
    }

    private fun initDialog() = with(dlg) {
        setContentView(binding.root)

        // 다이얼로그 가로 크기 조절
        val params = window?.attributes
        params?.width = (context.screenWidth() * 0.8).toInt()
        window?.attributes = params

        setCancelable(false) // 다이얼로그 외부 영역 터치 불가
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initProperty(
        title: String,
        okText: String,
        cancelText: String,
        visible: Boolean
    ) = with(binding) {
        tvTitle.text = title
        tvOk.text = okText
        tvCancel.text = cancelText
        cancelVisible = visible
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