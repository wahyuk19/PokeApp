package com.compose.pokeapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PokemonEntity::class], version = 1, exportSchema = true)
abstract class PokemonRoomDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}