package com.brid.covid_19.domain

import androidx.lifecycle.LiveData
import com.brid.covid_19.domain.entities.Dashboard
import com.brid.covid_19.domain.entities.DashboardDaily
import com.brid.covid_19.domain.entities.DashboardDetail
import com.brid.covid_19.domain.utils.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface CovidService {

    @GET("api")
    fun get(): LiveData<ApiResponse<Dashboard>>

    @GET("api/daily")
    fun getDaily(): LiveData<ApiResponse<Array<DashboardDaily>>>

    @GET
    fun getDetail(@Url url: String): LiveData<ApiResponse<Array<DashboardDetail>>>
}