package com.example.sanmarcosstore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.FilterChip
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.sanmarcosstore.model.productosDePrueba
import com.example.sanmarcosstore.ui.components.ProductoItem
import androidx.navigation.NavController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.example.sanmarcosstore.data.FavoriteDataStore
@Composable
fun TiendaScreen(
    navController: NavController
) {
    var busqueda by remember { mutableStateOf("") }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember {
        mutableStateOf("Todos")
    }
    val context = LocalContext.current

    val favoriteDataStore =
        remember {
            FavoriteDataStore(context)
        }

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {

        val favoritosGuardados =
            favoriteDataStore.obtenerFavoritos()

        productosDePrueba.forEach { producto ->

            producto.favorito =
                favoritosGuardados.contains(
                    producto.id.toString()
                )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Campo de búsqueda ──
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar producto") },
                placeholder = { Text("Ej: Café, Chocolate…") },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = null)
                },
                supportingText = {
                    Text("Escribe para filtrar la lista")
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),

                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                listOf("Todos", "Bebidas", "Dulces", "Panadería", "Endulzantes", "Conservas", "Aceites")
                    .forEach { categoria ->

                        FilterChip(
                            selected = categoriaSeleccionada == categoria,

                            onClick = {
                                categoriaSeleccionada = categoria
                            },

                            label = {
                                Text(categoria)
                            }
                        )
                    }
            }
            // ── Formulario "agregar producto rápido" (toggle por FAB) ──
            if (mostrarFormulario) {
                FormularioRapido()
            }

            // ── Lista filtrada ──
            val productosFiltrados = productosDePrueba.filter { producto ->

                val coincideBusqueda =
                    producto.nombre.contains(busqueda, ignoreCase = true) ||
                            producto.categoria.contains(busqueda, ignoreCase = true)

                val coincideCategoria =
                    categoriaSeleccionada == "Todos" ||
                            producto.categoria == categoriaSeleccionada

                coincideBusqueda && coincideCategoria
            }

            if (productosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No se encontraron productos",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 88.dp   // espacio para el FAB
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productosFiltrados, key = { it.id }) { producto ->

                        ProductoItem(

                            producto = producto,

                            onClick = {
                                navController.navigate(
                                    "detalle/${producto.id}"
                                )
                            },

                            onFavoritoClick = {

                                producto.favorito = !producto.favorito

                                scope.launch {

                                    val favoritosActuales =
                                        productosDePrueba
                                            .filter { it.favorito }
                                            .map { it.id.toString() }
                                            .toSet()

                                    favoriteDataStore.guardarFavoritos(
                                        favoritosActuales
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

        // ── FAB para mostrar/ocultar formulario ──
        FloatingActionButton(
            onClick = { mostrarFormulario = !mostrarFormulario },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar producto")
        }
    }
}

// ────────────────────────────────────────────────────────────
// Formulario con los 5 tipos de botón de Material 3
// ────────────────────────────────────────────────────────────
@Composable
private fun FormularioRapido() {
    var nombreProducto by remember { mutableStateOf("") }
    val esError = nombreProducto.length > 30

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Agregar producto rápido",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nombreProducto,
                onValueChange = { nombreProducto = it },
                label = { Text("Nombre del producto") },
                supportingText = {
                    if (esError) {
                        Text(
                            "Máximo 30 caracteres",
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        Text("${nombreProducto.length}/30")
                    }
                },
                trailingIcon = {
                    if (esError) {
                        Icon(
                            Icons.Filled.Error,
                            contentDescription = "Error de longitud",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = esError,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Acciones (los 5 niveles de énfasis):",
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(
                    onClick = { /* guardar */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar", style = MaterialTheme.typography.labelSmall)
                }
                FilledTonalButton(
                    onClick = { /* borrador */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Borrador", style = MaterialTheme.typography.labelSmall)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ElevatedButton(
                    onClick = { /* preview */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Vista previa", style = MaterialTheme.typography.labelSmall)
                }
                OutlinedButton(
                    onClick = { nombreProducto = "" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Limpiar", style = MaterialTheme.typography.labelSmall)
                }
            }

            TextButton(onClick = { /* cancelar */ }) {
                Text("Cancelar")
            }
        }
    }
}