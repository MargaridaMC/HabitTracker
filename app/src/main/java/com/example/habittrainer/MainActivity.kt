package com.example.habittrainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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
        // - tells the recyclerView that the size of each othe cards it contains is constant
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = HabitsAdapter(getSampleHabits())
    }
}