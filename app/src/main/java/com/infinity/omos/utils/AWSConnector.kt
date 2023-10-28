package com.infinity.omos.utils

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import timber.log.Timber
import java.io.File

enum class S3ImageFolder(val str: String) {
    PROFILE("profile"), RECORD("record")
}

class AWSConnector(private val context: Context) {

    private lateinit var transferUtility: TransferUtility

    init {
        createTransferUtility()
    }

    private fun createTransferUtility() {
        val awsConfiguration = AWSConfiguration(context)
        val credentialsProvider = CognitoCachingCredentialsProvider(
            context,
            awsConfiguration
        )

        val s3Client = AmazonS3Client(credentialsProvider, Region.getRegion(Regions.AP_NORTHEAST_2))
        transferUtility = TransferUtility.builder()
            .context(context)
            .awsConfiguration(awsConfiguration)
            .s3Client(s3Client)
            .build()
    }

    fun uploadFile(file: File, folder: S3ImageFolder, userId: Int, bucketName: String): String {
        val currentTime = System.currentTimeMillis()
        val objectKey = when (folder) {
            S3ImageFolder.RECORD -> "${S3ImageFolder.RECORD.str}/$userId$currentTime.png"
            S3ImageFolder.PROFILE -> "${S3ImageFolder.PROFILE.str}/$userId.png"
        }
        val transferObserver: TransferObserver = transferUtility.upload(
            bucketName,
            objectKey,
            file
        )
        transferObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                Timber.d("onStateChanged: $state")
                if (TransferState.COMPLETED == state) {
                    Timber.d("Image Upload!")
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {}
            override fun onError(id: Int, ex: Exception) {
                Timber.e("onError: $ex")
            }
        })

        return objectKey
    }

    /*    fun download(objectKey: String?) {
            val fileDownload = File(getCacheDir(), objectKey)
            val transferObserver: TransferObserver = transferUtility.download(
                BUCKET_NAME,
                objectKey,
                fileDownload
            )
            transferObserver.setTransferListener(object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState) {
                    Log.d("AWSConnector", "onStateChanged: $state")
                    if (TransferState.COMPLETED.equals(state)) {
                        imageViewDownload.setImageBitmap(BitmapFactory.decodeFile(fileDownload.getAbsolutePath()))
                        progressDialogDownload.dismiss()
                        Toast.makeText(this@MainActivity, "Image downloaded", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {}
                override fun onError(id: Int, ex: Exception) {
                    progressDialogDownload.dismiss()
                    Log.e("AWSConnector", "onError: ", ex)
                    Toast.makeText(this@MainActivity, "Error: " + ex.message, Toast.LENGTH_SHORT).show()
                }
            })
        }*/
}