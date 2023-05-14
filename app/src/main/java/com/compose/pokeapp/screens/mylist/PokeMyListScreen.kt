package com.compose.pokeapp.screens.mylist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.compose.pokeapp.BuildConfig
import com.compose.pokeapp.components.PokeAppBar
import com.compose.pokeapp.model.MPoke
import com.compose.pokeapp.navigation.PokeScreens
import com.compose.pokeapp.screens.home.PokeHomeViewModel
import com.compose.pokeapp.screens.home.PokemonList
import com.compose.pokeapp.screens.home.PokemonRow
import java.util.*

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
                PokemonRow(poke, navController)
                Log.d("TAG", "PokemonList: $poke")
            }

        }
    }

}

@Composable
fun PokemonRow(
    pokemon: MPoke,
    navController: NavController
) {
    val id = pokemon.pokemonId
    val imageUrl = pokemon.photoUrl
    Card(
        modifier = Modifier
            .clickable {
                navController.navigate(PokeScreens.CollectionDetailScreen.name + "/${id}")
            }
            .fillMaxWidth()
            .height(72.dp)
            .padding(3.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 7.dp
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {

//            val imageUrl: String = if(pokemon.volumeInfo.imageLinks.smallThumbnail.isEmpty())
//                "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80"
//            else {
//                pokemon.volumeInfo.imageLinks.smallThumbnail
//            }
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = "pokemon image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp),
            )

            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = pokemon.name!!.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    textAlign = TextAlign.Center
                )
            }


        }

    }

}