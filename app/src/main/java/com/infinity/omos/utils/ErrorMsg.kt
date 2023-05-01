package com.infinity.omos.utils

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.infinity.omos.R

class ErrorMsg {

    companion object{

        fun showErrorMsg(context: Context, et: EditText, tvMsg: TextView, msg: String, shakeLayout: LinearLayout){
            et.background = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.rectangle_stroke_box, null)
            tvMsg.visibility = View.VISIBLE
            tvMsg.text = msg

            // 흔들림 효과
            YoYo.with(Techniques.Shake)
                .duration(100)
                .repeat(1)
                .playOn(shakeLayout)
        }

        fun hideErrorMsg(context: Context, et: EditText, tvMsg: TextView){
            et.background = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.rectangle_box, null)
            tvMsg.visibility = View.INVISIBLE
        }
    }
}