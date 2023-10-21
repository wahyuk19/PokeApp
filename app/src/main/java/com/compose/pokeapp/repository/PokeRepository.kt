package com.compose.pokeapp.repository

import com.compose.pokeapp.data.Resource
import com.compose.pokeapp.db.PokemonEntity
import com.compose.pokeapp.db.PokemonDao
import com.compose.pokeapp.model.PokemonDetails
import com.compose.pokeapp.network.PokeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PokeRepository @Inject constructor(
    private val pokemonDao: PokemonDao,
    private val api: PokeApi
) {

    suspend fun getPokemonList(): Flow<List<PokemonEntity>> {
        try {
            val client = api.getAllPokemon(limit = 1292, offset = 0)
            client.results.forEach {
                val pokemonData = PokemonEntity(
                    id = it.url.substringAfter("pokemon/", "").dropLast(1).toInt(),
                    name = it.name,
                    url = it.url
                )
                pokemonDao.insert(pokemonData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pokemonDao.getAllPokemonAsc().flowOn(Dispatchers.IO).conflate()
    }

    fun getPokemonsAsc(): Flow<List<PokemonEntity>> {
        return pokemonDao.getAllPokemonAsc().flowOn(Dispatchers.IO).conflate()
    }

    fun getPokemonDesc(): Flow<List<PokemonEntity>> {
        return pokemonDao.getAllPokemonDesc().flowOn(Dispatchers.IO).conflate()
    }

    fun getPokemonByName(pokemonName: String): Flow<List<PokemonEntity>> {
        return pokemonDao.getPokemonByName(pokemonName).flowOn(Dispatchers.IO).conflate()
    }

    suspend fun getPokemonDetails(pokemonId: String): Resource<PokemonDetails> {
        val response = try {
            Resource.Loading(data = true)
            api.getBookInfo(pokemonId)

        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }


}