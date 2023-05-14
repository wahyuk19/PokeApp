package com.compose.pokeapp.screens.mylist

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.pokeapp.data.DataOrException
import com.compose.pokeapp.model.MPoke
import com.compose.pokeapp.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokeMyListViewModel @Inject constructor(
    private val repository: FireRepository
):ViewModel() {
    val data: MutableState<DataOrException<List<MPoke>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(), true,Exception("")))

    init {
        getAllPokemonFromDatabase()
    }

    private fun getAllPokemonFromDatabase() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getListFromDatabase()
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
        Log.d("GET", "getAllBooksFromDatabase: ${data.value.data?.toList().toString()}")
    }
}