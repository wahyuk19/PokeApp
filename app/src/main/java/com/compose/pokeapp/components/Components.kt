package com.compose.pokeapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.compose.pokeapp.R
import com.compose.pokeapp.navigation.PokeScreens

@Composable
fun PokeAppLogo() {
    Image(
        painter = rememberImagePainter(data = R.drawable.ic_pokemon_logo),
        contentDescription = "pokemon image"
    )
}

@Composable
fun PokeAppBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    onBackArrowClicked: () -> Unit = {}
) {

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showProfile) {
                    Image(painter = rememberImagePainter(data = R.drawable.ic_megaball),
                        contentDescription = "Logo Icon",
                        modifier = Modifier
                            .width(36.dp)
                            .height(36.dp)
                    )

                }
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = "arrow back",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.clickable { onBackArrowClicked.invoke() })
                }
                Spacer(modifier = Modifier.width(40.dp))
                Text(
                    text = title,
                    color = Color.Red.copy(alpha = 0.7f),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )


            }


        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    )

}
