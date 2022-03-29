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
        private val CHO = arrayOf("ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ")
        val JOONG = arrayOf("ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ")
        val JONG = arrayOf("", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ")

        fun extractLetter(text: String): String{
            var str = ""
            for(i in text.indices){
                var uniVal = text[i]
                // 한글일 경우만 시작해야 하기 때문에 0xAC00부터 아래의 로직을 실행한다
                // http://www.unicode.org/charts/PDF/UAC00.pdf
                if (uniVal >= 0xAC00.toChar() && uniVal <= 0xD7A3.toChar()) {
                    uniVal = (uniVal - 0xAC00)
                    var cho =(uniVal.code / 28 / 21)
                    var joong =(uniVal.code / 28 % 21)
                    var jong =(uniVal.code % 28) // 종성의 첫번째는 채움이기 때문에
                    str += CHO[cho] + JOONG[joong] + JONG[jong]
                } else {
                    str += uniVal
                }
            }

            return str
        }

        fun changeList(text: String): List<String> {
            val tmp = text.split("\n")
            val resultList = ArrayList<String>()

            for (i in tmp){
                if (i != ""){
                    resultList.add(i)
                }
            }
            return resultList
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