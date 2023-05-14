package com.compose.pokeapp.screens.details

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.pokeapp.data.DataOrException
import com.compose.pokeapp.data.Resource
import com.compose.pokeapp.model.MPoke
import com.compose.pokeapp.model.PokemonDetails
import com.compose.pokeapp.repository.FireRepository
import com.compose.pokeapp.repository.PokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokeDetailsViewModel @Inject constructor(private val repository: PokeRepository):ViewModel() {
    suspend fun getPokemonDetails(id: String): Resource<PokemonDetails>{
        return repository.getPokemonDetails(id)
    }
}