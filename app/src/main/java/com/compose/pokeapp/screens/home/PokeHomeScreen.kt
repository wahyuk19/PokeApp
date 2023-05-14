package com.compose.pokeapp.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.compose.pokeapp.BuildConfig
import com.compose.pokeapp.components.PokeAppBar
import com.compose.pokeapp.model.MPoke
import com.compose.pokeapp.navigation.PokeScreens
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(navController: NavController, viewModel: PokeHomeViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        PokeAppBar(title = "Pokemon Lists", navController = navController)
    }) {
        Surface() {
            Column {
                Spacer(modifier = Modifier.height(13.dp))
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
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator()
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
    Card(
        modifier = Modifier
//        .clickable {
//            navController.navigate(PokeScreens.DetailScreen.name + "/${pokemon.name}")
//        }
            .fillMaxWidth()
            .height(72.dp)
            .padding(3.dp),
        shape = RectangleShape,
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
            val id = pokemon.url.substringAfter("pokemon/", "").dropLast(1)
            Log.d("TAG", "PokemonRow: id $id")
            val imageUrl = "${BuildConfig.IMAGE_URL}$id.png"
            Log.d("TAG", "PokemonRow: $imageUrl")
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
