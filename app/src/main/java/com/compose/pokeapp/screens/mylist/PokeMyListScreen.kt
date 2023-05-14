package com.compose.pokeapp.screens.mylist

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.compose.pokeapp.components.PokeAppBar
import com.compose.pokeapp.screens.home.PokeHomeViewModel
import com.compose.pokeapp.screens.home.PokemonList
import com.compose.pokeapp.screens.home.PokemonRow

@Composable
fun MyListScreen(navController: NavController,viewModel: PokeMyListViewModel = hiltViewModel()){
    Scaffold(topBar = {
        PokeAppBar(title = "My Pokemon Collection",icon = Icons.Default.ArrowBack,showProfile = false, navController = navController)
    }) {
        Surface() {
            Column {
                PokemonMyList(navController = navController)
            }
        }
    }
}

@Composable
fun PokemonMyList(
    navController: NavController,
    viewModel: PokeMyListViewModel = hiltViewModel()
) {


    val listOfPokemon = viewModel.data.value.data!!
    if (viewModel.data.value.loading == true) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }

    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items = listOfPokemon) { poke ->
//                PokemonRow(poke, navController)
                Log.d("TAG", "PokemonList: $poke")
            }

        }
    }

}