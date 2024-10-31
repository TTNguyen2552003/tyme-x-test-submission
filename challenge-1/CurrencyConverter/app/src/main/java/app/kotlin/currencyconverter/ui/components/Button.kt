package app.kotlin.currencyconverter.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.kotlin.currencyconverter.ui.motion.standardAnimation
import app.kotlin.currencyconverter.ui.styles.compactWidthLabelLarge
import app.kotlin.currencyconverter.ui.styles.gapPositive300
import app.kotlin.currencyconverter.ui.styles.gapPositive600
import app.kotlin.currencyconverter.ui.styles.noScale
import app.kotlin.currencyconverter.ui.styles.onPrimaryContainerDarkColor
import app.kotlin.currencyconverter.ui.styles.onPrimaryContainerLightColor
import app.kotlin.currencyconverter.ui.styles.onSurfaceDarkColor
import app.kotlin.currencyconverter.ui.styles.onSurfaceLightColor
import app.kotlin.currencyconverter.ui.styles.primaryContainerDarkColor
import app.kotlin.currencyconverter.ui.styles.primaryContainerLightColor
import app.kotlin.currencyconverter.ui.styles.shapeMedium
import app.kotlin.currencyconverter.ui.styles.stateLayerOnPressedDarkColor
import app.kotlin.currencyconverter.ui.styles.stateLayerOnPressedLightColor
import app.kotlin.currencyconverter.ui.styles.surfaceContainerTint1DarkColor
import app.kotlin.currencyconverter.ui.styles.surfaceContainerTint1LightColor

enum class ButtonType {
    PRIMARY,
    NUMPAD,
    FUNCTIONALITY
}

data class ButtonData(
    val type: ButtonType,
    val isContainedIcon: Boolean = false,
    val isIconColorStatic: Boolean = false,
    @DrawableRes var iconLabel: Int? = null,
    @StringRes val textLabel: Int? = null
)

@Composable
fun Button(
    isDarkTheme: Boolean = false,
    type: ButtonType,
    isContainedIcon: Boolean = false,
    isIconColorStatic: Boolean = false,
    @DrawableRes iconLabel: Int? = null,
    @StringRes textLabel: Int? = null,
    onPressed: () -> Unit = {}
) {
    val backgroundColor: Color by animateColorAsState(
        targetValue = when (type) {
            ButtonType.PRIMARY, ButtonType.FUNCTIONALITY -> {
                if (isDarkTheme)
                    primaryContainerDarkColor
                else
                    primaryContainerLightColor
            }

            ButtonType.NUMPAD -> {
                if (isDarkTheme)
                    surfaceContainerTint1DarkColor
                else
                    surfaceContainerTint1LightColor
            }
        },
        animationSpec = standardAnimation(),
        label = "color of button background"
    )

    val labelColor: Color by animateColorAsState(
        targetValue = when (type) {
            ButtonType.PRIMARY, ButtonType.FUNCTIONALITY -> {
                if (isDarkTheme)
                    onPrimaryContainerDarkColor
                else
                    onPrimaryContainerLightColor
            }

            ButtonType.NUMPAD -> {
                if (isDarkTheme)
                    onSurfaceDarkColor
                else
                    onSurfaceLightColor
            }
        },
        animationSpec = standardAnimation(),
        label = "color of button label"
    )

    var isPress: Boolean by remember {
        mutableStateOf(value = false)
    }

    when (type) {
        ButtonType.PRIMARY -> {
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
                    .drawBehind {
                        drawRoundRect(
                            color = backgroundColor,
                            cornerRadius = CornerRadius(shapeMedium.toPx())
                        )

                        if (isPress) {
                            drawRoundRect(
                                color = if (isDarkTheme)
                                    stateLayerOnPressedDarkColor
                                else
                                    stateLayerOnPressedLightColor,
                                cornerRadius = CornerRadius(shapeMedium.toPx())
                            )
                        }
                    }
                    .padding(
                        horizontal = gapPositive600,
                        vertical = gapPositive300
                    )
            ) {
                Text(
                    text = stringResource(id = textLabel ?: 0),
                    style = compactWidthLabelLarge.noScale(),
                    color = labelColor
                )
            }
        }

        ButtonType.NUMPAD -> {
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
                    .fillMaxWidth()
                    .aspectRatio(ratio = 1f / 1f)
                    .drawBehind {
                        drawRoundRect(
                            color = backgroundColor,
                            cornerRadius = CornerRadius(shapeMedium.toPx())
                        )

                        if (isPress) {
                            drawRoundRect(
                                color = if (isDarkTheme)
                                    stateLayerOnPressedDarkColor
                                else
                                    stateLayerOnPressedLightColor,
                                cornerRadius = CornerRadius(shapeMedium.toPx())
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = textLabel ?: 0),
                    style = compactWidthLabelLarge.noScale(),
                    color = labelColor
                )
            }
        }

        ButtonType.FUNCTIONALITY -> {
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
                    .fillMaxWidth()
                    .aspectRatio(ratio = 1f / 1f)
                    .drawBehind {
                        drawRoundRect(
                            color = backgroundColor,
                            cornerRadius = CornerRadius(shapeMedium.toPx())
                        )

                        if (isPress) {
                            drawRoundRect(
                                color = if (isDarkTheme)
                                    stateLayerOnPressedDarkColor
                                else
                                    stateLayerOnPressedLightColor,
                                cornerRadius = CornerRadius(shapeMedium.toPx())
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isContainedIcon) {
                    if (isIconColorStatic) {
                        Image(
                            painter = painterResource(id = iconLabel ?: 0),
                            contentDescription = "",
                            modifier = Modifier.size(size = 40.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = iconLabel ?: 0),
                            contentDescription = "",
                            modifier = Modifier.size(size = 40.dp),
                            colorFilter = ColorFilter.tint(color = labelColor)
                        )
                    }
                } else {
                    Text(
                        text = stringResource(id = textLabel ?: 0),
                        style = compactWidthLabelLarge.noScale(),
                        color = labelColor
                    )
                }
            }
        }
    }
}

