package com.example.habittrainer

import android.graphics.Bitmap

class BooleanHabit (title : String, description : String, image : Bitmap, val doneToday : Boolean) : Habit(title, description, image)