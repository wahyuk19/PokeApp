package com.compose.pokeapp.screens.collectionDetail

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
class CollectionDetailsViewModel @Inject constructor(private val fireRepository: FireRepository):ViewModel() {
    val data: MutableState<DataOrException<List<MPoke>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(), true,Exception("")))

    init {
        getAllPokemonFromDatabase()
    }

    private fun getAllPokemonFromDatabase() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = fireRepository.getListFromDatabase()
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
    }
}