package com.example.tp_seminariolenguajes.fragments

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tp_seminariolenguajes.R

class BusquedaActivity : AppCompatActivity(), BusquedaFragmentoInterfaz {

    // Inicializaciones de vistas
    private lateinit var toolbar: Toolbar
    private lateinit var btnAtras: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_busqueda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.titulo)

        // Atras
        btnAtras = findViewById(R.id.btnAtras)
        btnAtras.setOnClickListener {
            finish()
        }

        // Busqueda
        val busquedaFragmento = supportFragmentManager
            .findFragmentById(R.id.contenedorBusquedaFragmento) as? BusquedaFragmento
        busquedaFragmento?.listener = this

    }

    override fun mostrarResultado(ubicacion: String) {
        val resultadoFragmento = RespuestaFragmento()
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorRespuestaFragmento, resultadoFragmento)
            .addToBackStack(null)
            .commit()
    }
}