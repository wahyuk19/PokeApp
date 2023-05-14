package com.compose.pokeapp.navigation

enum class PokeScreens {
    SplashScreen,
    DetailScreen,
    HomeScreen,
    MyListScreen;

    companion object {
        fun fromRoute(route: String):PokeScreens
        = when(route.substringBefore("/")){
            SplashScreen.name -> SplashScreen
            DetailScreen.name -> DetailScreen
            HomeScreen.name -> HomeScreen
            MyListScreen.name -> MyListScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}