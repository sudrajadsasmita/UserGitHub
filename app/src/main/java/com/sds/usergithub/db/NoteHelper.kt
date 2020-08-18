package com.sds.usergithub.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.sds.usergithub.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.sds.usergithub.db.DatabaseContract.NoteColumns.Companion._ID
import java.sql.SQLException

class NoteHelper(context: Context) {
    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: NoteHelper?= null
        fun getInstance(context: Context):NoteHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: NoteHelper(context)
            }
        private lateinit var database: SQLiteDatabase
    }
    init {
        databaseHelper = DatabaseHelper(context)
    }
    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }
    fun close(){
        databaseHelper.close()
        if (database.isOpen)
            database.close()
    }
    fun querryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }
    fun querryById(id: String): Cursor{
        return  database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }
    fun insert(values: ContentValues?): Long{
        return database.insert(DATABASE_TABLE, null, values)
    }
    fun deleteById(id: String):Int{
        return database.delete(DATABASE_TABLE, "$_ID = $id", null)
    }
}