package com.example.tp_seminariolenguajes.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
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
import com.example.tp_seminariolenguajes.dtos.Hour
import com.example.tp_seminariolenguajes.endpoints.WeatherEndpoints
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HistorialClimaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // Inicializaciones de vistas
        lateinit var toolbar: Toolbar
        lateinit var btnAtras: ImageButton
        lateinit var weatherRecyclerView: RecyclerView
        lateinit var weatherAdapter: WeatherAdapter

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial_clima)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnAtras = findViewById(R.id.btnAtras)

        // Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.titulo)

        // Botón de Atrás
        btnAtras.setOnClickListener {
            onBackPressed()
        }

        // Inicializar el RecyclerView
        weatherRecyclerView = findViewById(R.id.rvWeather)
        weatherRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val listaHorasTotal = mutableListOf<Hour>()
        weatherAdapter = WeatherAdapter(listaHorasTotal)
        weatherRecyclerView.adapter = weatherAdapter


        // Obtener las fechas anteriores
        val fechas = mutableListOf<String>()
        for (i in 1..7) {
            fechas.add(getFechaAnterior(i))
        }

        // Llamada a la API desde una corrutina
        CoroutineScope(Dispatchers.IO).launch {

            for (i in 0..6) {
                val response: Response<ForecastWeather> = RetrofitClient.retrofit.create(WeatherEndpoints::class.java)
                    .getHistoryWeather("86747557a02549e6bd900345241810", "Argentina", fechas[i], "es")
                    .awaitResponse()

                val forecastWeather = response.body()
                if (forecastWeather != null && response.isSuccessful) {
                    val listaHoras = forecastWeather.forecast.forecastday[0].hour
                    listaHorasTotal.addAll(listaHoras)
                } else {
                    Log.e("API", "Error: ${response.code()} ${response.message()}")
                }
            }

            // Ordenar la lista de horas por fecha
            withContext(Dispatchers.Main) {
                listaHorasTotal.sortWith { h1, h2 ->
                    val fecha1 = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(h1.time)
                    val fecha2 = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(h2.time)
                    fecha2?.compareTo(fecha1) ?: Int.MIN_VALUE
                }
                // Notificar al adaptador que los datos han cambiado
                weatherAdapter.notifyDataSetChanged()
            }
        }

    }

    // Crear el menú
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Cambiar el modo de la app
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


// Función para obtener las fechas anteriores
fun getFechaAnterior(dias: Int): String {
    val calendario = Calendar.getInstance()
    calendario.time = Date()
    calendario.add(Calendar.DAY_OF_YEAR, -dias)
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendario.time ?: Date())
}