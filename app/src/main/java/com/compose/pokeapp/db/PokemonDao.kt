package com.compose.pokeapp.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: PokemonEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(pokemon: PokemonEntity)

    @Delete
    suspend fun delete(pokemon: PokemonEntity)

    @Query("SELECT * from pokemon_list ORDER BY id ASC")
    fun getAllPokemonAsc(): Flow<List<PokemonEntity>>

    @Query("SELECT * from pokemon_list ORDER BY id DESC")
    fun getAllPokemonDesc(): Flow<List<PokemonEntity>>

    @Query("SELECT * from pokemon_list WHERE name LIKE '%' || :pokeName || '%'")
    fun getPokemonByName(pokeName: String): Flow<List<PokemonEntity>>
}