package com.example.habittrainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittrainer.db.HabitDbTable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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
        rv.adapter = HabitsAdapter(HabitDbTable(this).readAllHabits())
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
        }
        return true
    }

    private fun switchTo(c: Class<*>){ // any class
        val intent = Intent(this, c)
        startActivity(intent)
    }
}