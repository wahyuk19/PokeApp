package com.compose.pokeapp.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.compose.pokeapp.components.PokeAppBar
import com.compose.pokeapp.data.Resource
import com.compose.pokeapp.model.Ability
import com.compose.pokeapp.model.PokemonDetails
import com.compose.pokeapp.navigation.PokeScreens
import java.util.*

@Composable
fun DetailsScreen(
    navController: NavController,
    id: String,
    viewModel: PokeDetailsViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        PokeAppBar(
            title =
            "Pokemon Details", icon = Icons.Default.ArrowBack, showProfile = false
        ) {
            navController.navigate(PokeScreens.HomeScreen.name)
        }
    }) {
        Surface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                val pokeInfo =
                    produceState<Resource<PokemonDetails>>(initialValue = Resource.Loading()) {
                        value = viewModel.getPokemonDetails(id)
                    }.value

                if (pokeInfo.data == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    ShowPokemonDetails(pokeInfo)
                }

            }
        }
    }
}

@Composable
fun ShowPokemonDetails(
    pokeInfo: Resource<PokemonDetails>,
) {
    val pokemonData = pokeInfo.data
    var types = ""

    Card(
        modifier = Modifier.padding(34.dp),
        shape = CircleShape, elevation = 4.dp
    ) {
        Image(
            painter = rememberImagePainter(data = pokemonData?.sprites?.front_default),
            contentDescription = "pokemon Image",
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
                .padding(1.dp)
        )
    }
    if (pokemonData != null) {
        Text(
            text = pokemonData.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            style = MaterialTheme.typography.h6,
            overflow = TextOverflow.Ellipsis,
        )
    }
    for (i in pokemonData?.types!!.indices) {
        types += "${pokemonData.types[i].type.name}, "
    }
    Text(text = "Type: ${types.dropLast(2)}")
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = "Abilities")
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items = pokemonData.abilities) { ability ->
            MovesRow(ability)
        }
    }
}

@Composable
fun MovesRow(
    ability: Ability,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(3.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 7.dp
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = ability.ability.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    },
                    textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

    }

}