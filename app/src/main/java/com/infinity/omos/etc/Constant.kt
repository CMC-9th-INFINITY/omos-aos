package com.infinity.omos.etc

class Constant {
    enum class ApiState{LOADING, ERROR, DONE, NETWORK}

    companion object {
        // 푸시알림 ID
        const val NOTI_ID = 1
        const val CHANNEL_ID = "default"
    }
}