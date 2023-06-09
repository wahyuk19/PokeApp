package com.compose.pokeapp.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.compose.pokeapp.navigation.PokeScreens
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(navController: NavController, viewModel: PokeHomeViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        PokeAppBar(title = "Pokemon Index", navController = navController)
    }) {
        Surface() {
            Column {
                PokemonList(navController = navController)
            }
        }
    }
}

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokeHomeViewModel = hiltViewModel()
) {


    val listOfPokemon = viewModel.list
    if (viewModel.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
    pokemon: com.compose.pokeapp.model.Result,
    navController: NavController
) {
    val id = pokemon.url.substringAfter("pokemon/", "").dropLast(1)
    Log.d("TAG", "PokemonRow: id $id")
    val imageUrl = "${BuildConfig.IMAGE_URL}$id.png"
    Log.d("TAG", "PokemonRow: $imageUrl")
    Card(
        modifier = Modifier
            .clickable {
                navController.navigate(PokeScreens.DetailScreen.name + "/${id}")
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
                    text = pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    textAlign = TextAlign.Center
                )
            }


        }

    }

}
