package com.example.habittrainer

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

// Function that takes in the transaction we want to do with the database
// and does all the extra necessary steps for it
// Unit is like void here
// SQLiteDatabase.() means that the input function is in itself an extension function to the SQLiteDatabase
// inline is useful in higher order functions such as this one. This implies that:
// - the compiler will replace the function call with the code inside this function
// - we get to keep the code modular without losing the performance
// because if it wasn't inline then the compiler would have to create anonymous objects here
inline fun <T> SQLiteDatabase.transaction(function: SQLiteDatabase.() -> T) : T{
    // All the following methods are applied on a SQLite object
    // including the inputted function because it is in itself an extension on the SQLite database
    beginTransaction()
    val result = try{
        // Adding an input to the function means there wil never be the problem of applying this transaction to the wrong database
        val returnValue = function()
        setTransactionSuccessful()
        returnValue
    } finally {
        endTransaction()
    }
    close()
    return result
}

fun SQLiteDatabase.doQuery(tableName: String, columns : Array<String>, selection : String? = null,
                                   selectionArgs : Array<String>? = null, groupBy : String? = null,
                                   having : String? = null, orderBy : String? = null) : Cursor {
    return query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy)
}

fun Cursor.getString(columnName : String) = getString(getColumnIndex(columnName))

fun Cursor.getInt(columnName : String) = getInt(getColumnIndex(columnName))

fun Cursor.getLong(columnName : String) = getLong(getColumnIndex(columnName))

fun Cursor.getBitmap(columnName: String) : Bitmap {

    val byteArray = getBlob(getColumnIndex(columnName))
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

}

fun Boolean.toInt() = if (this) 1 else 0

val DATE_STRING_FORMAT = "dd.MMM.yyyy"

fun DateTime.toFormattedString() : String{
    val dateTimeFormatter = DateTimeFormat.forPattern(DATE_STRING_FORMAT)
    return dateTimeFormatter.print(this)
}

fun String.toDate() : DateTime {
    val dateTimeFormatter = DateTimeFormat.forPattern(DATE_STRING_FORMAT)
    return DateTime.parse(this, dateTimeFormatter)
}

// Utilities for hiding the keyboard
/*
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}
*/

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}