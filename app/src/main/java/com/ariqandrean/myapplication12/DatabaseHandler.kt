package com.ariqandrean.myapplication12

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper
    (context, DATABASE_NAME, null, DATABASE_VERSION) {

    // parameter database sqlite
    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "ActivityDataBase"

        private val Table_ACTIVITY = "ActivityTable"

        private val Key_id = "id"
        private val Key_time = "Time"
        private val Key_description = "description"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val Create_Contacts_Table = ("Create Table" + Table_ACTIVITY + "("
                + Key_id + " Integer Primary Key, "
                + Key_time + " Text, "
                + Key_description + " Text) " )
        db?.execSQL(Create_Contacts_Table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop table if exists $Table_ACTIVITY")
        onCreate(db)
    }

    fun addActivity(act:MyActivityModel):Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Key_time,act.time)
        contentValues.put(Key_time,act.description)

        val success = db.insert(Table_ACTIVITY,null,contentValues)
        db.close()
        return success
    }

    fun viewSchedule(): ArrayList<MyActivityModel>{
        val actList:ArrayList<MyActivityModel> = ArrayList<MyActivityModel>()
        val selectQuery = "Select * From $Table_ACTIVITY "

        val db = this.readableDatabase

        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id : Int
        var time:String
        var description :String

        if (cursor.moveToFirst()){
            do{
                id=cursor.getInt(cursor.getColumnIndex(Key_id))
                time=cursor.getString(cursor.getColumnIndex(Key_time))
                description = cursor.getString(cursor.getColumnIndex(Key_description))

                val act = MyActivityModel(id=id , time= time , description = description)
                actList.add(act)
            }while (cursor.moveToNext())
        }
        return actList
    }
    fun deleteAcitivity(act: MyActivityModel):Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Key_id,act.id)

        val success = db.delete (Table_ACTIVITY, Key_id + "=" + act.id,null)

        db.close()
        return success
    }

    fun updateActivity(act:MyActivityModel):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Key_id,act.id)
        contentValues.put(Key_time,act.time)
        contentValues.put(Key_description,act.description)

        val success = db.update(Table_ACTIVITY,contentValues,Key_id + "=" + act.id , null)
        db.close()
        return success
    }
}