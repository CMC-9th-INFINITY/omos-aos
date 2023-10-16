package com.infinity.omos.data.user

import com.infinity.omos.data.user.profile.DjProfile
import com.infinity.omos.data.user.profile.DjProfileModel
import com.infinity.omos.data.user.profile.Profile
import com.infinity.omos.data.user.profile.ProfileCount
import com.infinity.omos.data.user.profile.ProfileCountModel
import com.infinity.omos.data.user.profile.ProfileModel

fun Profile.toPresentation(): ProfileModel {
    return ProfileModel(
        nickname = nickname,
        profileUrl = profileUrl,
        userId = userId
    )
}

fun Profile.toDjPresentation(): ProfileModel {
    return ProfileModel(
        nickname = "DJ $nickname",
        profileUrl = profileUrl,
        userId = userId
    )
}

fun ProfileCount.toPresentation(): ProfileCountModel {
    return ProfileCountModel(
        followerCount = followerCount,
        followingCount = followingCount,
        recordsCount = recordsCount
    )
}

fun DjProfile.toPresentation(): DjProfileModel {
    return DjProfileModel(
        count = count.toPresentation(),
        isFollowed = isFollowed,
        profile = profile.toDjPresentation()
    )
}