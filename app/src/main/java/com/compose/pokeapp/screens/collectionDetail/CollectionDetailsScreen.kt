package com.compose.pokeapp.screens.collectionDetail

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.compose.pokeapp.components.FABContent
import com.compose.pokeapp.components.PokeAppBar
import com.compose.pokeapp.components.PokemonNameInput
import com.compose.pokeapp.model.MPoke
import com.compose.pokeapp.navigation.PokeScreens
import com.compose.pokeapp.screens.login.SubmitButton
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@Composable
fun CollectionDetailsScreen(
    navController: NavController,
    id: String,
    viewModel: CollectionDetailsViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        PokeAppBar(
            title =
            "My Pokemon Details",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ) {
            navController.navigate(PokeScreens.MyListScreen.name)
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

                val pokeList = viewModel.data.value.data
                Log.d("TAG", "CollectionDetailsScreen: $pokeList")
                for (i in pokeList!!.indices) {
                    if (id == pokeList[i].pokemonId) {
                        ShowCollectionDetails(pokeList[i], navController)
                    }
                }
            }
        }
    }
}

@Composable
fun ShowCollectionDetails(
    pokemonData: MPoke,
    navController: NavController
) {
    Log.d("TAG", "ShowCollectionDetails: $pokemonData")
    val types = pokemonData.type
    val moves = pokemonData.moves
    var state = 0
    var catchStatus by remember {
        mutableStateOf(state)
    }
    val name = rememberSaveable { mutableStateOf(pokemonData.alias.toString()) }
    val context = LocalContext.current

    Card(
        modifier = Modifier.padding(34.dp),
        shape = CircleShape, elevation = 4.dp
    ) {
        Image(
            painter = rememberImagePainter(data = pokemonData.photoUrl),
            contentDescription = "pokemon Image",
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
                .padding(1.dp)
        )
    }
    PokemonNameInput(nameState = name, labelId = "Name")
    Button(
        onClick = {
            val mPoke = MPoke(
                name = pokemonData.name,
                moves = moves,
                type = types,
                photoUrl = pokemonData.photoUrl,
                pokemonId = pokemonData.id.toString(),
                t1 = pokemonData.t1,
                t2 = pokemonData.t2,
                alias = name.value
            )
            pokemonData.pokemonId?.let { updateFirebase(context, mPoke, name.value, navController) }
        },
        modifier = Modifier
            .padding(3.dp)
            .width(100.dp),
        shape = CircleShape
    ) {
        Text(text = "Update", modifier = Modifier.padding(5.dp))

    }
    Text(text = "Type: $types")
    Spacer(modifier = Modifier.height(5.dp))
    Text(text = "Moves: $moves", maxLines = 5, modifier = Modifier.padding(4.dp))
    Spacer(modifier = Modifier.height(40.dp))
    FABContent(true) {
        Log.d("TAG", "showPokemonDetails: clicked")
        catchStatus = 1
        //start release/remove pokemon using prime number
        val randomInt = (0..100).random()
        var flag = false
        Log.d("TAG", "showPokemonDetails: result $randomInt")
        for (i in 2..randomInt / 2) {
            if (randomInt % i == 0) {
                flag = true
                break
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if (!flag) {
                catchStatus = 2
                val poke = MPoke(
                    name = pokemonData.name,
                    moves = moves,
                    type = types,
                    photoUrl = pokemonData.photoUrl,
                    pokemonId = pokemonData.id.toString(),
                )
                deleteFromFirebase(context, poke, navController)
                Handler(Looper.getMainLooper()).postDelayed({
                    navController.navigate(PokeScreens.MyListScreen.name)
                }, 1000)
            } else {
                catchStatus = 3
            }
        }, 3000)

    }
    Text(
        text = when (catchStatus) {
            0 -> {
                "Tap the pokeball to release this pokemon"
            }
            1 -> {
                "Releasing..."
            }
            2 -> {
                "${pokemonData.name} released!!"
            }
            else -> {
                "${pokemonData.name} failed to release"
            }
        }
    )
}

fun updateFirebase(
    context: Context,
    pokemonData: MPoke,
    name: String,
    navController: NavController
) {
    //step to fibonacci series
    val n = 100
    var t1 = try {
        pokemonData.t1!!
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
    Log.d("TAG", "updateFirebase: $t1")
    var t2 = try {
        pokemonData.t2!!
    } catch (e: Exception) {
        1
    }
    val updatedName = "${pokemonData.alias}-$t1"

    val sum = t1 + t2
    t1 = t2
    t2 = sum
    Log.d("TAG", "updateFirebase: updated $updatedName")

    val db = FirebaseFirestore.getInstance()
    pokemonData.pokemonId?.let {
        db.collection("pokemon").document(it).update("alias", updatedName, "t1", t1, "t2", t2)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Successfully renamed to $updatedName",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(PokeScreens.MyListScreen.name)
                }

            }.addOnFailureListener {
            Log.w("Error", "updateToFirebase:  Error update db", it)
        }
    }
}

fun deleteFromFirebase(context: Context, pokemon: MPoke, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    pokemon.pokemonId?.let {
        db.collection("pokemon").document(it).delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    context,
                    "${pokemon.name} successfully released from collection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnFailureListener {
            Log.w("Error", "deleteFromFirebase:  Error delete db", it)
        }
    }
}