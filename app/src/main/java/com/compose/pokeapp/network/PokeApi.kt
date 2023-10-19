package com.compose.pokeapp.network

import com.compose.pokeapp.model.Pokemon
import com.compose.pokeapp.model.PokemonDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface PokeApi {

    @GET("pokemon")
    suspend fun getAllPokemon(
        @Query("limit") limit:Int = 20,
        @Query("offset") offset:Int?
    ): Pokemon

    @GET("pokemon/{pokemonId}")
    suspend fun getBookInfo(@Path("pokemonId") pokemonId: String): PokemonDetails
}