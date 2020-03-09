package com.brid.covid_19.scenes.detail

import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.brid.covid_19.BR
import com.brid.covid_19.R
import com.brid.covid_19.domain.entities.Dashboard
import com.brid.covid_19.domain.entities.DashboardDaily
import com.brid.covid_19.domain.entities.DashboardData
import com.brid.covid_19.domain.entities.DashboardDetail
import com.brid.covid_19.domain.usecase.CovidUseCase
import com.google.android.gms.maps.model.LatLng
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.text.NumberFormat
import java.util.*

class DetailViewModel: ViewModel() {

    private val useCase = CovidUseCase()

    val latLngsLiveData = MediatorLiveData<Array<LatLng>>()
    val isRefreshing = ObservableBoolean()
    val dashboardData = ObservableField<DashboardData>()

    val allItems = ObservableArrayList<DashboardDetail>()
    val items = ObservableArrayList<DashboardDetail>()
    val itemBinding = ItemBinding.of<DashboardDetail> { itemBinding, position, item ->
        itemBinding.set(BR.item, R.layout.row_detail)
    }
    val itemAdapter = object : BindingRecyclerViewAdapter<DashboardDetail>() {
        init {
            setHasStableIds(true)
        }
    }

    val keywordSearch = ObservableField<String>()
    val detailLiveData = MediatorLiveData<Array<DashboardDetail>>()

    init {
        keywordSearch.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                keywordSearch.get()?.let { keyword ->
                    items.clear()
                    if (keyword.isNotEmpty()) {
                        items.addAll(allItems.filter { it.countryRegion?.toLowerCase()?.contains(keyword.toLowerCase()) ?: it.provinceState?.toLowerCase()?.contains(keyword.toLowerCase()) ?: false })
                    } else {
                        items.addAll(allItems)
                    }
                    latLngsLiveData.value = items.map { LatLng(it.lat, it.long) }.toTypedArray()
                }
            }
        })
    }

    fun getdetail(url: String) {
        isRefreshing.set(true)
        detailLiveData.addSource(useCase.getDetail(url)) {
            isRefreshing.set(false)
            it.body?.let { detail ->
                detail.forEach { it.dashboardData = dashboardData.get() }
                detail.map { it.lat }
                allItems.addAll(detail)
                items.addAll(detail)

                latLngsLiveData.value = detail.map { LatLng(it.lat, it.long) }.toTypedArray()
            }
            detailLiveData.value = it.body
        }
    }
}