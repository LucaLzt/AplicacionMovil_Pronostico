package com.example.tp_seminariolenguajes.endpoints

import com.example.tp_seminariolenguajes.dtos.ForecastWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherEndpoints {

    // Llamado al forecast de la API
    @GET("forecast.json")
    fun getForecastWeather(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: Int,
        @Query("aqi") aqi: String,
        @Query("alerts") alerts: String,
        @Query("lang") lang: String
    ): Call<ForecastWeather>

    // Llamado al historial de la API
    @GET("history.json")
    fun getHistoryWeather(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("dt") dt: String,
        @Query("lang") lang: String
    ): Call<ForecastWeather>


}