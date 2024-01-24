package com.gabrielsanchez.ac603.views

import android.annotation.SuppressLint
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.gabrielsanchez.ac603.models.Articulos
import com.gabrielsanchez.ac603.navigation.AppScreens
import com.gabrielsanchez.ac603.viewmodels.ArticulosViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditArticuloView(navController: NavController, viewModel: ArticulosViewModel, codigo: Int) {

    var descripcion = remember { mutableStateOf<String?>(null) }
    var precio = remember { mutableStateOf<Float?>(null) }
    viewModel.viewModelScope.launch {
        val articulo = viewModel.getArticuloByCodigo(codigo)
        if (articulo != null) {
            descripcion.value = articulo.descripcion
            precio.value = articulo.precio
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Editar Artículo #${codigo}", color = Color.White, fontWeight = FontWeight.Bold)
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
                    if (descripcion.value.toString().isEmpty() || precio.value.toString().isEmpty()) {
                        Toast.makeText(navController.context, "Tienes que llenar todos los campos", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }
                    val articulo = Articulos(codigo = codigo, descripcion = descripcion.value!!, precio = precio.value!!)

                    viewModel.updateArticulo(articulo)
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
        ContentEditArticuloView(it, descripcion, precio)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentEditArticuloView(
    it: PaddingValues,
    descripcion: MutableState<String?>,
    precio: MutableState<Float?>
) {

    Column(
        modifier = Modifier
            .padding(it)
            .padding(top = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = descripcion.value ?: "",
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
            value = precio.value?.toString() ?: "",
            onValueChange = { if (it.isNotEmpty()) precio.value = it.toFloat() },
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
