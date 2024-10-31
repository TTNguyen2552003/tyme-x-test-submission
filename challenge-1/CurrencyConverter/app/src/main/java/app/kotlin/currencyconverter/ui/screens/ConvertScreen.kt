package app.kotlin.currencyconverter.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import app.kotlin.currencyconverter.ui.components.ButtonsContainer
import app.kotlin.currencyconverter.ui.components.CurrencyType
import app.kotlin.currencyconverter.ui.components.Display
import app.kotlin.currencyconverter.ui.components.SwapButton
import app.kotlin.currencyconverter.ui.components.buttons
import app.kotlin.currencyconverter.ui.motion.standardAnimation
import app.kotlin.currencyconverter.ui.styles.gapPositive200
import app.kotlin.currencyconverter.ui.styles.gapPositive600
import app.kotlin.currencyconverter.ui.styles.surfaceDarkColor
import app.kotlin.currencyconverter.ui.styles.surfaceLightColor

@Composable
fun ConvertScreen(
    isDarkTheme: Boolean = false,
    toggleTheme: () -> Unit,
    currencyUnits: List<String>,
    sourceCurrencyUnit: String,
    sourceCurrencyValue: String,
    updateSourceCurrencyUnit: (String) -> Unit,
    swapCurrencyUnit: () -> Unit,
    targetCurrencyUnit: String,
    targetCurrencyValue: String,
    updateTargetCurrencyUnit: (String) -> Unit,
    onPressedEvents: List<() -> Unit>
) {
    val backgroundColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            surfaceDarkColor
        else
            surfaceLightColor,
        animationSpec = standardAnimation(),
        label = "color of screen background"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind { drawRect(color = backgroundColor) }
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f)
                .padding(all = gapPositive600),
            verticalArrangement = Arrangement.spacedBy(space = gapPositive200),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f)
            ) {
                Display(
                    isDarkTheme = isDarkTheme,
                    currencyType = CurrencyType.SOURCE,
                    currencyUnits = currencyUnits,
                    currentCurrencyUnit = sourceCurrencyUnit,
                    currentCurrencyValue = sourceCurrencyValue,
                    updateCurrencyUnit = updateSourceCurrencyUnit
                )
            }

            SwapButton(
                isDarkTheme = isDarkTheme,
                onPressed = swapCurrencyUnit
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f)
            ) {
                Display(
                    isDarkTheme = isDarkTheme,
                    currencyType = CurrencyType.TARGET,
                    currencyUnits = currencyUnits,
                    currentCurrencyUnit = targetCurrencyUnit,
                    currentCurrencyValue = targetCurrencyValue,
                    updateCurrencyUnit = updateTargetCurrencyUnit,
                )
            }
        }

        ButtonsContainer(
            isDarkTheme = isDarkTheme,
            toggleTheme = toggleTheme,
            buttons = buttons,
            onPressedEvents = onPressedEvents
        )
    }
}