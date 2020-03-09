package com.brid.covid_19.domain.entities

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

data class DashboardDetail(
    val confirmed: Int,
    val countryRegion: String?,
    val deaths: Int,
    val lastUpdate: Long,
    val lat: Double,
    val long: Double,
    val provinceState: String?,
    val recovered: Int
) {

    var dashboardData: DashboardData? = null

    val detail: String
    get() {
        if (dashboardData == null) { return "" }
        return when (dashboardData!!.case) {
            DashboardEnum.Infected -> "Confirmed ${NumberFormat.getInstance(Locale.US).format(confirmed)} cases"
            DashboardEnum.Recovered -> "Recovered ${NumberFormat.getInstance(Locale.US).format(recovered)} cases"
            DashboardEnum.Death -> "Death ${NumberFormat.getInstance(Locale.US).format(deaths)} cases"
        }
    }

    val lastUpdateStr: String
    get() {
        val date = Date(lastUpdate)
        return SimpleDateFormat("d MMMM yyyy").format(date)
    }
}