package com.example.tp_seminariolenguajes.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_seminariolenguajes.R
import com.example.tp_seminariolenguajes.adapters.WeatherAdapter
import com.example.tp_seminariolenguajes.configurations.RetrofitClient
import com.example.tp_seminariolenguajes.dtos.ForecastWeather
import com.example.tp_seminariolenguajes.endpoints.WeatherEndpoints
import com.example.tp_seminariolenguajes.fragments.BusquedaActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // Inicializaciones de vistas
    private lateinit var toolbar: Toolbar
    private lateinit var btnHistorial: Button
    private lateinit var btnBusqueda: Button
    private lateinit var weatherRecyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var tvUbicacionCurrent: TextView
    private lateinit var tvGradosCurrent: TextView
    private lateinit var tvCondicionCurrent: TextView
    private lateinit var tvMaxMin: TextView
    private lateinit var tvSensacionTermicaCurrent: TextView
    private lateinit var tvHumedadCurrent: TextView
    private lateinit var ivImagenClima: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.titulo)

        btnHistorial = findViewById(R.id.btnHistorial)

        btnHistorial.setOnClickListener {
            val intent = Intent(this, HistorialClimaActivity::class.java)
            startActivity(intent)
        }

        btnBusqueda = findViewById(R.id.btnBusqueda)

        // Boton de busqueda
        btnBusqueda.setOnClickListener {
            val intent = Intent(this, BusquedaActivity::class.java)
            startActivity(intent)
        }

        // Llamada a la API desde una corrutina
        CoroutineScope(Dispatchers.IO).launch {
            val api = RetrofitClient.retrofit.create(WeatherEndpoints::class.java)
            val response: Response<ForecastWeather> = api.getForecastWeather("86747557a02549e6bd900345241810", "Argentina", 1, "no", "no", "es").execute()

            val forecastWeather = response.body()
            if (forecastWeather != null && response.isSuccessful) {
                // Procesar la respuesta
                withContext(Dispatchers.Main) {
                    // Inicializar el RecyclerView
                    weatherRecyclerView = findViewById(R.id.rvWeather)
                    val listaHoras = forecastWeather.forecast.forecastday[0].hour
                    weatherAdapter = WeatherAdapter(listaHoras)
                    weatherRecyclerView.adapter = weatherAdapter
                    weatherRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

                    // Actualizar las vistas con los datos de la API
                    tvUbicacionCurrent = findViewById(R.id.tvUbicacionCurrent)
                    tvGradosCurrent = findViewById(R.id.tvGradosCurrent)
                    tvCondicionCurrent = findViewById(R.id.tvCondicionCurrent)
                    tvMaxMin = findViewById(R.id.tvMaxMin)
                    tvSensacionTermicaCurrent = findViewById(R.id.tvSensacionTermicaCurrent)
                    tvHumedadCurrent = findViewById(R.id.tvHumedadCurrent)
                    ivImagenClima = findViewById(R.id.ivImagenClima)

                    // Actualizo los datos de las vistas
                    tvUbicacionCurrent.text = "Ahora en ${forecastWeather.location.country}, ${forecastWeather.location.name}"
                    tvGradosCurrent.text = "${forecastWeather.current?.temp_c?.toInt()}°C"
                    tvCondicionCurrent.text = forecastWeather.current?.condition?.text
                    tvMaxMin.text = "Max: ${forecastWeather.forecast.forecastday[0].day.maxtemp_c.toInt()}°C Min: ${forecastWeather.forecast.forecastday[0].day.mintemp_c.toInt()}°C"
                    tvSensacionTermicaCurrent.text = "Sen. térmica: ${forecastWeather.current?.feelslike_c} °C"
                    tvHumedadCurrent.text = "Humedad: ${forecastWeather.current?.humidity}%"
                    ivImagenClima.setImageResource(getIconoClima(forecastWeather.current?.condition!!.code))

                    Log.d("API", forecastWeather.toString())
                }
            } else {
                Log.e("API", "Error: ${response.code()} ${response.message()}")
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.itemModoDia) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else if (item.itemId == R.id.itemModoNoche) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        recreate()
        return super.onOptionsItemSelected(item)
    }

}

fun getIconoClima(codigo: Int) : Int {
    return when (codigo) {
        // Llovizna Moderada Intensa
        1171, 1186, 1189, 1192, 1195, 1243, 1246 -> R.drawable.llovizna_moderada_intensa

        // Lluvia Granizo
        1069, 1072, 1114, 1117, 1198, 1201, 1204, 1207, 1249, 1252, 1255,
        1264, 1273, 1276, 1279, 1282  -> R.drawable.lluvia_granizo

        // Nevando
        1066, 1092, 1095, 1210, 1213, 1216, 1219, 1222, 1225, 1237  -> R.drawable.nevando

        // Nublado
        1006, 1009, 1030, 1135, 1147 -> R.drawable.nublado

        // Sol Despejado
        1000 -> R.drawable.sol_despejado

        // Sol Llovizna Ligera
        1063, 1150, 1153, 1168, 1180, 1183, 1240 -> R.drawable.sol_llovizna_ligera

        // Sol Parcialmente Nublado
        1003 -> R.drawable.sol_parcialmente_nublado

        // Tormenta Eléctrica
        1087 -> R.drawable.tormenta_electrica

        else -> R.drawable.sol_despejado // Icono por defecto
    }
}