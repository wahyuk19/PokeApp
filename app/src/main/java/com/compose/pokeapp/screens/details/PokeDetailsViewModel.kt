package com.compose.pokeapp.screens.details

import androidx.lifecycle.ViewModel
import com.compose.pokeapp.data.Resource
import com.compose.pokeapp.model.PokemonDetails
import com.compose.pokeapp.repository.PokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokeDetailsViewModel @Inject constructor(private val repository: PokeRepository):ViewModel() {
    suspend fun getPokemonDetails(id: String): Resource<PokemonDetails>{
        return repository.getPokemonDetails(id)
    }
}