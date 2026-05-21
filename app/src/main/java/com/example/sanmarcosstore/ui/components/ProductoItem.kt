package com.example.sanmarcosstore.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sanmarcosstore.model.Producto

@Composable
fun ProductoItem(
    producto: Producto,
    onClick: () -> Unit,
    onFavoritoClick: () -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth().clickable {
            onClick()
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
            },
            supportingContent = {
                Text(producto.categoria, style = MaterialTheme.typography.bodySmall)
            },
            trailingContent = {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        producto.precio,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector =
                            if (producto.favorito)
                                Icons.Filled.Favorite
                            else
                                Icons.Filled.FavoriteBorder,

                        contentDescription = "Favorito",

                        tint =
                            if (producto.favorito)
                                MaterialTheme.colorScheme.tertiary
                            else
                                MaterialTheme.colorScheme.outline,

                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                onFavoritoClick()
                            }
                    )
                }
            },
            leadingContent = {
                Icon(
                    Icons.Filled.ShoppingCart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        )
    }
}