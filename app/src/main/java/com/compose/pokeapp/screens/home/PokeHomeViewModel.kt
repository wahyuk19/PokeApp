package com.compose.pokeapp.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.pokeapp.db.PokemonEntity
import com.compose.pokeapp.repository.PokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            repository.getPokemonsAsc().distinctUntilChanged().collect { listOfPokemon ->
                if (listOfPokemon.isEmpty()) {
                    Log.d("TAG", "empty list")
                    repository.getPokemonList().distinctUntilChanged().collect { pokemonList ->
                        run {
                            _pokemonList.value = pokemonList
                        }
                    }
                } else {
                    _pokemonList.value = listOfPokemon
                }
            }
        }
    }

    fun getAllPokemonDesc(callback: (List<PokemonEntity>) -> Unit) {
        viewModelScope.launch {
            repository.getPokemonDesc().distinctUntilChanged().collect { pokemon ->
                Log.d("TAG", "getAllPokemonDesc: data $pokemon")
                callback(pokemon)
            }
        }
    }


    fun getPokemonByName(pokemonName: String, callback: (List<PokemonEntity>) -> Unit) {
        viewModelScope.launch {
            repository.getPokemonByName(pokemonName).distinctUntilChanged().collect { pokemon ->
                if (pokemon.isEmpty()) {
                    Log.d("TAG", "getPokemonByName: no record found")
                } else {
                    callback(pokemon)
                    Log.d("TAG", "getPokemonByName: $pokemon")
                }
            }
        }
    }

}