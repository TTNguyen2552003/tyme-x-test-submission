package app.kotlin.currencyconverter.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.kotlin.currencyconverter.R
import app.kotlin.currencyconverter.ui.motion.standardAnimation
import app.kotlin.currencyconverter.ui.styles.compactWidthLabelMedium
import app.kotlin.currencyconverter.ui.styles.gapPositive200
import app.kotlin.currencyconverter.ui.styles.gapPositive300
import app.kotlin.currencyconverter.ui.styles.iconSizeExtraSmall
import app.kotlin.currencyconverter.ui.styles.noScale
import app.kotlin.currencyconverter.ui.styles.onSurfaceDarkColor
import app.kotlin.currencyconverter.ui.styles.onSurfaceLightColor
import app.kotlin.currencyconverter.ui.styles.shapeMedium
import app.kotlin.currencyconverter.ui.styles.shapeSmall
import app.kotlin.currencyconverter.ui.styles.stateLayerOnPressedDarkColor
import app.kotlin.currencyconverter.ui.styles.stateLayerOnPressedLightColor
import app.kotlin.currencyconverter.ui.styles.surfaceContainerTint1DarkColor
import app.kotlin.currencyconverter.ui.styles.surfaceContainerTint1LightColor

@Composable
fun CurrencyUnitOption(
    isDarkTheme: Boolean = false,
    currencyUnits: List<String>,
    currentCurrencyUnit: String,
    updateCurrencyUnit: (String) -> Unit
) {
    var isPressed: Boolean by remember { mutableStateOf(value = false) }
    var isDropdownMenuShown: Boolean by remember { mutableStateOf(value = false) }

    val backgroundColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            surfaceContainerTint1DarkColor
        else
            surfaceContainerTint1LightColor,
        animationSpec = standardAnimation(),
        label = "color of background of Currency Unit Option"
    )

    val textAndIconColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            onSurfaceDarkColor
        else
            onSurfaceLightColor,
        animationSpec = standardAnimation(),
        label = "color of text and icon of Currency Unit Option"
    )

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        isDropdownMenuShown = true
                    }
                )
            }
            .width(width = 108.dp)
            .clip(shape = RoundedCornerShape(size = shapeMedium))
            .drawBehind {
                drawRoundRect(
                    color = backgroundColor,
                    cornerRadius = CornerRadius(shapeSmall.toPx())
                )

                if (isPressed) {
                    drawRoundRect(
                        color = if (isDarkTheme)
                            stateLayerOnPressedDarkColor
                        else
                            stateLayerOnPressedLightColor,
                        cornerRadius = CornerRadius(shapeSmall.toPx())
                    )
                }
            }

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = gapPositive300,
                    end = gapPositive200,
                    top = gapPositive200,
                    bottom = gapPositive200
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentCurrencyUnit,
                style = compactWidthLabelMedium.noScale(),
                color = textAndIconColor
            )

            Image(
                painter = painterResource(id = R.drawable.expand_icon),
                contentDescription = "expand icon",
                modifier = Modifier.size(size = iconSizeExtraSmall),
                colorFilter = ColorFilter.tint(color = textAndIconColor)
            )
        }

        if (isDropdownMenuShown) {
            CurrencyDropdownMenu(
                isDarkTheme = isDarkTheme,
                onDismissRequest = { isDropdownMenuShown = false },
                currencyUnits = currencyUnits,
                updateCurrencyUnit = updateCurrencyUnit
            )
        }
    }
}