package com.gabrielsanchez.ac603.states

import com.gabrielsanchez.ac603.models.Articulos

data class ArticulosState(
    val articulosList: List<Articulos> = emptyList()
)