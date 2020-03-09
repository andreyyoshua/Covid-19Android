package com.brid.covid_19.domain

import androidx.lifecycle.MediatorLiveData
import com.brid.covid_19.domain.entities.Dashboard
import com.brid.covid_19.domain.entities.DashboardAdapter
import com.brid.covid_19.domain.utils.LiveDataCallAdapterFactory
import com.google.gson.GsonBuilder
import com.musiq.id.domain.constants.Constants
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class API {

    companion object {
        val logoutObservable = MediatorLiveData<String>()

        val retrofit: Retrofit
            get() {

                val client = client()

                return retrofitBuilder()
                    .client(client)
                    .build()
            }

        private fun retrofitBuilder(): Retrofit.Builder {
            return Retrofit.Builder()
                .addConverterFactory(
                    GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").registerTypeAdapter(Dashboard::class.java, DashboardAdapter()).create())
                )
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .baseUrl("${Constants.API_URL.value}")
        }

        private fun client(): OkHttpClient {

            val clientBuilder = OkHttpClient.Builder()

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(interceptor)

            val headerInterceptor = HttpLoggingInterceptor()
            headerInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            clientBuilder.addInterceptor(headerInterceptor)

            clientBuilder.addInterceptor {

                val chain = it
                val request = chain.request()
                val response: Response = chain.proceed(request)

                if (response.code() == 401) {
                    logoutObservable.postValue("Your session has expired. Please Re-Login")
                }

                response
            }

            return clientBuilder.build()
        }

    }
}