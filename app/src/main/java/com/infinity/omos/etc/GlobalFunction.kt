package com.infinity.omos.etc

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.infinity.omos.R
import com.infinity.omos.data.Artists
import com.infinity.omos.data.Record
import com.infinity.omos.databinding.ListAlineCategoryItemBinding
import com.infinity.omos.databinding.ListDetailCategoryItemBinding

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

        fun setCategoryText(context: Context, category: String): String{
            var ctg = ""
            when(category){
                "A_LINE" -> {
                    ctg = context.resources.getString(R.string.a_line)
                }

                "STORY" -> {
                    ctg = context.resources.getString(R.string.story)
                }

                "OST" -> {
                    ctg = context.resources.getString(R.string.ost)
                }

                "LYRICS" -> {
                    ctg = context.resources.getString(R.string.lyrics)
                }

                "FREE" -> {
                    ctg = context.resources.getString(R.string.free)
                }
            }

            return ctg
        }

        fun setDate(createdDate: String): String{
            var listDate = createdDate.split("T")
            var date = listDate[0].replace("-", " ")
            return date
        }
    }
}