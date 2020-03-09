package com.brid.covid_19.scenes.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.brid.covid_19.R
import com.brid.covid_19.databinding.ActivityDetailBinding
import com.brid.covid_19.domain.entities.DashboardData
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class DetailActivity : AppCompatActivity() {

    companion object {
        val data = "data"
        val type = "type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataBinding = DataBindingUtil.setContentView<ActivityDetailBinding>(this, R.layout.activity_detail)
        val viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        dataBinding.viewModel = viewModel

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        (intent.getSerializableExtra(data) as? DashboardData)?.let { data ->
            viewModel.dashboardData.set(data)
            viewModel.isRefreshing.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    if (viewModel.isRefreshing.get()) {
                        viewModel.getdetail(data.detail)
                    }
                }
            })

            viewModel.detailLiveData.observe(this@DetailActivity, Observer {  })
            viewModel.isRefreshing.set(true)
        }

        val sheetBehavior = BottomSheetBehavior.from(bottom_sheet_container)
        sheetBehavior.peekHeight = 1000
        sheetBehavior.isHideable = false
        sheetBehavior.isFitToContents = true
        sheetBehavior.skipCollapsed = true

        (supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.let { mapFragment ->
            mapFragment.getMapAsync { map ->

                val markers = ArrayList<Marker>()
                viewModel.latLngsLiveData.observe(this, Observer { latLngs ->
                    markers.forEach { marker -> marker.remove() }
                    val builder = LatLngBounds.builder()
                    latLngs.forEach { latLng ->
                        builder.include(latLng)
                        markers.add(map.addMarker(MarkerOptions().position(latLng)))
                    }

                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200))
                })
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
