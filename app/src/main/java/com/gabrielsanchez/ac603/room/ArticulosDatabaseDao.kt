package com.gabrielsanchez.ac603.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gabrielsanchez.ac603.models.Articulos
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticulosDatabaseDao {

    @Query("SELECT * FROM articulos")
    fun getAllArticulos(): Flow<List<Articulos>>

    @Query("SELECT * FROM articulos WHERE codigo = :codigo")
    fun getArticulobyCodigo(codigo: Int): Flow<Articulos>

    @Insert
    suspend fun insertArticulo(articulo: Articulos)

    @Update
    suspend fun updateArticulo(articulo: Articulos)

    @Query("SELECT * FROM articulos WHERE codigo = :codigo")
    suspend fun getArticuloByCodigo(codigo: Int): Articulos

    @Query("SELECT * FROM articulos WHERE precio BETWEEN :precioMin AND :precioMax")
    suspend fun getArticulosByPrecio(precioMin: Float, precioMax: Float): List<Articulos>

    @Query("DELETE FROM articulos WHERE codigo = :codigo")
    suspend fun deleteArticuloByCodigo(codigo: Int)

    @Delete
    suspend fun deleteArticulo(articulo: Articulos)
}