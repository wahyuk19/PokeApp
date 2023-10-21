package com.compose.pokeapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.compose.pokeapp.R

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
                    Image(
                        painter = rememberImagePainter(data = R.drawable.ic_megaball),
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

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    nameState: MutableState<String>,
    labelId: String = "Search",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    searchCallback: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        InputField(
            modifier = modifier,
            valueState = nameState,
            labelId = labelId,
            enabled = enabled,
            keyboardType = KeyboardType.Text,
            imeAction = imeAction,
            onAction = onAction,
        )
        IconButton(
            onClick = { searchCallback.invoke() },
            modifier = Modifier
                .background(MaterialTheme.colors.onBackground)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.White
            )
        }
    }


}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
) {
    TextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = MaterialTheme.colors.onBackground
        ),
        modifier = modifier
            .padding(bottom = 4.dp, start = 10.dp, end = 10.dp),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        trailingIcon = {
            if (valueState.value.isNotEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_cancel_24),
                    contentDescription = "clear button",
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            valueState.value = ""
                        })
                    })
            }
        }
    )
}
