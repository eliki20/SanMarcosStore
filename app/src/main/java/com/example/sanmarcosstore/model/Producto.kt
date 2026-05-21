package com.example.sanmarcosstore.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
class Producto(
    val id: Int,
    val nombre: String,
    val precio: String,
    val categoria: String,
    favoritoInicial: Boolean = false
) {

    var favorito by mutableStateOf(favoritoInicial)

}

val productosDePrueba = mutableStateListOf(

    Producto(1, "Café Premium 500g", "S/ 45.00", "Bebidas"),
    Producto(2, "Chocolate artesanal 70%", "S/ 28.00", "Dulces"),
    Producto(3, "Pan de masa madre", "S/ 18.00", "Panadería"),
    Producto(4, "Miel de abeja orgánica", "S/ 35.00", "Endulzantes"),
    Producto(5, "Mermelada de fresa", "S/ 22.00", "Conservas"),
    Producto(6, "Té verde matcha", "S/ 55.00", "Bebidas"),
    Producto(7, "Galletas de avena", "S/ 15.00", "Dulces"),
    Producto(8, "Aceite de oliva extra virgen", "S/ 68.00", "Aceites")

)