package com.compose.pokeapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.compose.pokeapp.data.Resource
import com.compose.pokeapp.db.PokemonEntity
import com.compose.pokeapp.db.PokemonDao
import com.compose.pokeapp.db.PokemonRoomDatabase
import com.compose.pokeapp.model.PokemonDetails
import com.compose.pokeapp.network.PokeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class PokeRepository @Inject constructor(private val pokemonDao: PokemonDao,private val api: PokeApi) {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val result = MediatorLiveData<Resource<List<PokemonEntity>>>()

    suspend fun getAllPokemonAsc(): Flow<List<PokemonEntity>> {
        try{
            val client = api.getAllPokemon(limit = 20, offset = 0)
            client.results.forEach {
                    val pokemonData = PokemonEntity(id = it.url.substringAfter("pokemon/", "").dropLast(1).toInt()
                , name = it.name, url = it.url)
                    pokemonDao.insert(pokemonData)
                }
        }catch (e: Exception){
            e.printStackTrace()
        }

        return pokemonDao.getAllPokemonAsc().flowOn(Dispatchers.IO).conflate()
    }

//    fun getAllPokemonDesc(): LiveData<List<PokemonEntity>> = mPokemonDao.getAllPokemonDesc()

    suspend fun insert(pokemon: PokemonEntity){
        pokemonDao.insert(pokemon)
    }

    suspend fun delete(pokemon: PokemonEntity){
        pokemonDao.delete(pokemon)
    }

    suspend fun update(pokemon: PokemonEntity){
        pokemonDao.update(pokemon)
    }

//    suspend fun getPokemons(): Resource<List<com.compose.pokeapp.model.Result>> {
//
//        return try {
//            Resource.Loading(data = true)
//
//            val itemList = api.getAllPokemon(limit = 20, offset = 0).results
//            if (itemList.isNotEmpty()){
//                Resource.Loading(data = false)
//                itemList.forEach { res ->
//                    insert(PokemonEntity(name = res.name, url = res.url))
//                }
//            }
//
//            Resource.Success(data = itemList)
//
//        }catch (exception: Exception) {
//            Resource.Error(message = exception.message.toString())
//        }
//
//    }

    suspend fun getPokemonDetails(pokemonId: String): Resource<PokemonDetails>? {
        val response = try {
//            Resource.Loading(data = true)
            api.getBookInfo(pokemonId)

        }catch (exception: Exception){
//            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
//        Resource.Loading(data = false)
        return null
    }


}