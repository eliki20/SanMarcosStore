package com.example.sanmarcosstore.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sanmarcosstore.ui.screens.PerfilScreen
import com.example.sanmarcosstore.ui.screens.TiendaScreen
import com.example.sanmarcosstore.ui.screens.CarritoScreen
import com.example.sanmarcosstore.ui.screens.DetalleScreen

// Cada pantalla se identifica con una "ruta" única
sealed class Ruta(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
) {
    data object Tienda : Ruta("tienda", "Tienda", Icons.Filled.Store)
    data object Detalle : Ruta("detalle/{productoId}", "Detalle", Icons.Filled.Info)
    data object Perfil : Ruta("perfil", "Mi Perfil", Icons.Filled.Person)
    data object Carrito : Ruta( "carrito", "Carrito", Icons.Filled.ShoppingCart)
}

private val pestañas = listOf(Ruta.Tienda, Ruta.Carrito, Ruta.Perfil )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    darkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    // Título dinámico según la pantalla activa
    val tituloActual = when (currentDestination?.route) {
        Ruta.Tienda.ruta -> "SanMarcos Store"
        Ruta.Perfil.ruta -> "Mi Perfil"
        else -> "SanMarcos Store"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tituloActual) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ) {
                pestañas.forEach { pestaña ->
                    val seleccionada = currentDestination
                        ?.hierarchy?.any { it.route == pestaña.ruta } == true

                    NavigationBarItem(
                        selected = seleccionada,
                        onClick = {
                            navController.navigate(pestaña.ruta) {
                                // Evita acumular instancias en el back stack
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(pestaña.icono, contentDescription = pestaña.etiqueta)
                        },
                        label = { Text(pestaña.etiqueta) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Ruta.Tienda.ruta,
            modifier = Modifier.padding(padding)
        ) {
            composable(Ruta.Tienda.ruta) { TiendaScreen(navController) }
            composable("detalle/{productoId}") { backStackEntry ->

                val productoId =
                    backStackEntry.arguments
                        ?.getString("productoId")

                DetalleScreen(productoId)
            }
            composable(Ruta.Carrito.ruta) { CarritoScreen() }
            composable(Ruta.Perfil.ruta) {

                PerfilScreen(
                    darkTheme = darkTheme,
                    onThemeChange = onThemeChange
                )
            }
        }
    }
}