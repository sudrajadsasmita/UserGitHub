package com.sds.usergithub.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sds.usergithub.db.DatabaseContract.NoteColumns.Companion.AVATAR
import com.sds.usergithub.db.DatabaseContract.NoteColumns.Companion.COMPANY
import com.sds.usergithub.db.DatabaseContract.NoteColumns.Companion.FOLLOWERS
import com.sds.usergithub.db.DatabaseContract.NoteColumns.Companion.FOLLOWING
import com.sds.usergithub.db.DatabaseContract.NoteColumns.Companion.LOCATION
import com.sds.usergithub.db.DatabaseContract.NoteColumns.Companion.NAME
import com.sds.usergithub.db.DatabaseContract.NoteColumns.Companion.REPOSITORY
import com.sds.usergithub.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.sds.usergithub.db.DatabaseContract.NoteColumns.Companion.USERNAME
import com.sds.usergithub.db.DatabaseContract.NoteColumns.Companion._ID


internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "db_user"

        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_NOTE = "CREATE TABLE ${DatabaseContract.NoteColumns.TABLE_NAME}" +
                "($_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "$USERNAME TEXT NOT NULL,"+
                "$NAME TEXT NOT NULL,"+
                "$AVATAR TEXT NOT NULL,"+
                "$COMPANY TEXT NOT NULL,"+
                "$LOCATION TEXT NOT NULL,"+
                "$REPOSITORY TEXT NOT NULL,"+
                "$FOLLOWERS TEXT NOT NULL,"+
                "$FOLLOWING TEXT NOT NULL)"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }
}