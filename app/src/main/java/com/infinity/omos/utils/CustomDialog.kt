package com.infinity.omos.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.method.ScrollingMovementMethod
import android.view.Window
import android.view.WindowManager
import com.infinity.omos.R
import kotlinx.android.synthetic.main.dialog_popup.*
import kotlinx.android.synthetic.main.dialog_pp.*
import kotlinx.android.synthetic.main.dialog_tos.*
import kotlinx.android.synthetic.main.dialog_tos.btn_exit

class CustomDialog(private val context : Context) {

    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var listener: DialogReportOkClickedListener

    fun show(title: String, yesText: String) {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_popup)
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        dlg.title.text = title
        dlg.tv_yes.text = yesText

        var result = ""
        dlg.btn_ok.setOnClickListener {
            result = "yes"
            listener.onOkClicked(result)
            dlg.dismiss()
        }


        dlg.btn_cancel.setOnClickListener {
            result = "no"
            listener.onOkClicked(result)
            dlg.dismiss()
        }
        dlg.show()
    }

    fun setOnOkClickedListener(listener: (String) -> Unit) {
        this.listener = object : DialogReportOkClickedListener {
            override fun onOkClicked(content: String) {
                listener(content)
            }
        }
    }

    interface DialogReportOkClickedListener {
        fun onOkClicked(context: String)
    }

    fun showImageDialog(){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_image)
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.setCancelable(true) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        var result = ""
        dlg.btn_ok.setOnClickListener {
            result = "yes"
            listener.onOkClicked(result)
            dlg.dismiss()
        }


        dlg.btn_cancel.setOnClickListener {
            result = "no"
            listener.onOkClicked(result)
            dlg.dismiss()
        }
        dlg.show()
    }

    fun showTosDialog(){
        dlg.setContentView(R.layout.dialog_tos)
        dlg.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dlg.setCanceledOnTouchOutside(true)
        dlg.setCancelable(true)
        dlg.show()

        dlg.tv_tos.movementMethod = ScrollingMovementMethod()
        dlg.btn_exit.setOnClickListener {
            dlg.dismiss()
        }
    }

    fun showPPDialog(){
        dlg.setContentView(R.layout.dialog_pp)
        dlg.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dlg.setCanceledOnTouchOutside(true)
        dlg.setCancelable(true)
        dlg.show()

        dlg.tv_pp.movementMethod = ScrollingMovementMethod()
        dlg.btn_exit.setOnClickListener {
            dlg.dismiss()
        }
    }

    fun showProgress(){
        dlg.setContentView(R.layout.dialog_progress)
        dlg.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dlg.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.setCanceledOnTouchOutside(false)
        dlg.setCancelable(false)
        dlg.show()
    }

    fun dismissProgress(){
        dlg.dismiss()
    }
}