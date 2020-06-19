package com.example.habittrainer.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// SQLLite helper for our database
// It will more clearly define the schema of our database using SQL code
// Will be used to create, recreate or destroy items in the database

// Having access to the context allows us to have access to some of the Android system context
// e.g. save thing to the file system
class HabitTrainerDb(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    // Define required SQL queries

    // 1. To create the table
    // first create a table with name TABLE_NAME
    // then define each column by giving their name and then their type
    // BLOB == Binary Large Object
    private val SQL_CREATE_ENTRIES = "CREATE TABLE ${HabitEntry.TABLE_NAME}(" +
            "${HabitEntry._ID} INTEGER PRIMARY KEY," + // Unique ID for each element
            "${HabitEntry.TITLE_COL} TEXT," +
            "${HabitEntry.DESCR_COL} TEXT," +
            "${HabitEntry.IMAGE_COL} BLOB," +
            "${HabitEntry.HABIT_TYPE_COL} TEXT," +
            "${HabitEntry.HABIT_COUNT_COL} INTEGER" +
            ")"

    private val SQL_CREATE_TIME_ENTRIES = "CREATE TABLE ${TimeEntry.TABLE_NAME}(" +
            "${TimeEntry._ID} INTEGER PRIMARY KEY," +
            "${TimeEntry.DATE_COL} TEXT," +
            "${TimeEntry.DONE_COL} INTEGER," +
            "${TimeEntry.HABIT_ID_FK_COL} INTEGER," +
            "FOREIGN KEY (${TimeEntry.HABIT_ID_FK_COL}) REFERENCES ${HabitEntry.TABLE_NAME} ON DELETE CASCADE" +
            ")"

    // 2. Delete all the tables we have and recreate everything
    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${HabitEntry.TABLE_NAME};" +
            "DROP TABLE IF EXISTS ${TimeEntry.TABLE_NAME};"

    // What happens when the database is first created
    override fun onCreate(db: SQLiteDatabase?) {
        // executes SQL code to create the table with all columns we need
        db?.execSQL(SQL_CREATE_ENTRIES)
        db?.execSQL(SQL_CREATE_TIME_ENTRIES)
    }

    // What happens when the database version is increased
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        db?.execSQL("PRAGMA foreign_keys=ON")
    }

}