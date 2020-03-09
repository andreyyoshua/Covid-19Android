package com.brid.covid_19.domain.entities

import com.google.gson.*
import java.lang.reflect.Type

data class Dashboard(
    val confirmed: DashboardData,
    val daily: String,
    val deaths: DashboardData,
    val image: String,
    val lastUpdate: String,
    val recovered: DashboardData,
    val source: String
)

class DashboardAdapter: JsonDeserializer<Dashboard> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Dashboard {
        val jsonObject = json?.asJsonObject

        val confirmedJsonObject = jsonObject?.getAsJsonObject("confirmed")
        val confirmed = DashboardData(confirmedJsonObject!!.get("detail")!!.asString, confirmedJsonObject.get("value")!!.asInt, DashboardEnum.Infected)
        val deathJsonObject = jsonObject.getAsJsonObject("deaths")
        val death = DashboardData(deathJsonObject!!.get("detail")!!.asString, deathJsonObject.get("value")!!.asInt, DashboardEnum.Death)
        val recoveredJsonObject = jsonObject.getAsJsonObject("recovered")
        val recovered = DashboardData(recoveredJsonObject.get("detail")!!.asString, recoveredJsonObject.get("value")!!.asInt, DashboardEnum.Recovered)
        val daily = jsonObject.get("daily").asString
        val image = jsonObject.get("image").asString
        val lastUpdate = jsonObject.get("lastUpdate").asString
        val source = jsonObject.get("source").asString
        return Dashboard(confirmed, daily, death, image, lastUpdate, recovered, source)
    }

}