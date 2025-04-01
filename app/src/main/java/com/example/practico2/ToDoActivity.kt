package com.example.practico2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.example.practico2.datos.Actividad
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ToDoActivity : AppCompatActivity() {

    var consultaOrdenada: Query = refActividades.orderByChild("fecha")

    private lateinit var actividades: MutableList<Actividad>
    private lateinit var listaActividades: ListView
    private lateinit var fab_agregar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_to_do)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializar()
    }

    private fun inicializar() {
        actividades = ArrayList<Actividad>()
        fab_agregar = findViewById(R.id.fab_agregar)
        listaActividades = findViewById(R.id.ListaActividades)

        fab_agregar.setOnClickListener {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val intent = Intent(baseContext, AddActividadActivity::class.java)
            intent.putExtra("accion", "a")
            intent.putExtra("key", "")
            intent.putExtra("titulo", "")
            intent.putExtra("descripcion", "")
            intent.putExtra("estado", "pendiente")
            intent.putExtra("fecha", LocalDateTime.now().format(formatter))
            startActivity(intent)
        }

        actividades = ArrayList<Actividad>()

        consultaOrdenada.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                actividades!!.clear()
                for (dato in snapshot.children) {
                    val actividad: Actividad? = dato.getValue(Actividad::class.java)
                    actividad?.key = dato.key
                    if(actividad != null) {
                        actividades!!.add(actividad)
                    }
                }
                val adapter = AdaptadorActividad(
                    this@ToDoActivity,
                    actividades as ArrayList<Actividad>
                )
                listaActividades!!.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ToDoActivity,
                    "Ha ocurrido un error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refActividades: DatabaseReference = database.getReference("actividades")
    }
}