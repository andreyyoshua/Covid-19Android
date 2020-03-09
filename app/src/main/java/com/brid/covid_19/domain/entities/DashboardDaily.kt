package com.brid.covid_19.domain.entities

import java.text.DateFormat
import java.text.SimpleDateFormat

data class DashboardDaily(
    val deltaConfirmed: Int,
    val deltaRecovered: Int,
    val mainlandChina: Int,
    val objectid: Int,
    val otherLocations: Int,
    val reportDate: Long,
    val reportDateString: String,
    val totalConfirmed: Int,
    val totalRecovered: Int
) {
    val formattedDate: String
    get() {
        val date = SimpleDateFormat("yyyy/MM/dd").parse(reportDateString)
        return SimpleDateFormat("d MMMM yyyy").format(date)
    }
}