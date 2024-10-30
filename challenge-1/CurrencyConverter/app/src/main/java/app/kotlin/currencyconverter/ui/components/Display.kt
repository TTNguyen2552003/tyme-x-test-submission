package app.kotlin.currencyconverter.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import app.kotlin.currencyconverter.ui.motion.standardAnimation
import app.kotlin.currencyconverter.ui.styles.compactWidthDisplay
import app.kotlin.currencyconverter.ui.styles.gapPositive300
import app.kotlin.currencyconverter.ui.styles.noScale
import app.kotlin.currencyconverter.ui.styles.onSurfaceDarkColor
import app.kotlin.currencyconverter.ui.styles.onSurfaceLightColor
import app.kotlin.currencyconverter.ui.styles.primaryDarkColor
import app.kotlin.currencyconverter.ui.styles.primaryLightColor
import app.kotlin.currencyconverter.ui.styles.shapeMedium
import app.kotlin.currencyconverter.ui.styles.surfaceContainerDarkColor
import app.kotlin.currencyconverter.ui.styles.surfaceContainerLightColor

enum class CurrencyType {
    SOURCE,
    TARGET
}

@Composable
fun Display(
    currencyType: CurrencyType,
    isDarkTheme: Boolean = false,
    currencyUnit: String = "VND",
    currencyValue: String = "1000"
) {
    val backgroundColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            surfaceContainerDarkColor
        else
            surfaceContainerLightColor,
        animationSpec = standardAnimation(),
        label = "color of display background"
    )

    val textColor: Color by animateColorAsState(
        targetValue = when (currencyType) {
            CurrencyType.SOURCE -> {
                if (isDarkTheme) primaryDarkColor else primaryLightColor
            }

            CurrencyType.TARGET -> {
                if (isDarkTheme) onSurfaceDarkColor else onSurfaceLightColor
            }
        },
        animationSpec = standardAnimation(),
        label = "color of currency"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRoundRect(
                    color = backgroundColor,
                    cornerRadius = CornerRadius(shapeMedium.toPx())
                )
            }
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = gapPositive300,
                    y = gapPositive300
                )
        ) {
            CurrencyUnitOption(
                isDarkTheme = isDarkTheme,
                unit = currencyUnit
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
                .offset(y = -gapPositive300)
                .padding(horizontal = gapPositive300)
                .horizontalScroll(state = rememberScrollState()),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = currencyValue,
                style = compactWidthDisplay.noScale(),
                color = textColor
            )
        }
    }
}