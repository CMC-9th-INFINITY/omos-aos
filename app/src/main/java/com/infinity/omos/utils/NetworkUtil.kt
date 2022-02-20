package com.infinity.omos.utils

import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.ResponseErrorBody
import okhttp3.ResponseBody

object NetworkUtil {
    fun getErrorResponse(errorBody: ResponseBody): ResponseErrorBody? {
        return RetrofitAPI.instance?.responseBodyConverter<ResponseErrorBody>(
            ResponseErrorBody::class.java,
            ResponseErrorBody::class.java.annotations
        )?.convert(errorBody)
    }
}