package com.brid.covid_19

import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.brid.covid_19.domain.entities.Dashboard
import com.brid.covid_19.domain.entities.DashboardDaily
import com.brid.covid_19.domain.usecase.CovidUseCase
import com.google.android.gms.maps.model.Dash
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.text.NumberFormat
import java.util.*

class MainViewModel: ViewModel() {

    private val useCase = CovidUseCase()
    val infected = ObservableField<String>()
    val recovered = ObservableField<String>()
    val death = ObservableField<String>()
    val dashboard = ObservableField<Dashboard>()
    val isRefreshing = ObservableBoolean()

    val items = ObservableArrayList<DashboardDaily>()
    val itemBinding = ItemBinding.of<DashboardDaily> { itemBinding, position, item ->
        itemBinding.set(BR.item, R.layout.row_daily)
    }

    val dashboardLiveData = MediatorLiveData<Dashboard>()
    val dailyLiveData = MediatorLiveData<Array<DashboardDaily>>()

    fun getDashboard() {
        isRefreshing.set(true)
        dashboardLiveData.addSource(useCase.get()) {
            isRefreshing.set(false)
            it.body?.let { dashboard ->
                this.dashboard.set(dashboard)
                infected.set(NumberFormat.getInstance(Locale.US).format(dashboard.confirmed.value))
                recovered.set(NumberFormat.getInstance(Locale.US).format(dashboard.recovered.value))
                death.set(NumberFormat.getInstance(Locale.US).format(dashboard.deaths.value))
            }
            dashboardLiveData.value = it.body
        }
    }

    fun getDaily() {
        isRefreshing.set(true)
        dailyLiveData.addSource(useCase.getDaily()) {
            isRefreshing.set(false)
            items.clear()
            it.body?.reversedArray()?.let { items.addAll(it) }
            dailyLiveData.value = it.body
        }
    }


}