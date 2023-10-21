package com.compose.pokeapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.compose.pokeapp.components.PokeAppLogo
import com.compose.pokeapp.navigation.PokeScreens

@Composable
fun SplashScreen(navController: NavController) {

    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(3300.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            PokeAppLogo()
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedButton(shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, color = Color.Black), onClick = {
                    navController.navigate(PokeScreens.HomeScreen.name)
                }) {
                Text(
                    text = "Start",
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}