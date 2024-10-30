package app.kotlin.currencyconverter.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import app.kotlin.currencyconverter.ui.motion.standardAnimation
import app.kotlin.currencyconverter.ui.styles.compactWidthLabelMedium
import app.kotlin.currencyconverter.ui.styles.gapPositive200
import app.kotlin.currencyconverter.ui.styles.gapPositive300
import app.kotlin.currencyconverter.ui.styles.noScale
import app.kotlin.currencyconverter.ui.styles.onSurfaceDarkColor
import app.kotlin.currencyconverter.ui.styles.onSurfaceLightColor
import app.kotlin.currencyconverter.ui.styles.stateLayerOnPressedDarkColor
import app.kotlin.currencyconverter.ui.styles.stateLayerOnPressedLightColor
import app.kotlin.currencyconverter.ui.styles.surfaceContainerTint1DarkColor
import app.kotlin.currencyconverter.ui.styles.surfaceContainerTint1LightColor

@Composable
fun CurrencyDropdownMenuItem(
    isDarkTheme: Boolean = false,
    unit: String,
    onTapped: () -> Unit = {}
) {
    var isPressed: Boolean by remember { mutableStateOf(value = false) }

    val backgroundColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            surfaceContainerTint1DarkColor
        else
            surfaceContainerTint1LightColor,
        animationSpec = standardAnimation(),
        label = "background color"
    )

    val textColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            onSurfaceDarkColor
        else
            onSurfaceLightColor,
        animationSpec = standardAnimation(),
        label = "text color"
    )

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTapped() },
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            }
            .width(width = 200.dp)
            .drawBehind {
                drawRect(color = backgroundColor)
                if (isPressed) {
                    drawRect(
                        color = if (isDarkTheme)
                            stateLayerOnPressedDarkColor
                        else
                            stateLayerOnPressedLightColor
                    )
                }
            }
            .padding(
                start = gapPositive300,
                end = gapPositive200,
                top = gapPositive200,
                bottom = gapPositive200
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = unit,
            style = compactWidthLabelMedium.noScale(),
            color = textColor
        )
    }
}