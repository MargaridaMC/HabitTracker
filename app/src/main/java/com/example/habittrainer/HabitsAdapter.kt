package com.example.habittrainer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.single_card.view.*

class HabitsAdapter(val habits: List<Habit>) :
    RecyclerView.Adapter<HabitsAdapter.HabitViewHolder>() {

    // Defines the contents of the card
    override fun onBindViewHolder(holder: HabitViewHolder, idx: Int) {
        val habit = habits[idx]
        holder.card.tv_title.text = habit.title
        holder.card.tv_description.text = habit.description
        holder.card.iv_icon.setImageBitmap(habit.image)
    }

    // Creates a View Holder, which corresponds to one item (or one card)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_card, parent, false)
        return HabitViewHolder(view)
    }

    override fun getItemCount() = habits.size

    // in Java you would call super(view) in the constructor
    class HabitViewHolder(val card : View) : RecyclerView.ViewHolder(card)

}