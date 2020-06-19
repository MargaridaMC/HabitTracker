package com.example.habittrainer.db

import android.content.ContentValues
import android.content.Context
import com.example.habittrainer.*
import org.joda.time.DateTime
import org.joda.time.Interval

class TimeDbTable (context: Context) {

    private val TAG = HabitDbTable::class.simpleName
    private val dbHelper = HabitTrainerDb(context)

    fun updateTimeEntry(habit : Habit){
        val db = dbHelper.writableDatabase

        val today = DateTime.now().toFormattedString()
        val values = ContentValues()
        with(values){
            put(TimeEntry.DONE_COL, habit.getCount())
        }
        db.update(TimeEntry.TABLE_NAME, values, "${TimeEntry.HABIT_ID_FK_COL} = ? AND ${TimeEntry.DATE_COL} = ?",
            arrayOf("${habit._id}", today))

        db.close()
    }

    fun getLastEntryFromHabitIDDate(habitID : Long, date : DateTime) : Int{
        val db = dbHelper.readableDatabase

        val cursor = db.doQuery(TimeEntry.TABLE_NAME, arrayOf(TimeEntry.DONE_COL, TimeEntry._ID),
            "${TimeEntry.HABIT_ID_FK_COL} = ? AND ${TimeEntry.DATE_COL} = ?",
            arrayOf("$habitID", date.toFormattedString()))
        val count = if(cursor.moveToLast()){
            cursor.getInt(TimeEntry.DONE_COL)
        } else {
            -1
        }

        cursor.close()
        db.close()
        return count
    }

    fun addTimeEntriesForDate(date: DateTime, allHabitIDs: List<Long>) {
        val db = dbHelper.writableDatabase
        for(id in allHabitIDs){
            val values = ContentValues()
            with(values){
                put(TimeEntry.DATE_COL, date.toFormattedString())
                put(TimeEntry.DONE_COL, 0)
                put(TimeEntry.HABIT_ID_FK_COL, id)
            }
            db.insert(TimeEntry.TABLE_NAME, null, values)
        }
        db.close()
    }

    fun addTimeEntriesForDates(timeInterval : Interval, allHabitIDs: List<Long>){
        val db = dbHelper.writableDatabase
        var date = timeInterval.start
        do {
            addTimeEntriesForDate(date, allHabitIDs)
            date = date.plusDays(1)
        }
        while(date <= timeInterval.end)

        db.close()
    }
}