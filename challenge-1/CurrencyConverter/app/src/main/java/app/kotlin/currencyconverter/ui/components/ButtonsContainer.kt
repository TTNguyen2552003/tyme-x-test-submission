package app.kotlin.currencyconverter.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import app.kotlin.currencyconverter.R
import app.kotlin.currencyconverter.ui.motion.standardAnimation
import app.kotlin.currencyconverter.ui.styles.gapPositive600
import app.kotlin.currencyconverter.ui.styles.surfaceContainerDarkColor
import app.kotlin.currencyconverter.ui.styles.surfaceContainerLightColor

val buttons: List<ButtonData> = listOf(
    ButtonData(
        type = ButtonType.NUMPAD,
        textLabel = R.string.numpad_button_label_7
    ),
    ButtonData(
        type = ButtonType.NUMPAD,
        textLabel = R.string.numpad_button_label_8
    ),
    ButtonData(
        type = ButtonType.NUMPAD,
        textLabel = R.string.numpad_button_label_9
    ),
    ButtonData(
        type = ButtonType.FUNCTIONALITY,
        textLabel = R.string.functionality_button_label_clear
    ),
    ButtonData(
        type = ButtonType.NUMPAD,
        textLabel = R.string.numpad_button_label_4
    ),
    ButtonData(
        type = ButtonType.NUMPAD,
        textLabel = R.string.numpad_button_label_5
    ),
    ButtonData(
        type = ButtonType.NUMPAD,
        textLabel = R.string.numpad_button_label_6
    ),
    ButtonData(
        type = ButtonType.FUNCTIONALITY,
        textLabel = R.string.functionality_button_label_delete
    ),
    ButtonData(
        type = ButtonType.NUMPAD,
        textLabel = R.string.numpad_button_label_1
    ),
    ButtonData(
        type = ButtonType.NUMPAD,
        textLabel = R.string.numpad_button_label_2
    ),
    ButtonData(
        type = ButtonType.NUMPAD,
        textLabel = R.string.numpad_button_label_3
    ),
    ButtonData(
        type = ButtonType.FUNCTIONALITY,
        isContainedIcon = true,
        iconLabel = R.drawable.reset_icon
    ),
    ButtonData(
        type = ButtonType.FUNCTIONALITY,
        textLabel = R.string.functionality_button_label_decrease_decimal
    ),
    ButtonData(
        type = ButtonType.NUMPAD,
        textLabel = R.string.numpad_button_label_0
    ),
    ButtonData(
        type = ButtonType.NUMPAD,
        textLabel = R.string.numpad_button_label_dot_sign
    ),
    ButtonData(
        type = ButtonType.FUNCTIONALITY,
        isContainedIcon = true,
        isIconColorStatic = true
    )
)

@Composable
fun ButtonsContainer(
    isDarkTheme: Boolean = false,
    buttons: List<ButtonData>,
    onPressedEvents: List<() -> Unit>
) {
    val backgroundColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            surfaceContainerDarkColor
        else
            surfaceContainerLightColor,
        animationSpec = standardAnimation(),
        label = "buttons container background color"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 4),
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(color = backgroundColor)
            },
        contentPadding = PaddingValues(
            horizontal = gapPositive600,
            vertical = gapPositive600
        ),
        verticalArrangement = Arrangement.spacedBy(space = gapPositive600),
        horizontalArrangement = Arrangement.spacedBy(space = gapPositive600)
    ) {
        items(count = buttons.size - 1) { index: Int ->
            Button(
                isDarkTheme = isDarkTheme,
                type = buttons[index].type,
                isContainedIcon = buttons[index].isContainedIcon,
                isIconColorStatic = buttons[index].isIconColorStatic,
                iconLabel = buttons[index].iconLabel,
                textLabel = buttons[index].textLabel,
                onPressed = onPressedEvents[index]
            )
        }

        item {
            val lastButtonIndex = buttons.size - 1
            buttons[lastButtonIndex].iconLabel = if (isDarkTheme)
                R.drawable.dark_theme_icon
            else
                R.drawable.light_theme_icon
            Button(
                isDarkTheme = isDarkTheme,
                type = buttons[lastButtonIndex].type,
                isContainedIcon = buttons[lastButtonIndex].isContainedIcon,
                isIconColorStatic = buttons[lastButtonIndex].isIconColorStatic,
                iconLabel = buttons[lastButtonIndex].iconLabel,
                textLabel = buttons[lastButtonIndex].textLabel,
                onPressed = onPressedEvents[lastButtonIndex]
            )
        }
    }
}