package com.infinity.omos.etc

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.infinity.omos.R
import com.infinity.omos.data.Artists

class GlobalFunction {
    companion object{

        fun changeList(text: String): List<String> {
            return text.split("\n")
        }

        fun categoryEngToKr(context: Context, category: String): String{
            var ctg = "ERROR"
            when(category){
                "A_LINE" -> {
                    ctg = context.resources.getString(R.string.a_line)
                }
                "OST" -> {
                    ctg = context.resources.getString(R.string.ost)
                }
                "STORY" -> {
                    ctg = context.resources.getString(R.string.story)
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

        fun categoryKrToEng(context: Context, category: String): String{
            var ctg = "ERROR"
            when(category){
                context.resources.getString(R.string.a_line) -> {
                    ctg = "A_LINE"
                }
                context.resources.getString(R.string.ost) -> {
                    ctg = "OST"
                }
                context.resources.getString(R.string.story) -> {
                    ctg = "STORY"
                }
                context.resources.getString(R.string.lyrics) -> {
                    ctg = "LYRICS"
                }
                context.resources.getString(R.string.free) -> {
                    ctg = "FREE"
                }
            }

            return ctg
        }

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

        fun setDate(createdDate: String): String {
            var listDate = createdDate.split("T")
            return listDate[0].replace("-", " ")
        }
    }
}