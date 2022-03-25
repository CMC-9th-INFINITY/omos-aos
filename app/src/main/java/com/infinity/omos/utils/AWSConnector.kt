package com.infinity.omos.utils

import android.app.Activity
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import java.io.File

class AWSConnector constructor(val activity: Activity) {
    private class Listener() : TransferListener {
        override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
            val percentDoneFloat = bytesCurrent.toFloat() / bytesTotal.toFloat() * 100
            val percentDone = percentDoneFloat.toInt()
            Log.d("AWS", "percent: $percentDone")
        }
        override fun onStateChanged(id: Int, state: TransferState?) {
            if (TransferState.COMPLETED == state) {
            }
        }
        override fun onError(id: Int, ex: java.lang.Exception?) {

        }
    }
    /**
     * 주어진 키(S3의 상대경로) 에 위치한 파일을 다운로드 합니다.
     * [File] 파라미터는 유효해야 하며, 폴더는 지원하지 않습니다.
     * 해당 파일이 이미 있을 경우에는 덮어쓰기 됩니다.
     *
     * @param key S3의 상대경로
     * @param file 다운로드 될 경로
     * @param callback mode: [AWSConnector.Mode] 참고
     * int: Done Progress Percent
     * Exception: not-null if mode = ERROR
     */
    fun downloadFile(key: String, file: File) {
        initializeAWSMobileClient {
            val observer = it.download(key, file)
            observer.setTransferListener(Listener())
        }
    }
    /**
     * 주어진 키(S3의 상대경로) 에 주어진 파일을 업로드 합니다.
     * [File] 파라미터는 유효해야 하며, 폴더는 지원하지 않습니다.
     * 해당 파일이 이미 있을 경우에는 덮어쓰기 됩니다.
     *
     * @param key S3의 상대경로
     * @param file 업로드 할 경로
     * @param callback mode: [AWSConnector.Mode] 참고
     * int: Done Progress Percent
     * Exception: not-null if mode = ERROR
     */
    fun uploadFile(key: String, file: File) {
        initializeAWSMobileClient {
            val observer = it.upload(key, file)
            observer.setTransferListener(Listener())
        }
    }
    private fun initializeAWSMobileClient(job: (TransferUtility) -> Unit) {
        AWSMobileClient.getInstance().initialize(activity) {
            val transferUtility = TransferUtility.builder()
                .context(activity)
                .awsConfiguration(AWSMobileClient.getInstance().configuration)
                .s3Client(AmazonS3Client(AWSMobileClient.getInstance().credentialsProvider))
                .build()
            job(transferUtility)
        }.execute()
    }
    enum class Mode {
        Progress, Error, Done
    }
}