package com.sds.usergithub.helper

import android.database.Cursor
import android.provider.ContactsContract
import com.sds.usergithub.UserGitHub
import com.sds.usergithub.db.DatabaseContract

object MappingHelper {
    fun mapCursorTOArrayList(noteCursor: Cursor?): ArrayList<UserGitHub>{
        val noteList = ArrayList<UserGitHub>()

        noteCursor?.apply {
            while (moveToNext()){
                val id =  getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                noteList.add((UserGitHub(id,username, null, avatar, null, null, null, null, null)))
            }
        }
        return noteList
    }
}