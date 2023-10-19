package com.compose.pokeapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PokemonEntity::class], version = 1, exportSchema = true)
abstract class PokemonRoomDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}