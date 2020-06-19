package com.example.habittrainer

import android.graphics.Bitmap

class BooleanHabit(title: String, description: String, image: Bitmap?, var doneToday: Boolean, _id: Long) : Habit(title, description, image, _id) {
    override fun setCount(doneToday: Int) {
        this.doneToday = doneToday == 1
    }

    override fun getCount(): Int {
        return doneToday.toInt()
    }
}