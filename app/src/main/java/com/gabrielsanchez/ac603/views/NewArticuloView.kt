package com.gabrielsanchez.ac603.views

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gabrielsanchez.ac603.models.Articulos
import com.gabrielsanchez.ac603.viewmodels.ArticulosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewArticuloView(navController: NavController, viewModel: ArticulosViewModel) {
    var codigo = remember { mutableStateOf("") }
    var descripcion = remember { mutableStateOf("") }
    var precio = remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Nuevo Artículo", color = Color.White, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (codigo.value.isEmpty() || descripcion.value.isEmpty() || precio.value.isEmpty()) {
                        Toast.makeText(navController.context, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }
                    val articulo = Articulos(codigo = codigo.value.toInt(), descripcion = descripcion.value, precio = precio.value.toFloat())

                    viewModel.insertArticulo(articulo)
                    navController.popBackStack()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = CircleShape,
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Agregar")
            }
        }
    ) {
        ContentNewArticuloView(it, codigo, descripcion, precio)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentNewArticuloView(
    it: PaddingValues,
    codigo: MutableState<String>,
    descripcion: MutableState<String>,
    precio: MutableState<String>
) {

    Column(
        modifier = Modifier
            .padding(it)
            .padding(top = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = codigo.value,
            onValueChange = { codigo.value = it },
            label = { Text(text = "Código") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
        )

        OutlinedTextField(
            value = descripcion.value,
            onValueChange = { descripcion.value = it },
            label = { Text(text = "Descripcion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
        )
        OutlinedTextField(
            value = precio.value,
            onValueChange = { precio.value = it },
            label = { Text(text = "Precio") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
        )
    }
}