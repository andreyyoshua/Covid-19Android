package com.brid.covid_19.domain.entities

import java.io.Serializable

data class DashboardData(
    val detail: String,
    val value: Int,
    val case: DashboardEnum
): Serializable