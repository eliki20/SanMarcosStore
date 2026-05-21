package com.example.sanmarcosstore.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(
    name = "favoritos"
)

class FavoriteDataStore(
    private val context: Context
) {

    companion object {
        val FAVORITOS_KEY =
            stringSetPreferencesKey("favoritos_ids")
    }

    suspend fun guardarFavoritos(
        favoritos: Set<String>
    ) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITOS_KEY] = favoritos
        }
    }

    suspend fun obtenerFavoritos(): Set<String> {

        val preferences =
            context.dataStore.data.first()

        return preferences[FAVORITOS_KEY]
            ?: emptySet()
    }
}