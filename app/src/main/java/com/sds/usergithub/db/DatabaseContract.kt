package com.sds.usergithub.db

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class UserColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "tb_user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
        }
    }
}