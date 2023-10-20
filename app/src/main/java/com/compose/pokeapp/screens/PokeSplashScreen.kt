package com.compose.pokeapp.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.compose.pokeapp.components.PokeAppLogo
import com.compose.pokeapp.navigation.PokeScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController){

//    val scale = remember{
//        Animatable(0f)
//    }
//
//    LaunchedEffect(key1 = true){
//        scale.animateTo(targetValue = 0.9f,
//        animationSpec = tween(durationMillis = 800, easing = {
//            OvershootInterpolator(8f).getInterpolation(it)
//        }))
//        delay(2000L)
//    }

    Surface(modifier = Modifier
        .padding(15.dp)
        .size(3300.dp),
//        .scale(scale.value),
//        shape = CircleShape,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PokeAppLogo()
            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = {
                    navController.navigate(PokeScreens.HomeScreen.name)
                }) {
                    Text(text = "Start", color = Color.Red)
                }
        }
    }
}