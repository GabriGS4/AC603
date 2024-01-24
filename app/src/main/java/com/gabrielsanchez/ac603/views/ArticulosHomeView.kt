package com.gabrielsanchez.ac603.views


import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.gabrielsanchez.ac603.models.Articulos
import com.gabrielsanchez.ac603.navigation.AppScreens
import com.gabrielsanchez.ac603.viewmodels.ArticulosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticulosHomeView(navController: NavController, viewModel: ArticulosViewModel) {
    var isMenuOpen by rememberSaveable { mutableStateOf(false) }
    val menuItems =
        listOf("Consulta por código", "Consulta por precio", "Borrar artículo por código")
    val showDialogConsultaPorCodigo = rememberSaveable { mutableStateOf(false) }
    val showDialogConsultaPorPrecio = rememberSaveable { mutableStateOf(false) }
    val showDialogDelete = rememberSaveable { mutableStateOf(false) }

    var precioMin = remember { mutableStateOf("") }
    var precioMax = remember { mutableStateOf("") }

    if (showDialogConsultaPorCodigo.value) {
        ConsultaPorCodigo(
            onDismissRequest = { showDialogConsultaPorCodigo.value = false },
            onConfirmation = { showDialogConsultaPorCodigo.value = false },
            dialogTitle = "Consulta por código",
            viewModel = viewModel,
            navController = navController
        )
    }

    if (showDialogDelete.value) {
        DeletePorCodigo(
            onDismissRequest = { showDialogDelete.value = false },
            onConfirmation = { showDialogDelete.value = false },
            dialogTitle = "Borrar artículo por código",
            viewModel = viewModel
        )
    }

    if (showDialogConsultaPorPrecio.value) {
        ConsultaPorPrecio(
            onDismissRequest = { showDialogConsultaPorPrecio.value = false },
            onConfirmation = { showDialogConsultaPorPrecio.value = false },
            dialogTitle = "Consulta por precio",
            viewModel = viewModel,
            precioMin,
            precioMax
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Artículos", color = Color.White, fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = { isMenuOpen = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menú",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = isMenuOpen,
                        onDismissRequest = { isMenuOpen = false }
                    ) {
                        menuItems.forEachIndexed { index, s ->
                            DropdownMenuItem(text = { Text(text = s) }, onClick = {
                                isMenuOpen = false
                                when (index) {
                                    0 -> showDialogConsultaPorCodigo.value = true
                                    1 -> showDialogConsultaPorPrecio.value = true
                                    2 -> showDialogDelete.value = true
                                }
                            })
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AppScreens.NewArticulo.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) {
        ContentInicioView(it, navController, viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentInicioView(
    it: PaddingValues,
    navController: NavController,
    viewModel: ArticulosViewModel
) {
    var state = viewModel.state

    LaunchedEffect(state.articulosList) {
        // Este bloque se ejecutará cada vez que state.articulosList cambie
        state = state.copy()
    }

    Column(
        modifier = Modifier.padding(it)
    ) {
        LazyColumn {
            if (state.articulosList.isEmpty()) {
                item {
                    Text(
                        text = "No hay articulos",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(state.articulosList) {
                    Card(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        onClick = {
                            navController.navigate(AppScreens.EditArticulo.route + "/${it.codigo}")
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "#${it.codigo} - ${it.descripcion}",
                                    textAlign = TextAlign.Start,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold, // Aumenta el grosor del texto
                                    modifier = Modifier
                                        .weight(1f),
                                )

                                Text(
                                    text = "${it.precio}€",
                                    textAlign = TextAlign.End,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold, // Aumenta el grosor del texto
                                    modifier = Modifier
                                        .weight(1f),
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConsultaPorCodigo(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    viewModel: ArticulosViewModel,
    navController: NavController,
    context: Context = LocalContext.current
) {
    var codigo by remember { mutableStateOf("") }

    AlertDialog(
        icon = {
            Icon(Icons.Default.Edit, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            OutlinedTextField(
                value = codigo,
                onValueChange = { codigo = it },
                label = { Text("Código del artículo") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                )
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Lanzamos una corrutina para obtener el articulo
                    viewModel.viewModelScope.launch {
                        val articulo = viewModel.getArticuloByCodigo(codigo.toInt())
                        if (articulo != null) {
                            navController.navigate(AppScreens.EditArticulo.route + "/${articulo.codigo}")
                        } else {
                            Toast.makeText(
                                context,
                                "El artículo no existe",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    onConfirmation()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ConsultaPorPrecio(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    viewModel: ArticulosViewModel,
    precioMin: MutableState<String>, // Recibir precioMin y precioMax como argumentos
    precioMax: MutableState<String>,
    ) {

    AlertDialog(
        icon = {
            Icon(Icons.Default.Edit, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            RangeSliderPrecio(precioMin, precioMax)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Lanzamos una corrutina para obtener el articulo
                    viewModel.viewModelScope.launch {
                        viewModel.getArticulosByPrecio(precioMin.value.toFloat(), precioMax.value.toFloat())
                    }

                    onConfirmation()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RangeSliderPrecio(precioMin: MutableState<String>, precioMax: MutableState<String>) {
    Column {
        // Precio Minimo
        OutlinedTextField(
            value = precioMin.value,
            onValueChange = { precioMin.value = it },
            label = { Text("Precio mínimo") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            )
        )
        // Precio Maximo
        OutlinedTextField(
            value = precioMax.value.toString(),
            onValueChange = { precioMax.value = it },
            label = { Text("Precio máximo") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            )
        )
    }
}

@Composable
fun DeletePorCodigo(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    viewModel: ArticulosViewModel,
    context: Context = LocalContext.current
) {
    var codigo by remember { mutableStateOf("") }

    AlertDialog(
        icon = {
            Icon(Icons.Default.Delete, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            OutlinedTextField(
                value = codigo,
                onValueChange = { codigo = it },
                label = { Text("Código del artículo") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Comprobamos que el articulo existe
                    viewModel.viewModelScope.launch {
                        val articulo = viewModel.getArticuloByCodigo(codigo.toInt())
                        if (articulo == null) {
                            Toast.makeText(
                                context,
                                "El artículo no existe",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.deleteArticuloByCodigo(codigo.toInt())
                            Toast.makeText(
                                context,
                                "Artículo borrado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    onConfirmation()
                }
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}