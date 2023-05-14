package com.compose.pokeapp.screens.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.pokeapp.data.Resource
import com.compose.pokeapp.repository.PokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokeHomeViewModel @Inject constructor(
    private val repository: PokeRepository
) : ViewModel() {
    var list: List<com.compose.pokeapp.model.Result> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)
    init {
        loadPokemons()
    }

    private fun loadPokemons() {
        loadList()
    }

    fun loadList() {
        viewModelScope.launch(Dispatchers.Default) {

            try {
                when(val response = repository.getPokemons()) {
                    is Resource.Success -> {
                        list = response.data!!
                        if (list.isNotEmpty()) isLoading = false
                    }
                    is Resource.Error -> {
                        isLoading = false
                        Log.e("Network", "searchBooks: Failed getting list", )
                    }
                    else -> {isLoading = false}
                }

            }catch (exception: Exception){
                isLoading = false
                Log.d("Network", "search Pokemon: ${exception.message.toString()}")
            }

        }


    }

}