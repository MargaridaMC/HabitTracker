package com.example.habittrainer

import android.graphics.Bitmap

class NumericHabit(title: String, description: String, image: Bitmap?, var numberTimesDoneToday: Int, _id: Long) : Habit(title, description, image, _id) {
    override fun setCount(count : Int) {
        this.numberTimesDoneToday = count
    }

    override fun getCount(): Int {
        return numberTimesDoneToday
    }

}