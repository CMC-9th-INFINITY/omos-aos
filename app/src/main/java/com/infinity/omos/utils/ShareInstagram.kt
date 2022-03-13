package com.infinity.omos.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.infinity.omos.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ShareInstagram(private val context: Context) {

    fun shareInsta(imageView: ImageView, textView: TextView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val bgBitmap = drawBackgroundBitmap()
            val bgUri = saveImageAtCacheDir(bgBitmap)

            val viewBitmap = drawViewBitmap(imageView, textView)
            val viewUri = saveImageAtCacheDir(viewBitmap)

            instaShare(bgUri, viewUri)
        } else {
            val bgBitmap = drawBackgroundBitmap()
            val bgUri = saveImageAtCacheDir(bgBitmap)

            val viewBitmap = drawViewBitmap(imageView, textView)
            val viewUri = saveImageAtCacheDir(viewBitmap)

            instaShare(bgUri, viewUri)
        }
    }

    // 화면에 나타난 View를 Bitmap에 그릴 용도.
    private fun drawBackgroundBitmap(): Bitmap {
        //기기 해상도를 가져옴.
        val backgroundWidth = context.resources.displayMetrics.widthPixels
        val backgroundHeight = context.resources.displayMetrics.heightPixels

        val backgroundBitmap = Bitmap.createBitmap(backgroundWidth, backgroundHeight, Bitmap.Config.ARGB_8888) // 비트맵 생성
        val canvas = Canvas(backgroundBitmap) // 캔버스에 비트맵을 Mapping.

        // TODO : 배경색 설정하기
        // val bgColor = viewModel?.background?.value // 뷰모델의 현재 설정된 배경색을 가져온다.
        val bgColor = R.color.deep_dark
        if(bgColor != null) {
            val color = ContextCompat.getColor(context, bgColor)
            canvas.drawColor(color) // 캔버스에 현재 설정된 배경화면색으로 칠한다.
        }

        return backgroundBitmap
    }

    // 이미지를 캐시에 저장하는 메서드. Android 버전과 상관 없다.
    private fun saveImageAtCacheDir(bitmap: Bitmap): Uri? {
        val fileName = System.currentTimeMillis().toString() + ".png"
        val cachePath = "${context.cacheDir}/file"
        val dir = File(cachePath)

        if(dir.exists().not()) {
            dir.mkdirs() // 폴더 없을경우 폴더 생성
        }

        val fileItem = File("$dir/$fileName")
        try {
            fileItem.createNewFile()
            //0KB 파일 생성.

            val fos = FileOutputStream(fileItem) // 파일 아웃풋 스트림

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            //파일 아웃풋 스트림 객체를 통해서 Bitmap 압축.

            fos.close() // 파일 아웃풋 스트림 객체 close

            MediaScannerConnection.scanFile(context, arrayOf(fileItem.toString()), null, null)

            //sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileItem)))
            // 브로드캐스트 수신자에게 파일 미디어 스캔 액션 요청. 그리고 데이터로 추가된 파일에 Uri를 넘겨준다. - Deprecated 위 코드로 수정
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return FileProvider.getUriForFile(context, "com.infinity.omos.fileprovider", fileItem)
    }

    private fun drawViewBitmap(imageView: ImageView, textView: TextView): Bitmap {
        val margin = context.resources.displayMetrics.density * 20

        val width = if (imageView.width > textView.width) {
            imageView.width
        } else {
            textView.width
        }

        val height = (imageView.height + textView.height + margin).toInt()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val imageViewBitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)
        val imageViewCanvas = Canvas(imageViewBitmap)
        imageView.draw(imageViewCanvas)
        /*imageViewCanvas를 통해서 imageView를 그린다.
         *이 때 스케치북은 imageViewBitmap이므로 imageViewBitmap에 imageView가 그려진다.
         */

        val imageViewLeft = ((width - imageView.width) / 2).toFloat()

        canvas.drawBitmap(imageViewBitmap, imageViewLeft, (0).toFloat(), null)

        //아래는 TextView. 위에 ImageView와 같은 로직으로 비트맵으로 만든 후 캔버스에 그려준다.
        if(textView.length() > 0) {
            //textView가 공백이 아닐때만
            val textViewBitmap = Bitmap.createBitmap(textView.width, textView.height, Bitmap.Config.ARGB_8888)
            val textViewCanvas = Canvas(textViewBitmap)
            textView.draw(textViewCanvas)

            val textViewLeft = ((width - textView.width) / 2).toFloat()
            val textViewTop = imageView.height + margin

            canvas.drawBitmap(textViewBitmap, textViewLeft, textViewTop, null)
        }

        return bitmap
    }

    private fun instaShare(bgUri: Uri?, viewUri: Uri?) {
// Define image asset URI
        val stickerAssetUri = Uri.parse(viewUri.toString())
        val sourceApplication = "com.infinity.omos"

// Instantiate implicit intent with ADD_TO_STORY action,
// sticker asset, and background colors
        val intent = Intent("com.instagram.share.ADD_TO_STORY")
        intent.putExtra("source_application", sourceApplication)

        intent.type = "image/png"
        intent.setDataAndType(bgUri, "image/png");
        intent.putExtra("interactive_asset_uri", stickerAssetUri)

// Instantiate activity and verify it will resolve implicit intent
        context.grantUriPermission(
            "com.instagram.android", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        context.grantUriPermission(
            "com.instagram.android", bgUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        try {
            context.startActivity(intent)
        } catch (e : ActivityNotFoundException) {
            Toast.makeText(context, "인스타그램 앱이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
//        try{
//            //저장해놓고 삭제한다.
//            Thread.sleep(1000)
//            viewUri?.let { uri -> contentResolver.delete(uri, null, null) }
//            bgUri?.let { uri -> contentResolver.delete(uri, null, null) }
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
    }

}