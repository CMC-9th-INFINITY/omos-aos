package com.infinity.omos.data.user

fun Profile.toPresentation(): ProfileModel {
    return ProfileModel(
        nickname = nickname,
        profileUrl = profileUrl,
        userId = userId
    )
}