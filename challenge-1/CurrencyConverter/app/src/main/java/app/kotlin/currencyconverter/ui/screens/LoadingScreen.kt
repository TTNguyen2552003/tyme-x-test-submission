package app.kotlin.currencyconverter.ui.screens

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.kotlin.currencyconverter.R
import app.kotlin.currencyconverter.ui.motion.standardAnimation
import app.kotlin.currencyconverter.ui.styles.compactWidthTitle
import app.kotlin.currencyconverter.ui.styles.gapPositive700
import app.kotlin.currencyconverter.ui.styles.noScale
import app.kotlin.currencyconverter.ui.styles.onSurfaceDarkColor
import app.kotlin.currencyconverter.ui.styles.onSurfaceLightColor
import app.kotlin.currencyconverter.ui.styles.surfaceDarkColor
import app.kotlin.currencyconverter.ui.styles.surfaceLightColor
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource

@Composable
fun LoadingScreen(isDarkTheme: Boolean = false) {
    val backgroundColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            surfaceDarkColor
        else
            surfaceLightColor,
        animationSpec = standardAnimation(),
        label = "color of No Internet Screen background"
    )

    val titleColor: Color by animateColorAsState(
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
            val animationPath: String = if (isDarkTheme)
                "loading_dark.lottie"
            else
                "loading_light.lottie"

            DotLottieAnimation(
                source = DotLottieSource.Asset(assetPath = animationPath),
                autoplay = true,
                loop = true,
                modifier = Modifier.size(size = 200.dp)
            )

            Text(
                text = stringResource(id = R.string.loading_screen_title_text),
                style = compactWidthTitle.noScale(),
                color = titleColor
            )
        }
    }
}