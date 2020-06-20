package com.example.habittrainer.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.util.Log
import com.example.habittrainer.*
import com.example.habittrainer.db.HabitEntry.DESCR_COL
import com.example.habittrainer.db.HabitEntry.HABIT_TYPE_COL
import com.example.habittrainer.db.HabitEntry.IMAGE_COL
import com.example.habittrainer.db.HabitEntry.TABLE_NAME
import com.example.habittrainer.db.HabitEntry.TITLE_COL
import com.example.habittrainer.db.HabitEntry._ID
import org.joda.time.DateTime
import java.io.ByteArrayOutputStream

// Represents table in our database for all the habits
// All functionality to read and write to and from the database

class HabitDbTable (private val context: Context) {

    private val TAG = HabitDbTable::class.simpleName
    private val dbHelper = HabitTrainerDb(context)

    // Save habit to the database
    // Returns a long which corresponds to the ID of this object in the database
    fun store(habit: Habit) : Long {
        val db = dbHelper.writableDatabase

        /*val values = ContentValues()
        // values.put(col_name, value)
        *//*values.put(HabitEntry.TITLE_COL, habit.title)
        values.put(HabitEntry.DESCR_COL, habit.description)
        // Use toByteArray because we don't want to store a Bitmap
        values.put(HabitEntry.IMAGE_COL, toByteArray(habit.image))
        *//*

        // Better
        with(values){
            put(TITLE_COL, habit.title)
            put(DESCR_COL, habit.description)
            if(habit.image != null){
                put(IMAGE_COL, toByteArray(habit.image))
            }

            if(habit is BooleanHabit){
                put(HABIT_TYPE_COL, HabitTypeEnum.BOOLEAN.name)
                //val count = habit.doneToday.toInt()
                //put(HABIT_COUNT_COL, count)
            } else if (habit is NumericHabit){
                put(HABIT_TYPE_COL, HabitTypeEnum.NUMERIC.name)
                //put(HABIT_COUNT_COL, habit.numberTimesDoneToday)
            }

        }

        // arguments:
        // - table name
        // - nullColumnHack - what to store if any value of the object is null
        // (in this case null == if any value is null then the object is not stored)
        // - contentValues - values for each of the fields in our data table
        // val id = db.insert(HabitEntry.TABLE_NAME, null, values)

        // However this has to be more robust
        // You should always have these steps:
        // 1. begin transaction
        // 2. Do whatever actions you want like inserting an element to the db
        // 3. setTransactionSuccessful
        // 4. end transaction
        // 5. CLOSE THE DATABASE (IMPORTANT)
        *//*db.beginTransaction()
        val id = try {
            val returnValue = db.insert(HabitEntry.TABLE_NAME, null, values)
            db.setTransactionSuccessful()
            returnValue
        } finally {
            db.endTransaction()
        }

        db.close()*/

        val values = ContentValues().put(habit)

        // However this is very lengthy code and will need to be repeated whenever you want to interact with the database
        // The only code that changes is the actual interaction
        // Create extension function for the database function to do all these steps
        // Don't need to to it.insert because we made the input of this method an extension function of the SQLiteDatabase
        val id = db.transaction { insert(TABLE_NAME, null, values) }

        // Remember that data classes (such as Habit) already come with a toString method
        Log.d(TAG, "Stored new habit to the DB $habit")

        return id
    }

    fun update(habit: Habit) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().put(habit)
        db.update(TABLE_NAME, values, "$_ID = ?", arrayOf("${habit._id}"))
        db.close()
    }//152??

    fun ContentValues.put(habit: Habit): ContentValues {


        put(TITLE_COL, habit.title)
        put(DESCR_COL, habit.description)
        if(habit.image != null){
            put(IMAGE_COL, toByteArray(habit.image))
        }

        if(habit is BooleanHabit){
            put(HABIT_TYPE_COL, HabitTypeEnum.BOOLEAN.name)
        } else if (habit is NumericHabit){
            put(HABIT_TYPE_COL, HabitTypeEnum.NUMERIC.name)
        }

        return this
    }

    fun readAllHabits() : List<Habit>{

        val columns = arrayOf(
            _ID, TITLE_COL, DESCR_COL,
            IMAGE_COL, HABIT_TYPE_COL
        )

        val db = dbHelper.readableDatabase

        // Select all collumns in our table and order them in ascending order by ID
        // Returns a cursor = what allows you to go through the database and get all the elements you want
        // In a database it will go through each row of the database
        val cursor = db.doQuery(TABLE_NAME, columns)
        return parseHabitsFrom(cursor)
    }

    private fun parseHabitsFrom(cursor: Cursor): MutableList<Habit> {
        val habits = mutableListOf<Habit>()
        // Return false once there are no more entries for the cursor to read from

        while (cursor.moveToNext()) {
            habits.add(cursor.getHabit(context))
        }

        // Always close the cursor so you free the resources
        cursor.close()

        return habits
    }

    private fun toByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        // Writes the bitmap to the stream in a compressed form (in PNG)
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    fun deleteHabit(habit: Habit) {
        val db = dbHelper.writableDatabase
        db.delete(TABLE_NAME, "$_ID = ?", arrayOf("${habit._id}"))
        db.close()
    }

    fun getHabitByID(habitID: Long): Habit {
        val db = dbHelper.readableDatabase
        val cursor = db.doQuery(HabitEntry.TABLE_NAME, HabitEntry.getAllColumns(),
            "${HabitEntry._ID} = ?", arrayOf("$habitID"))
        cursor.moveToFirst()
        val habit = cursor.getHabit(context)
        db.close()
        return habit
    }



}


