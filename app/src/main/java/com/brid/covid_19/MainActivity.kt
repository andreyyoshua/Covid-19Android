package com.brid.covid_19

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.brid.covid_19.databinding.ActivityMainBinding
import com.brid.covid_19.domain.entities.Dashboard
import com.brid.covid_19.scenes.detail.DetailActivity
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.activity_main.*
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.animation.Easing
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        dataBinding.viewModel = viewModel

        viewModel.isRefreshing.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel.isRefreshing.get()) {
                    viewModel.getDaily()
                    viewModel.getDashboard()
                }
            }
        })
        viewModel.dashboardLiveData.observe(this@MainActivity, Observer {
            it?.let { setData(it) }
        })
        viewModel.dailyLiveData.observe(this@MainActivity, Observer {  })

        viewModel.isRefreshing.set(true)
        infected_layout.setOnClickListener {

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.data, viewModel.dashboard.get()?.confirmed)
            startActivity(intent)
        }

        recovered_layout.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.data, viewModel.dashboard.get()?.recovered)
            startActivity(intent)
        }

        death_layout.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.data, viewModel.dashboard.get()?.deaths)
            startActivity(intent)
        }

        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)

        chart.dragDecelerationFrictionCoef = 0.95f

        chart.isDrawHoleEnabled = false
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f

        chart.setDrawCenterText(true)

        chart.rotationAngle = 0f
        // enable rotation of the chart by touch
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true


        chart.animateY(1400, Easing.EaseInOutQuad)




        // entry label styling
        chart.setEntryLabelColor(Color.WHITE)
        chart.setEntryLabelTextSize(12f)

    }


    private fun setData(data: Dashboard) {
        val entries = ArrayList<PieEntry>()

        entries.add(PieEntry(data.confirmed.value.toFloat()))
        entries.add(PieEntry(data.recovered.value.toFloat()))
        entries.add(PieEntry(data.deaths.value.toFloat()))

        val dataSet = PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.sliceSpace = 3f;
        dataSet.iconsOffset = MPPointF(0f, 40f);
        dataSet.selectionShift = 5f;


        dataSet.setColors(
            Color.parseColor(data.confirmed.case.colorString),
            Color.parseColor(data.recovered.case.colorString),
            Color.parseColor(data.deaths.case.colorString))
        //dataSet.setSelectionShift(0f);

        val data = PieData(dataSet);
        data.setValueFormatter(PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(tfLight);
        chart.data = data;

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

}
