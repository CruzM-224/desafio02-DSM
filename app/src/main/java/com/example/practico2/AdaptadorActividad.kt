package com.example.practico2

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.TextView
import com.example.practico2.datos.Actividad
import android.content.Intent
import android.widget.Toast
import com.example.practico2.ToDoActivity.Companion.refActividades
import com.google.firebase.database.FirebaseDatabase


class AdaptadorActividad(private val context: Activity, var actividades: List<Actividad>) : ArrayAdapter<Actividad?>(context, R.layout.activity_to_do, actividades) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        val layoutInflater = context.layoutInflater
        val rowview = view ?: layoutInflater.inflate(R.layout.actividad_layout, parent, false)

        val tvTitulo = rowview!!.findViewById<TextView>(R.id.tvTitulo)
        val tvDescripcion = rowview!!.findViewById<TextView>(R.id.tvDescripcion)
        val switchEstado = rowview!!.findViewById<Switch>(R.id.switchEstado)

        val actividad = actividades[position]

        tvTitulo.text = "Titulo: " + actividades[position].titulo
        tvDescripcion.text = "Descripcion: " + actividades[position].descripcion
        switchEstado.isChecked = actividades[position].estado == "completada"

        switchEstado.setOnClickListener { view ->
            val isChecked = (view as Switch).isChecked
            val nuevoEstado = if (isChecked) "completada" else "pendiente"

            // Actualizar el estado localmente
            actividad.estado = nuevoEstado

            // Guardar cambio en Firebase
            actualizarEstadoEnFirebase(actividad)

            // Evitar que el clic se propague
            view.isClickable = true
        }

        rowview.setOnClickListener {
            val currentPosition = actividades.indexOf(actividad)
            if (currentPosition >= 0) {
                val intent = Intent(context, AddActividadActivity::class.java)
                intent.putExtra("accion", "e")
                intent.putExtra("key", actividades[currentPosition].key)
                intent.putExtra("titulo", actividades[currentPosition].titulo)
                intent.putExtra("descripcion", actividades[currentPosition].descripcion)
                intent.putExtra("estado", actividades[currentPosition].estado)
                intent.putExtra("fecha", actividades[currentPosition].fecha)
                context.startActivity(intent)
            }
        }

        rowview.setOnLongClickListener {
            val currentPosition = actividades.indexOf(actividad)
            if (currentPosition >= 0) {
                val ad = AlertDialog.Builder(context)
                    .setMessage("Esta seguro de eliminar la actividad?")
                    .setTitle("Confirmacion")
                    .setPositiveButton("Si") {
                            dialog, id ->
                        actividades!![position].key?.let {
                            refActividades.child(it).removeValue()
                        }
                        Toast.makeText(
                            context,
                            "Actividad eliminada!", Toast.LENGTH_SHORT
                        ).show()
                    }
                ad.setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Toast.makeText(
                            context,
                            "Operacion de borrado cancelada!", Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                    .show()
                true
            }else{
                false
            }
        }

        return rowview
    }

    private fun actualizarEstadoEnFirebase(actividad: Actividad) {
        val database = FirebaseDatabase.getInstance().getReference("actividades")

        // Opción 1: Actualizar solo el campo estado (más eficiente)
        if (!actividad.key.isNullOrEmpty()) {
            database.child(actividad.key!!).child("estado").setValue(actividad.estado)
                .addOnSuccessListener {
                    Toast.makeText(context, "Estado actualizado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al actualizar estado", Toast.LENGTH_SHORT).show()
                }
        }
    }
}