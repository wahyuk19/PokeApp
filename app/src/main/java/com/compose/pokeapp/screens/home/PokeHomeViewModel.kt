package com.compose.pokeapp.screens.home

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.pokeapp.data.Resource
import com.compose.pokeapp.db.PokemonEntity
import com.compose.pokeapp.repository.PokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokeHomeViewModel @Inject constructor(
    private val repository: PokeRepository
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<List<PokemonEntity>>(emptyList())
    val pokemonList = _pokemonList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllPokemonAsc().distinctUntilChanged().collect { listOfPokemon ->
                if(listOfPokemon.isNullOrEmpty()){
                    Log.d("TAG", "empty list")
                }else{
                    _pokemonList.value = listOfPokemon
                }
            }
        }
    }

    suspend fun getAllPokemonAsc() = repository.getAllPokemonAsc()
//    fun getAllPokemonDesc(): LiveData<List<PokemonEntity>> = repository.getAllPokemonDesc()

//    var list: List<com.compose.pokeapp.model.Result> by mutableStateOf(listOf())
//    var isLoading: Boolean by mutableStateOf(true)
//    init {
//        loadPokemons()
//    }
//
//    private fun loadPokemons() {
//        loadList()
//    }
//
//    fun loadList() {
//        viewModelScope.launch(Dispatchers.Default) {
//
//            try {
//                when(val response = repository.getPokemons()) {
//                    is Resource.Success -> {
//                        list = response.data!!
//                        if (list.isNotEmpty()) isLoading = false
//                    }
//                    is Resource.Error -> {
//                        isLoading = false
//                        Log.e("Network", "searchBooks: Failed getting list", )
//                    }
//                    else -> {isLoading = false}
//                }
//
//            }catch (exception: Exception){
//                isLoading = false
//                Log.d("Network", "search Pokemon: ${exception.message.toString()}")
//            }
//
//        }
//
//
//    }

}