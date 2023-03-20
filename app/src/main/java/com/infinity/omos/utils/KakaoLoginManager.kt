package com.infinity.omos.utils

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import timber.log.Timber

class KakaoLoginManager(private val context: Context) {

    private lateinit var success: (Long) -> Unit

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            when (error.toString()) {
                AuthErrorCause.AccessDenied.toString() -> Timber.e("접근이 거부 됨(동의 취소)")
                AuthErrorCause.InvalidClient.toString() -> Timber.e("유효하지 않은 앱")
                AuthErrorCause.InvalidGrant.toString() -> Timber.e("인증 수단이 유효하지 않아 인증할 수 없는 상태")
                AuthErrorCause.InvalidRequest.toString() -> Timber.e("요청 파라미터 오류")
                AuthErrorCause.InvalidScope.toString() -> Timber.e("유효하지 않은 scope ID")
                AuthErrorCause.Misconfigured.toString() -> Timber.e("설정이 올바르지 않음(android key hash)")
                AuthErrorCause.ServerError.toString() -> Timber.e("서버 내부 에러")
                AuthErrorCause.Unauthorized.toString() -> Timber.e("앱이 요청 권한이 없음")
                else -> Timber.e("기타 에러")
            }
        } else if (token != null) {
            // 로그인 성공 시

            UserApiClient.instance.me { user, _ ->
                success(user?.id ?: -1)
            }
        }
    }

    fun login(s: (Long) -> Unit) = with(UserApiClient.instance) {
        success = s
        if (isKakaoTalkLoginAvailable(context)) {
            loginWithKakaoTalk(context, callback = callback)
        } else {
            loginWithKakaoAccount(context, callback = callback)
        }
    }
}