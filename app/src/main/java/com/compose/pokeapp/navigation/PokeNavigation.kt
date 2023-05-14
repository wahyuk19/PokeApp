package com.compose.pokeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.compose.pokeapp.screens.SplashScreen
import com.compose.pokeapp.screens.details.DetailsScreen
import com.compose.pokeapp.screens.home.Home
import com.compose.pokeapp.screens.home.PokeHomeViewModel
import com.compose.pokeapp.screens.login.PokeLoginScreen
import com.compose.pokeapp.screens.mylist.MyListScreen
import com.compose.pokeapp.screens.mylist.PokeMyListViewModel

@ExperimentalComposeUiApi
@Composable
fun PokeNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = PokeScreens.SplashScreen.name ){
        composable(PokeScreens.SplashScreen.name){
            SplashScreen(navController = navController)
        }
        composable(PokeScreens.LoginScreen.name) {
            PokeLoginScreen(navController = navController)
        }
        composable(PokeScreens.MyListScreen.name){
            val myListViewModel = hiltViewModel<PokeMyListViewModel>()
            MyListScreen(navController = navController, viewModel = myListViewModel)
        }
        composable(PokeScreens.HomeScreen.name){
            val homeViewModel = hiltViewModel<PokeHomeViewModel>()
            Home(navController = navController, viewModel = homeViewModel)
        }
        val detail = PokeScreens.DetailScreen.name
        composable("$detail/{pokeId}",arguments = listOf(navArgument("pokeId"){
            type = NavType.StringType
        })){backStackEntry ->
            backStackEntry.arguments?.getString("pokeId").let {
                DetailsScreen(navController = navController, id = it.toString())
            }
        }
    }
}