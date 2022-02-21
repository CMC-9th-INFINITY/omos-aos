package com.infinity.omos.etc

import android.app.Dialog
import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.WindowManager
import com.infinity.omos.R
import kotlinx.android.synthetic.main.dialog_pp.*
import kotlinx.android.synthetic.main.dialog_tos.*
import kotlinx.android.synthetic.main.dialog_tos.btn_exit

class CustomDialog(context: Context){
    private val dialog = Dialog(context)

    fun showTosDialog(){
        dialog.setContentView(R.layout.dialog_tos)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        dialog.tv_tos.movementMethod = ScrollingMovementMethod()
        dialog.btn_exit.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun showPPDialog(){
        dialog.setContentView(R.layout.dialog_pp)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        dialog.tv_pp.movementMethod = ScrollingMovementMethod()
        dialog.btn_exit.setOnClickListener {
            dialog.dismiss()
        }
    }
}