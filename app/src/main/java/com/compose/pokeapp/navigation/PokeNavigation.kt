package com.compose.pokeapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.compose.pokeapp.screens.SplashScreen
import com.compose.pokeapp.screens.home.Home
import com.compose.pokeapp.screens.home.PokeHomeViewModel

@Composable
fun PokeNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = PokeScreens.SplashScreen.name ){
        composable(PokeScreens.SplashScreen.name){
            SplashScreen(navController = navController)
        }
        composable(PokeScreens.HomeScreen.name){
            val homeViewModel = hiltViewModel<PokeHomeViewModel>()
            Home(navController = navController, viewModel = homeViewModel)
        }
    }
}