package com.infinity.omos.data.user.profile

data class DjProfileModel(
    val count: ProfileCountModel,
    val isFollowed: Boolean?,
    val profile: ProfileModel
)