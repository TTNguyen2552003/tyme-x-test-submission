package app.kotlin.currencyconverter.ui.screens

import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.kotlin.currencyconverter.R
import app.kotlin.currencyconverter.ui.components.Button
import app.kotlin.currencyconverter.ui.components.ButtonType
import app.kotlin.currencyconverter.ui.motion.standardAnimation
import app.kotlin.currencyconverter.ui.styles.compactWidthBody
import app.kotlin.currencyconverter.ui.styles.compactWidthTitle
import app.kotlin.currencyconverter.ui.styles.gapPositive200
import app.kotlin.currencyconverter.ui.styles.gapPositive700
import app.kotlin.currencyconverter.ui.styles.noScale
import app.kotlin.currencyconverter.ui.styles.onSurfaceDarkColor
import app.kotlin.currencyconverter.ui.styles.onSurfaceLightColor
import app.kotlin.currencyconverter.ui.styles.onSurfaceVariantDarkColor
import app.kotlin.currencyconverter.ui.styles.onSurfaceVariantLightColor
import app.kotlin.currencyconverter.ui.styles.surfaceDarkColor
import app.kotlin.currencyconverter.ui.styles.surfaceLightColor

@Composable
fun LoadFailedScreen(isDarkTheme: Boolean = false) {
    val backgroundColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            surfaceDarkColor
        else
            surfaceLightColor,
        animationSpec = standardAnimation(),
        label = "color of Load Failed Screen background"
    )

    val titleAndImageColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            onSurfaceDarkColor
        else
            onSurfaceLightColor,
        animationSpec = standardAnimation(),
        label = "color of Load Failed Screen title and image"
    )

    val bodyTextColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            onSurfaceVariantDarkColor
        else
            onSurfaceVariantLightColor,
        animationSpec = standardAnimation(),
        label = "color of Load Failed Screen body text"
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
                painter = painterResource(id = R.drawable.broken_service_image),
                contentDescription = "",
                modifier = Modifier.size(size = 120.dp),
                colorFilter = ColorFilter.tint(color = titleAndImageColor)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(space = gapPositive200),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.load_failed_screen_title_text),
                    style = compactWidthTitle.noScale(),
                    color = titleAndImageColor
                )

                Text(
                    text = stringResource(id = R.string.load_failed_screen_body_text),
                    style = compactWidthBody.noScale(),
                    color = bodyTextColor
                )
            }
            val activity = (LocalContext.current as? Activity)
            Button(
                isDarkTheme = isDarkTheme,
                type = ButtonType.PRIMARY,
                textLabel = R.string.load_failed_screen_button_label,
                onPressed = { activity?.finishAffinity() }
            )
        }
    }
}