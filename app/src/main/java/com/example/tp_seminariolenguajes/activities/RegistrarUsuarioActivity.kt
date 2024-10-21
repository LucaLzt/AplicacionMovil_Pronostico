package com.example.tp_seminariolenguajes.activities

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tp_seminariolenguajes.R
import com.example.tp_seminariolenguajes.database.AppDatabase
import com.example.tp_seminariolenguajes.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrarUsuarioActivity : AppCompatActivity() {

    // Inicializaciones de vistas
    private lateinit var btnAtras: ImageButton
    private lateinit var btnModo: ImageButton
    private lateinit var btnConfirmarRegistro: Button
    private lateinit var etUsuario: EditText
    private lateinit var etContrasena: EditText
    private lateinit var etConfirmarContrasena: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Enlazar vistas
        btnAtras = findViewById(R.id.btnAtras)
        btnModo = findViewById(R.id.btnModo)
        btnConfirmarRegistro = findViewById(R.id.btn_confirmar_registro)
        etUsuario = findViewById(R.id.etUsuario)
        etContrasena = findViewById(R.id.etContraseña)
        etConfirmarContrasena = findViewById(R.id.etConfirmarContraseña)

        // Verificar modo de noche
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            btnModo.setImageResource(R.drawable.ic_mode_night)
        } else {
            btnModo.setImageResource(R.drawable.ic_mode)
        }

        // Moverse a la pantalla principal
        btnAtras.setOnClickListener {
            finish()
        }

        // Registrar usuario
        btnConfirmarRegistro.setOnClickListener {
            if(etUsuario.text.toString().isEmpty() || etContrasena.text.toString().isEmpty() || etConfirmarContrasena.text.toString().isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            } else if(etContrasena.text.toString() != etConfirmarContrasena.text.toString()) {
                Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(this, etUsuario.text.toString(), etContrasena.text.toString())
                Toast.makeText(this, "Registro Completo!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Cambiar el modo de noche o dia
        btnModo.setOnClickListener {
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            recreate()
        }

    }
}

// Función para guardar usuario en la bd ("Registrar Usuario")
fun registerUser(context: Context, username: String, password: String) {
    val user = User(username = username, password = password)
    val db = AppDatabase.getDatabase(context)

    CoroutineScope(Dispatchers.IO).launch {
        db.userDao().insertUser(user)
    }
}