package com.example.practico2.datos

class Actividad {
    fun key(key: String?) {

    }

    var titulo: String? = null
    var descripcion: String? = null
    var estado: String? = null
    var fecha: String? = null
    var key: String? = null
    var per: MutableMap<String, Boolean> = HashMap()

    constructor() {}

    constructor(titulo: String?, descripcion: String?, estado: String?, fecha: String?) {
        this.titulo = titulo
        this.descripcion = descripcion
        this.estado = estado
        this.fecha = fecha
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "estado" to estado,
            "fecha" to fecha,
            "key" to key,
            "per" to per
        )
    }
}