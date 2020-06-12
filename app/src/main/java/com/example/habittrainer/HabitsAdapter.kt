package com.example.habittrainer

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HabitsAdapter(val habits: List<Habit>) :
    RecyclerView.Adapter<HabitsAdapter.HabitViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount() = habits.size

    // in Java you would call super(view) in the constructor
    class HabitViewHolder(val iv : View) : RecyclerView.ViewHolder(iv){

    }
}