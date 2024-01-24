package com.gabrielsanchez.ac603.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielsanchez.ac603.models.Articulos
import com.gabrielsanchez.ac603.room.ArticulosDatabaseDao
import com.gabrielsanchez.ac603.states.ArticulosState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArticulosViewModel(
    private val dao: ArticulosDatabaseDao
): ViewModel() {
    var state by mutableStateOf(ArticulosState())
        private set

    init {
        viewModelScope.launch {
            dao.getAllArticulos().collectLatest {
                state = state.copy(
                    articulosList = it
                )
            }
        }
    }

     fun insertArticulo(articulo: Articulos) = viewModelScope.launch {
        dao.insertArticulo(articulo)
    }

    fun updateArticulo(articulo: Articulos) = viewModelScope.launch {
        dao.updateArticulo(articulo)
    }

    suspend fun getArticuloByCodigo(codigo: Int): Articulos? {
        return viewModelScope.async {
            dao.getArticuloByCodigo(codigo)
        }.await()
    }

    suspend fun getArticulosByPrecio(precioMin: Float, precioMax: Float) {
        val articulos = viewModelScope.async {
            dao.getArticulosByPrecio(precioMin, precioMax)
        }.await()
        state = state.copy(articulosList = articulos)
    }

    fun deleteArticuloByCodigo(codigo: Int) = viewModelScope.launch {
        dao.deleteArticuloByCodigo(codigo)
    }


}