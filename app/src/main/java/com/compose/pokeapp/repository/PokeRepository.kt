package com.compose.pokeapp.repository

import com.compose.pokeapp.data.Resource
import com.compose.pokeapp.model.PokemonDetails
import com.compose.pokeapp.network.PokeApi
import javax.inject.Inject

class PokeRepository @Inject constructor(private val api: PokeApi) {
    suspend fun getPokemons(): Resource<List<com.compose.pokeapp.model.Result>> {

        return try {
            Resource.Loading(data = true)

            val itemList = api.getAllPokemon().results
            if (itemList.isNotEmpty()) Resource.Loading(data = false)

            Resource.Success(data = itemList)

        }catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }

    }

    suspend fun getPokemonDetails(pokemonId: String): Resource<PokemonDetails> {
        val response = try {
            Resource.Loading(data = true)
            api.getBookInfo(pokemonId)

        }catch (exception: Exception){
            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }
}