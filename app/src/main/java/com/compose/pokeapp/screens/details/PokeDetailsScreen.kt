package com.compose.pokeapp.screens.details

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
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
import com.compose.pokeapp.data.Resource
import com.compose.pokeapp.model.MPoke
import com.compose.pokeapp.model.PokemonDetails
import com.compose.pokeapp.navigation.PokeScreens
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.math.log

@Composable
fun DetailsScreen(navController: NavController,id:String,viewModel: PokeDetailsViewModel = hiltViewModel()){
    Scaffold(topBar = {
        PokeAppBar(title = "Pokemon Details", icon = Icons.Default.ArrowBack, showProfile = false, navController = navController){
            navController.navigate(PokeScreens.HomeScreen.name)
        }
    }) {
        Surface(modifier = Modifier
            .padding(3.dp)
            .fillMaxSize()) {
            Column(modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {

                val pokeInfo = produceState<Resource<PokemonDetails>>(initialValue = Resource.Loading()){
                    value = viewModel.getPokemonDetails(id)
                }.value

                if (pokeInfo.data == null) {
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }else{
                    showPokemonDetails(pokeInfo, navController)
                }
                //  Log.d("Deets", "BookDetailsScreen: ${bookInfo.data.toString()}")
            }
        }
    }
}

@Composable
fun showPokemonDetails(pokeInfo: Resource<PokemonDetails>,
                       navController: NavController) {
    val pokemonData = pokeInfo.data
    var types = ""
    var moves = ""
    var state = 0
    var catchStatus by remember{
        mutableStateOf(state)
    }
    val context = LocalContext.current
//    val pokeId = pokeInfo.data?.id


    Card(modifier = Modifier.padding(34.dp),
        shape = CircleShape, elevation = 4.dp) {
        Image(painter = rememberImagePainter(data = pokemonData?.sprites?.other?.home?.front_default),
            contentDescription = "Book Image",
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
                .padding(1.dp))
    }
    for (i in pokemonData?.types!!.indices){
        types += "${pokemonData.types[i].type.name}, "
        Log.d("TAG", "showPokemonDetails: $types")
    }
    for(i in pokemonData.moves.indices){
        moves += "${pokemonData.moves[i].move.name}, "
        Log.d("TAG", "showPokemonDetails: $moves")
    }
    Text(text = pokemonData.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        style = MaterialTheme.typography.h6,
        overflow = TextOverflow.Ellipsis,)
    Text(text = "Type: ${types.dropLast(2)}")
    Spacer(modifier = Modifier.height(5.dp))
    Text(text = "Moves: $moves", maxLines = 5, modifier = Modifier.padding(4.dp))
    Spacer(modifier = Modifier.height(40.dp))
    FABContent {
        Log.d("TAG", "showPokemonDetails: clicked")
        catchStatus = 1
        val random = Random()
        val randomBoolean = random.nextBoolean()
        Log.d("TAG", "showPokemonDetails: result $randomBoolean")
        Handler(Looper.getMainLooper()).postDelayed({
            if(randomBoolean){
                catchStatus = 2
                val poke = MPoke(name = pokemonData.name, moves = moves, type = types, photoUrl = pokemonData.sprites.other.home.front_default)
                saveToFirebase(context,poke,navController)
            }else{
                catchStatus = 3
            }
        },3000)

    }
    Text(text = if(catchStatus == 0){
        "Tap the pokeball to catch this pokemon"
    }else if(catchStatus == 1){
        "Catching..."
    }else if(catchStatus == 2){
        "${pokemonData.name} catched!!"
    }else{
        "${pokemonData.name} has run away"
    })
//    Text(text = "Categories: ${pokemonData?.categories.toString()}",
//        style = MaterialTheme.typography.subtitle1,
//        maxLines = 3,
//        overflow = TextOverflow.Ellipsis)
//    Text(text = "Published: ${pokemonData?.publishedDate.toString()}",
//        style = MaterialTheme.typography.subtitle1)
//
//    Spacer(modifier = Modifier.height(5.dp))
//
//    val cleanDescription = HtmlCompat.fromHtml(pokemonData!!.description,
//        HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
//    val localDims = LocalContext.current.resources.displayMetrics
//    Surface(modifier = Modifier
//        .height(localDims.heightPixels.dp.times(0.09f))
//        .padding(4.dp),
//        shape = RectangleShape,
//        border = BorderStroke(1.dp, Color.DarkGray)
//    ) {
//
//        LazyColumn(modifier = Modifier.padding(3.dp)) {
//            item {
//
//                Text(text = cleanDescription)
//            }
//
//        }
//    }
//
//    //Buttons
//    Row(modifier = Modifier.padding(top = 6.dp),
//        horizontalArrangement = Arrangement.SpaceAround) {
//        RoundedButton(label = "Save"){
//            //save this book to the firestore database
//            val book = MBook(
//                title = pokemonData.title,
//                authors = pokemonData.authors.toString(),
//                description = pokemonData.description,
//                categories = pokemonData.categories.toString(),
//                notes = "",
//                photoUrl = pokemonData.imageLinks.thumbnail,
//                publishedDate = pokemonData.publishedDate,
//                pageCount = pokemonData.pageCount.toString(),
//                rating = 0.0,
//                googleBookId = pokeId,
//                userId = FirebaseAuth.getInstance().currentUser?.uid.toString())
//
//            saveToFirebase(book, navController = navController)
//
//        }
//        Spacer(modifier = Modifier.width(25.dp))
//        RoundedButton(label = "Cancel"){
//            navController.popBackStack()
//        }
//
//    }


}

fun saveToFirebase(context: Context, pokemon: MPoke, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("pokemon")

    if (pokemon.toString().isNotEmpty()){
        dbCollection.add(pokemon)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                           Toast.makeText(context,"${pokemon.name} successfully added to collection",Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Log.w("Error", "SaveToFirebase:  Error updating db",it )
                    }

            }
    }else {

    }


}