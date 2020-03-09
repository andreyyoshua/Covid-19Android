package com.brid.covid_19.domain.usecase

import androidx.lifecycle.LiveData
import com.brid.covid_19.domain.API
import com.brid.covid_19.domain.CovidService
import com.brid.covid_19.domain.entities.Dashboard
import com.brid.covid_19.domain.entities.DashboardDaily
import com.brid.covid_19.domain.entities.DashboardDetail
import com.brid.covid_19.domain.utils.ApiResponse

class CovidUseCase {
    private val service = API.retrofit.create(CovidService::class.java)

    fun get(): LiveData<ApiResponse<Dashboard>> {
        return service.get()
    }

    fun getDaily(): LiveData<ApiResponse<Array<DashboardDaily>>> {
        return service.getDaily()
    }

    fun getDetail(url: String): LiveData<ApiResponse<Array<DashboardDetail>>> {
        return service.getDetail(url)
    }
}