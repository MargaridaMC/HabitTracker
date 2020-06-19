package com.example.habittrainer

import android.graphics.Bitmap

class NumericHabit(title : String, description : String, image : Bitmap, val numberTimesDoneToday : Int) : Habit(title, description, image)