package com.gabrielsanchez.ac603.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articulos")
data class Articulos(
    @PrimaryKey(autoGenerate = false)
    val codigo: Int = 0,
    @ColumnInfo("descripcion")
    val descripcion: String,
    @ColumnInfo("precio")
    val precio: Float
)