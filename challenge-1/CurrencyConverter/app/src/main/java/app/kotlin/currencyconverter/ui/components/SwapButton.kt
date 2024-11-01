package app.kotlin.currencyconverter.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import app.kotlin.currencyconverter.R
import app.kotlin.currencyconverter.ui.motion.standardAnimation
import app.kotlin.currencyconverter.ui.styles.iconSizeLarge
import app.kotlin.currencyconverter.ui.styles.iconSizeSmall
import app.kotlin.currencyconverter.ui.styles.onPrimaryContainerDarkColor
import app.kotlin.currencyconverter.ui.styles.onPrimaryContainerLightColor
import app.kotlin.currencyconverter.ui.styles.primaryContainerDarkColor
import app.kotlin.currencyconverter.ui.styles.primaryContainerLightColor
import app.kotlin.currencyconverter.ui.styles.shapeSmall
import app.kotlin.currencyconverter.ui.styles.stateLayerOnPressedDarkColor
import app.kotlin.currencyconverter.ui.styles.stateLayerOnPressedLightColor

@Composable
fun SwapButton(
    isDarkTheme: Boolean = false,
    onPressed: () -> Unit = {}
) {
    val swapButtonBackgroundColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            primaryContainerDarkColor
        else
            primaryContainerLightColor,
        animationSpec = standardAnimation(),
        label = "color of swap button background"
    )

    val swapButtonLabelColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            onPrimaryContainerDarkColor
        else
            onPrimaryContainerLightColor,
        animationSpec = standardAnimation(),
        label = "color of swap button label"
    )

    var isPress: Boolean by remember {
        mutableStateOf(value = false)
    }

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPress = true
                        tryAwaitRelease()
                        isPress = false
                        onPressed()
                    }
                )
            }
            .size(size = iconSizeLarge)
            .drawBehind {
                drawRoundRect(
                    color = swapButtonBackgroundColor,
                    cornerRadius = CornerRadius(shapeSmall.toPx())
                )

                if (isPress) {
                    drawRoundRect(
                        color = if (isDarkTheme)
                            stateLayerOnPressedDarkColor
                        else
                            stateLayerOnPressedLightColor,
                        cornerRadius = CornerRadius(shapeSmall.toPx())
                    )
                }
            }
            ,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.swap_icon),
            contentDescription = "swap icon",
            modifier = Modifier.size(size = iconSizeSmall),
            colorFilter = ColorFilter.tint(color = swapButtonLabelColor)
        )
    }
}