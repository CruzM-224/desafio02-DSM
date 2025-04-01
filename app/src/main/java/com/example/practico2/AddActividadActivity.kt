package com.example.practico2

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practico2.datos.Actividad
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddActividadActivity : AppCompatActivity() {

    private lateinit var txtTitulo: EditText
    private lateinit var txtDescripcion: EditText
    private var key = ""
    private var titulo = ""
    private var descripcion = ""
    private var estado = ""
    private var fecha = ""
    private var accion = ""
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_actividad)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        inicializar()
    }

    private fun inicializar() {
        txtTitulo = findViewById<EditText>(R.id.txtTitulo)
        txtDescripcion = findViewById<EditText>(R.id.txtDescripcion)

        val datos: Bundle? = intent.extras
        if (datos != null) {
            key = datos.getString("key").toString()
            txtTitulo.setText(intent.getStringExtra("titulo").toString())
            txtDescripcion.setText(intent.getStringExtra("descripcion").toString())
            estado = datos.getString("estado").toString()
            fecha = datos.getString("fecha").toString()
            accion = datos.getString("accion").toString()
        }
    }

    fun guardar(v: View?) {
        titulo = txtTitulo.text.toString()
        descripcion = txtDescripcion.text.toString()

        database = FirebaseDatabase.getInstance().getReference("actividades")

        val actividad = Actividad(titulo, descripcion, estado, fecha)

        if (accion == "a") {
            val newKey = database.push().key
            if(titulo.isNotBlank()){
                if(newKey != null){
                    database.child(newKey).setValue(actividad).addOnSuccessListener {
                        Toast.makeText(this, "Se guardo con exito", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "No se pudo generar una clave", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "El titulo no debe estar en blanco", Toast.LENGTH_SHORT).show()
            }
        } else if (accion == "e") {
            if(key.isNotEmpty()) {
                val actividadValues = actividad.toMap()
                val childUpdates = hashMapOf<String, Any>(
                    key to actividadValues
                )
                database.updateChildren(childUpdates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Se actualizo con exito", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "No se encontrola clave del registro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun cancelar(v: View?) {
        finish()
    }
}