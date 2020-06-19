package com.example.habittrainer.db

import android.provider.BaseColumns

// Define here the schema for our habit database

val DATABASE_NAME = "habittrainer.db"
val DATABASE_VERSION = 10 // 1.0

// BaseColumns is an Android interface to define a schema or all the fields of a table in your database
object HabitEntry : BaseColumns{
    // Definition of an entry in the database
    // "habit" is the only table we'll have, inside of which we'll have 3 columns
    val TABLE_NAME = "habit"
    val _ID = "id"
    val TITLE_COL = "title"
    val DESCR_COL = "description"
    val IMAGE_COL = "image"
    val HABIT_TYPE_COL = "habitType"
}
object TimeEntry : BaseColumns{
    val TABLE_NAME = "habitTracking"
    val _ID = "id"
    val DATE_COL = "date"
    val DONE_COL = "habitDone"
    val HABIT_ID_FK_COL = "habitID_FK"
}