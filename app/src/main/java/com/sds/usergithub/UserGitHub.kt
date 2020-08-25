package com.sds.usergithub

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserGitHub (
    var id: Int? = null,
    var username : String? = null,
    val name : String? = null,
    var avatar : String? = null,
    val company : String? = null,
    var location : String? = null,
    var repository: String? = null,
    val follower : String? = null,
    val following : String? = null
) : Parcelable
