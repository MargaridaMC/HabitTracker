package com.example.habittrainer

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.single_card_boolean_habit.view.*
import kotlinx.android.synthetic.main.single_card_boolean_habit.view.iv_icon
import kotlinx.android.synthetic.main.single_card_boolean_habit.view.tv_description
import kotlinx.android.synthetic.main.single_card_boolean_habit.view.tv_title
import kotlinx.android.synthetic.main.single_card_numeric_habit.view.*

class HabitsAdapter(private val habits: List<Habit>, private val OnHabitChangedListener : OnHabitChangedListener) :
    RecyclerView.Adapter<HabitsAdapter.HabitViewHolder>() {

    private val TYPE_NUMERIC = 1
    private val TYPE_BOOLEAN = 2

    // Defines the contents of the card
    override fun onBindViewHolder(holder: HabitViewHolder, idx: Int) {
        val habit = habits[idx]
        holder.card.tv_title.text = habit.title
        holder.card.tv_description.text = habit.description
        if(habit.image == null){
            holder.card.iv_icon.setImageResource(android.R.drawable.ic_menu_camera)
        } else {
            holder.card.iv_icon.setImageBitmap(habit.image)
        }

        if(habit is NumericHabit){
            holder.card.count.setText(habit.numberTimesDoneToday.toString())

            holder.card.count.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    //Perform Code
                    val c = Integer.parseInt(holder.card.count.text.toString())
                    habit.setCount(c)
                    OnHabitChangedListener.updateHabit(habit)
                    return@OnKeyListener true
                }
                false
            })

        } else if(habit is BooleanHabit) {
            // Check whether checkbox should be clicked
            if(habit.doneToday){
                holder.card.checkBox.isChecked = true
            }

            // Set on click listener for checkbox
            holder.card.checkBox.setOnClickListener { v ->
                if(v is CheckBox){
                    if(v.isChecked){
                        habit.setCount(1)
                    } else {
                        habit.setCount(0)
                    }
                    OnHabitChangedListener.updateHabit(habit)
                }
            }
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
        return if (habits[position] is NumericHabit) {
            TYPE_NUMERIC
        } else {
            TYPE_BOOLEAN
        }
    }

    fun removeAt(position: Int) {
        //habits.drop(position)
        notifyItemRemoved(position)
        OnHabitChangedListener.deleteHabit(habits[position])
        // Remove from database
    }

    fun editHabit(position: Int) {
        notifyItemRemoved(position)
        OnHabitChangedListener.editHabit(habits[position])
    }

    // in Java you would call super(view) in the constructor
    class HabitViewHolder(val card : View) : RecyclerView.ViewHolder(card)


}

interface OnHabitChangedListener {
    fun editHabit(habit : Habit)
    fun updateHabit(habit : Habit)
    fun deleteHabit(habit : Habit)
}
