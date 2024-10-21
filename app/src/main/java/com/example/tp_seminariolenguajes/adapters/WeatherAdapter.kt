package com.example.tp_seminariolenguajes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_seminariolenguajes.R
import com.example.tp_seminariolenguajes.activities.getIconoClima
import com.example.tp_seminariolenguajes.dtos.Hour

class WeatherAdapter(
    private val weatherList: List<Hour>):
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val ivProximaImagenClima: ImageView = view.findViewById(R.id.ivProximaImagenClima)
        val tvProximaHora: TextView = view.findViewById(R.id.tvProximaHora)
        val tvProximaTemperatura: TextView = view.findViewById(R.id.tvProximaTemperatura)
        val tvProximaCondicion: TextView = view.findViewById(R.id.tvProximaCondicion)
        val tvProximaProbLluviaHumedad: TextView = view.findViewById(R.id.tvProximaProbLluviaHumedad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = weatherList[position]
        holder.tvProximaHora.text = weather.time
        holder.tvProximaTemperatura.text = "${weather.temp_c}ºC"
        holder.tvProximaCondicion.text = weather.condition.text
        holder.tvProximaProbLluviaHumedad.text = "Prec: ${weather.chance_of_rain}% - Hume: ${weather.humidity}%"
        holder.ivProximaImagenClima.setImageResource(getIconoClima(weather.condition.code))
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

}