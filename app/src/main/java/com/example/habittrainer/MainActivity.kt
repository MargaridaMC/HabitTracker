package com.example.habittrainer

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittrainer.db.HabitDbTable
import com.example.habittrainer.db.TimeDbTable
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.DateTime
import org.joda.time.Interval


class MainActivity : AppCompatActivity(), OnHabitChangedListener{

    public val HABIT_ID_EXTRA = "habitID"

    // Get reference to textView -- the "Java" way
    // -- we have a nullable type and a var so that it can be initialized here and assigned later
    // private var tvDescription: TextView? = null

    // The Kotlin way:
    // - use lateinit modifier to state that this variable doesn't need to be initialized just yet,
    // so it doesn't need to be null here -- we ensure that we will initialize it before use
    // private lateinit var tvDescription: TextView

    // Even better way:
    // the kotlin-android-extensions plugin creates member variables for each of the elements with
    // an id in the layout. So we already have access to the variables tv_description, tv_title,
    // iv_icon and can use them directly without declaring them.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val allHabits = HabitDbTable(this).readAllHabits()

        // Check the last time the app was opened so that we can fill in the days with no information
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val lastUsedDateString = sharedPref.getString(getString(R.string.last_day_used), "")

        val today = DateTime.now()
        val allHabitIDs : List<Long> = allHabits.map { it._id }

        if(lastUsedDateString == ""){
            // App is being used for the first time
            // Fill in time entries for today only
            TimeDbTable(this).addTimeEntriesForDate(today, allHabitIDs)

            // Write today as the last day of usage in shared preferences
            with (sharedPref.edit()) {
                putString(getString(R.string.last_day_used), today.toFormattedString())
                apply()
            }

        } else if(lastUsedDateString != today.toFormattedString()) {

            // Fill in all time entries between this date and today
            val lastUsedDate = lastUsedDateString?.toDate()
            if(lastUsedDate != null) {
                val interval = Interval(lastUsedDate, today)
                TimeDbTable(this).addTimeEntriesForDates(interval, allHabitIDs)
            }

            // Write today as the last day of usage in shared preferences
            with (sharedPref.edit()) {
                putString(getString(R.string.last_day_used), today.toFormattedString())
                apply()
            }

        }



        // Get reference to textView -- the "Java" way
        // We need to use the !! or ? operator to make sure that the object tvDescription is not null
        // as this object is nullable -- NOT COOL, avoid this at all times
        // This implies that if tvDescription is null the line will not be executed.
        /*tvDescription = findViewById(R.id.tv_description)
        tvDescription?.text = "A refreshing glass of water gets you hydrated"
         */

        // The Kotlin way
        /*tvDescription = findViewById(R.id.tv_description)
        tvDescription.text = "A refreshing glass of water gets you hydrated"
         */

        // Even better:
        /*
        tv_title.text = getString(R.string.drink_water)
        tv_description.text = getString(R.string.drink_water_desc)
        iv_icon.setImageResource(R.drawable.water)
         */

        // Now using the recycler view instead of just a card view
        // increases performance
        // - tells the recyclerView that the size of each other cards it contains is constant
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = HabitsAdapter(allHabits, this)


        val itemTouchHelper = ItemTouchHelper(SimpleItemTouchHelperCallback(this,
            rv.adapter as HabitsAdapter
        ))
        itemTouchHelper.attachToRecyclerView(rv)
    }

    // Override necessary methods needed to have a menu:
    // - onCreateOptionsMenu
    // - onOptionsItemSelected
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate menu layout into menu object
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Listener for when an options item is selected in the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Check which item was clicked
        if(item.itemId == R.id.add_habit){
            // .class is replaced by ::class.java because we are referencing the java class
            switchTo(CreateHabitActivity::class.java)
        } else if(item.itemId == R.id.see_progress){
            val toast = Toast.makeText(this, "Feature not yet implemented.",
                Toast.LENGTH_LONG)
            toast.show()
        }
        return true
    }

    private fun switchTo(c: Class<*>){ // any class
        val intent = Intent(this, c)
        startActivity(intent)
    }

    override fun editHabit(habit: Habit) {
        val intent = Intent(this, CreateHabitActivity::class.java)
        intent.putExtra(HABIT_ID_EXTRA, habit._id)
        startActivity(intent)
    }

    override fun updateHabit(habit: Habit) {
        TimeDbTable(this).updateTimeEntry(habit)
        hideKeyboard()
        rv.requestFocus()
    }

    override fun deleteHabit(habit: Habit) {
        confirmHabitDelete(habit)
    }

    private fun confirmHabitDelete(habit: Habit){
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Are you sure you want to delete this habit?")
        alertDialogBuilder.setPositiveButton("Yes!") { _: DialogInterface, _: Int ->
            HabitDbTable(this).deleteHabit(habit)
            finish()
            startActivity(intent)
        }
        alertDialogBuilder.setNegativeButton("No") { _: DialogInterface, _: Int -> return@setNegativeButton}

        alertDialogBuilder.show()
    }
}