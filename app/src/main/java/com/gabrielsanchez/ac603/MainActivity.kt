package com.gabrielsanchez.ac603

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.gabrielsanchez.ac603.navigation.NavManager
import com.gabrielsanchez.ac603.room.ArticulosDatabase

import com.gabrielsanchez.ac603.ui.theme.AC603Theme
import com.gabrielsanchez.ac603.viewmodels.ArticulosViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AC603Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val database = Room.databaseBuilder(this, ArticulosDatabase::class.java, "db_articulos").build()
                    val dao = database.articulosDao()

                    val viewModel = ArticulosViewModel(dao)
                    NavManager(viewModel = viewModel)
                }
            }
        }
    }
}