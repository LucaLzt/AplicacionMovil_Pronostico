package com.example.tp_seminariolenguajes.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tp_seminariolenguajes.R
import com.example.tp_seminariolenguajes.activities.getIconoClima

class RespuestaFragmento : Fragment() {

    private lateinit var tvUbicacion: TextView
    private lateinit var ivImagenClima: ImageView
    private lateinit var tvGrados: TextView
    private lateinit var tvCondicion: TextView
    private lateinit var tvMaxMin: TextView
    private lateinit var tvSensacionTermica: TextView
    private lateinit var tvHumedad: TextView
    private lateinit var tvVientoCurrent: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.respuesta_fragmento, container, false)

        tvUbicacion = view.findViewById(R.id.tvUbicacion)
        ivImagenClima = view.findViewById(R.id.ivImagenClima)
        tvGrados = view.findViewById(R.id.tvGrados)
        tvCondicion = view.findViewById(R.id.tvCondicion)
        tvMaxMin = view.findViewById(R.id.tvMaxMin)
        tvSensacionTermica = view.findViewById(R.id.tvSensacionTermica)
        tvHumedad = view.findViewById(R.id.tvHumedad)
        tvVientoCurrent = view.findViewById(R.id.tvVientoCurrent)

        val bundle = arguments
        if(bundle != null) {
            Log.d("Arguments", bundle.toString())
            tvUbicacion.text = "${bundle.getString("ubicacion")}"
            ivImagenClima.setImageResource(getIconoClima(bundle.getInt("imagen")))
            tvGrados.text = bundle.getString("temperatura")
            tvCondicion.text = bundle.getString("condicion")
            tvMaxMin.text = bundle.getString("maxMin")
            tvSensacionTermica.text = bundle.getString("sensacionTermica")
            tvHumedad.text = bundle.getString("humedad")
            tvVientoCurrent.text = bundle.getString("viento")
        } else {
            Log.e("Arguments", "No se encontraron argumentos")
        }

        return view

    }

}