package com.example.habittrainer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.single_card_boolean_habit.view.iv_icon
import kotlinx.android.synthetic.main.single_card_boolean_habit.view.tv_description
import kotlinx.android.synthetic.main.single_card_boolean_habit.view.tv_title
import kotlinx.android.synthetic.main.single_card_numeric_habit.view.*


class HabitsAdapter(val habits: List<Habit>) :
    RecyclerView.Adapter<HabitsAdapter.HabitViewHolder>() {

    val TYPE_NUMERIC = 1
    val TYPE_BOOLEAN = 2

    // Defines the contents of the card
    override fun onBindViewHolder(holder: HabitViewHolder, idx: Int) {
        val habit = habits[idx]
        holder.card.tv_title.text = habit.title
        holder.card.tv_description.text = habit.description
        holder.card.iv_icon.setImageBitmap(habit.image)
        if(habit is NumericHabit){
            holder.card.count.text = habit.numberTimesDoneToday.toString()
        }
    }

    // Creates a View Holder, which corresponds to one item (or one card)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = if (viewType == TYPE_NUMERIC){
            LayoutInflater.from(parent.context).inflate(R.layout.single_card_numeric_habit, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.single_card_boolean_habit, parent, false)
        }

        return HabitViewHolder(view)
    }

    override fun getItemCount() = habits.size


    override fun getItemViewType(position: Int): Int {
        return if (habits.get(position) is NumericHabit) {
            TYPE_NUMERIC
        } else {
            TYPE_BOOLEAN
        }
    }

    // in Java you would call super(view) in the constructor
    class HabitViewHolder(val card : View) : RecyclerView.ViewHolder(card)

}