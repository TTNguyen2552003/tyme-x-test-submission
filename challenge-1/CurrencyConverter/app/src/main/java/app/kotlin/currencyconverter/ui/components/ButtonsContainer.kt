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
        isIconColorStatic = true,
        iconLabel = R.drawable.light_theme_icon
    ),
    ButtonData(
        type = ButtonType.FUNCTIONALITY,
        isContainedIcon = true,
        isIconColorStatic = true,
        iconLabel = R.drawable.dark_theme_icon
    )
)

@Composable
fun ButtonsContainer(
    isDarkTheme: Boolean = false,
    buttons: List<ButtonData>,
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
        items(count = buttons.size - 2) { index: Int ->
            Button(
                isDarkTheme = isDarkTheme,
                type = buttons[index].type,
                isContainedIcon = buttons[index].isContainedIcon,
                isIconColorStatic = buttons[index].isIconColorStatic,
                iconLabel = buttons[index].iconLabel,
                textLabel = buttons[index].textLabel
            )
        }
        item {
            val toggleThemeButtonData = if (isDarkTheme)
                buttons[buttons.size - 1]
            else
                buttons[buttons.size - 2]
            Button(
                isDarkTheme = isDarkTheme,
                type = toggleThemeButtonData.type,
                isContainedIcon = toggleThemeButtonData.isContainedIcon,
                isIconColorStatic = toggleThemeButtonData.isIconColorStatic,
                iconLabel = toggleThemeButtonData.iconLabel,
                textLabel = toggleThemeButtonData.textLabel,
            )
        }
    }
}