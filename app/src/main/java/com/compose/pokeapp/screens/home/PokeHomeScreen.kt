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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.compose.pokeapp.BuildConfig
import com.compose.pokeapp.R
import com.compose.pokeapp.components.PokeAppBar
import com.compose.pokeapp.components.SearchBar
import com.compose.pokeapp.db.PokemonEntity
import com.compose.pokeapp.navigation.PokeScreens
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(navController: NavController, viewModel: PokeHomeViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        PokeAppBar(title = "Pokemon Index")
    }) {
        Surface() {
            Column {
                PokemonList(navController = navController, viewModel)
            }
        }
    }
}

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokeHomeViewModel
) {
    val name = rememberSaveable { mutableStateOf("") }
    val isSearch = rememberSaveable { mutableStateOf(false) }
    var searchPokemon by remember { mutableStateOf(mutableListOf<PokemonEntity>()) }
    var selectedOption by remember { mutableStateOf(0) }
    var pokemonList by remember {
        mutableStateOf(mutableListOf<PokemonEntity>())
    }

    Spacer(modifier = Modifier.height(4.dp))
    SearchBar(nameState = name, searchCallback = {
        isSearch.value = true
        viewModel.getPokemonByName(name.value) { pokeData ->
            searchPokemon = pokeData.toMutableList()
        }
    })
    if (name.value.isEmpty()) {
        if (pokemonList.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Sort : ")
                    RadioButton(selected = selectedOption == 0, onClick = {
                        selectedOption = 0
                        isSearch.value = false
                    })
                    Text(text = "Asc")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selectedOption == 1, onClick = {
                        selectedOption = 1
                        isSearch.value = false
                    })
                    Text(text = "Desc")
                }
            }
        }
        isSearch.value = false
    }

    Log.d("TAG", "PokemonList: ${name.value}")
    if (isSearch.value) {
        Log.d("TAG", "PokemonList: query $searchPokemon")
        Spacer(modifier = Modifier.height(4.dp))
        if (searchPokemon.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {

                items(items = searchPokemon) { poke ->
                    PokemonRow(poke, navController)
                    Log.d("TAG", "PokemonList: $poke")
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "No Records found")
            }
        }
    } else {
        if (selectedOption == 0) {
            pokemonList = viewModel.pokemonList.collectAsState().value.toMutableList()
        } else {
            viewModel.getAllPokemonDesc { descData ->
                Log.d("TAG", "PokemonList: descData $descData")
                pokemonList = descData.toMutableList()
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        if (pokemonList.isEmpty()) {
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
                items(items = pokemonList) { poke ->
                    PokemonRow(poke, navController)
                    Log.d("TAG", "PokemonList: dataShow $poke")
                }
            }
        }
    }
}

@Composable
fun PokemonRow(
    pokemon: PokemonEntity,
    navController: NavController
) {
    val id = pokemon.url.substringAfter("pokemon/", "").dropLast(1)
    val imageUrl = "${BuildConfig.IMAGE_URL}$id.png"
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
                painter = rememberImagePainter(data = imageUrl, builder = {
                    placeholder(R.drawable.ic_baseline_broken_image_24)
                }),
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
