package com.example.tp_seminariolenguajes.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tp_seminariolenguajes.R
import com.example.tp_seminariolenguajes.configurations.RetrofitClient
import com.example.tp_seminariolenguajes.dtos.ForecastWeather
import com.example.tp_seminariolenguajes.endpoints.WeatherEndpoints
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.awaitResponse

class BusquedaFragmento : Fragment() {
    var listener: BusquedaFragmentoInterfaz? = null
    lateinit var btnBusqueda: Button
    lateinit var etUbicacion: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.busqueda_fragmento, container, false)

        etUbicacion = view.findViewById(R.id.etBusqueda)
        btnBusqueda = view.findViewById(R.id.btnBusqueda)

        btnBusqueda.setOnClickListener {
            val ubicacion = etUbicacion.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val apiResponse = apiCall(ubicacion)

                withContext(Dispatchers.Main) {
                    if (apiResponse != null) {
                        // Enviar los datos al fragmento de resultados
                        val respuestaFragmento = RespuestaFragmento()
                        val bundle = Bundle()
                        bundle.putString("ubicacion", "Ahora en ${apiResponse.location.country}, ${apiResponse.location.name}")
                        bundle.putInt("imagen", apiResponse.current?.condition!!.code)
                        bundle.putString("temperatura", "${apiResponse.current?.temp_c?.toInt()}°C")
                        bundle.putString("condicion", apiResponse.current?.condition?.text)
                        bundle.putString("maxMin", apiResponse.forecast.forecastday[0].day.maxtemp_c.toString() + "°C / " + apiResponse.forecast.forecastday[0].day.mintemp_c.toString() + "°C")
                        bundle.putString("sensacionTermica", "Sen. térmica: ${apiResponse.current?.feelslike_c} °C")
                        bundle.putString("humedad", "Humedad: ${apiResponse.current?.humidity}%")
                        bundle.putString("viento", "Viento: ${apiResponse.current?.wind_kph}Km/h")
                        respuestaFragmento.arguments = bundle

                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.contenedorRespuestaFragmento, respuestaFragmento)
                            ?.commitNow()
                    } else {
                        // Mostrar un mensaje de error al usuario
                        Toast.makeText(context, "Ubicacion no válida", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return view
    }
}

suspend fun apiCall(ubicacion: String): ForecastWeather? {
    val api = RetrofitClient.retrofit.create(WeatherEndpoints::class.java)
    val response: Response<ForecastWeather> = api.getForecastWeather("86747557a02549e6bd900345241810", ubicacion, 1, "no", "no", "es")
        .awaitResponse()

    return if (response.isSuccessful) {
        response.body()
    } else {
        Log.e("API", "Error: ${response.code()} ${response.message()}")
        null
    }
}