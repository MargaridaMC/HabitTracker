package com.example.habittrainer

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.habittrainer.db.HabitDbTable
import com.example.habittrainer.db.TimeDbTable
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_progress.*
import org.joda.time.DateTime


class ProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        // Setup plot -- Example from https://martintjandra.wordpress.com/2019/05/21/mpandroidchart-explained-in-kotlin/
       /* val revenueComp1 = arrayListOf(10000f, 20000f, 30000f, 40000f)
        val revenueComp2 = arrayListOf(12000f, 23000f, 35000f, 48000f)

        val entries1 = revenueComp1.mapIndexed { index, value ->
            Entry(index.toFloat(), value) }

        val entries2 = revenueComp2.mapIndexed { index, value ->
            Entry(index.toFloat(), value) }

        val lineDataSet1 = LineDataSet(entries1, "Company 1")
        lineDataSet1.color = Color.RED
        lineDataSet1.setDrawValues(false)
        lineDataSet1.axisDependency = YAxis.AxisDependency.LEFT

        val lineDataSet2 = LineDataSet(entries2, "Company 2")
        lineDataSet2.color = Color.BLUE
        lineDataSet1.setDrawValues(false)
        lineDataSet2.axisDependency = YAxis.AxisDependency.LEFT

        lineChartView.data = LineData(lineDataSet1, lineDataSet2)
        lineChartView.axisLeft.mAxisMaximum = 1f
        lineChartView.axisLeft.mAxisMinimum = -1f
        lineChartView.axisLeft.mAxisRange = 2f

        // Refresh graph
        lineChartView.invalidate()*/

        lineChartView.data = LineData()

        val colors = listOf(Color.BLUE, Color.RED, Color.BLACK)

        val allHabits = HabitDbTable(this).readAllHabits()

        for((i, habit) in allHabits.withIndex()) {
            val timeEntries = TimeDbTable(this).getAllTimeEntriesForHabit(habit._id)
            var dates: List<DateTime> = timeEntries.map { entry -> entry.key }
            val entries = timeEntries.values.mapIndexed { idx, value ->
                Entry(
                    idx.toFloat(),
                    value.toFloat()
                )
            }
            val lineDataSet = LineDataSet(entries, habit.title)

            lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
            lineDataSet.setDrawValues(false)
            lineDataSet.color = colors[i]

            lineChartView.data.addDataSet(lineDataSet)
        }




        lineChartView.invalidate()
    }
}