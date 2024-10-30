package app.kotlin.currencyconverter.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.kotlin.currencyconverter.R
import app.kotlin.currencyconverter.ui.components.Button
import app.kotlin.currencyconverter.ui.components.ButtonType
import app.kotlin.currencyconverter.ui.motion.standardAnimation
import app.kotlin.currencyconverter.ui.styles.compactWidthTitle
import app.kotlin.currencyconverter.ui.styles.gapPositive700
import app.kotlin.currencyconverter.ui.styles.noScale
import app.kotlin.currencyconverter.ui.styles.onSurfaceDarkColor
import app.kotlin.currencyconverter.ui.styles.onSurfaceLightColor
import app.kotlin.currencyconverter.ui.styles.surfaceDarkColor
import app.kotlin.currencyconverter.ui.styles.surfaceLightColor

@Composable
fun NoInternetScreen(isDarkTheme: Boolean = false) {
    val backgroundColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            surfaceDarkColor
        else
            surfaceLightColor,
        animationSpec = standardAnimation(),
        label = "color of No Internet Screen background"
    )

    val titleAndImageColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            onSurfaceDarkColor
        else
            onSurfaceLightColor,
        animationSpec = standardAnimation(),
        label = "color of No Internet Screen title and image"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind { drawRect(color = backgroundColor) }
    ) {
        Column(
            modifier = Modifier.align(alignment = Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(space = gapPositive700),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.no_internet_image),
                contentDescription = "",
                modifier = Modifier.size(size = 120.dp),
                colorFilter = ColorFilter.tint(color = titleAndImageColor)
            )

            Text(
                text = stringResource(id = R.string.no_internet_screen_title_text),
                style = compactWidthTitle.noScale(),
                color = titleAndImageColor
            )

            Button(
                isDarkTheme = isDarkTheme,
                type = ButtonType.PRIMARY,
                textLabel = R.string.no_internet_screen_primary_button_label
            )
        }
    }
}