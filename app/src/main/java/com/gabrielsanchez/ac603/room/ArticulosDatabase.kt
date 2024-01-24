package com.gabrielsanchez.ac603.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gabrielsanchez.ac603.models.Articulos

@Database(
    entities = [Articulos::class],
    version = 1,
    exportSchema = false
)
abstract class ArticulosDatabase: RoomDatabase() {
    abstract fun articulosDao(): ArticulosDatabaseDao
}