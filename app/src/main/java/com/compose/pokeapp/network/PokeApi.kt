package com.compose.pokeapp.network

import com.compose.pokeapp.model.Pokemon
import com.compose.pokeapp.model.PokemonDetails
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface PokeApi {

    @GET("pokemon")
    suspend fun getAllPokemon(): Pokemon

    @GET("pokemon/{pokemonId}")
    suspend fun getBookInfo(@Path("pokemonId") pokemonId: String): PokemonDetails
}