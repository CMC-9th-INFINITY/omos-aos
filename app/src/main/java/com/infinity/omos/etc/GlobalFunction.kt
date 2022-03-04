package com.infinity.omos.etc

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.infinity.omos.data.Artists

class GlobalFunction {
    companion object{
        fun changeTextColor(context: Context, tv: TextView, start: Int, end: Int, color: Int){
            var ssb = SpannableStringBuilder(tv.text)
            ssb.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, color)), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
            tv.text = ssb
        }

        fun setArtist(artists: List<Artists>): String{
            var str = ""
            for (s in artists){
                str += s.artistName + " & "
            }

            if (str.length >= 3){
                str = str.substring(0, str.length - 3)
            } else{
                str = "-"
            }

            return str
        }
    }
}