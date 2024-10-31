package app.kotlin.currencyconverter.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.kotlin.currencyconverter.R
import app.kotlin.currencyconverter.ui.components.Button
import app.kotlin.currencyconverter.ui.components.ButtonType
import app.kotlin.currencyconverter.ui.motion.standardAnimation
import app.kotlin.currencyconverter.ui.navigation.Destination
import app.kotlin.currencyconverter.ui.styles.compactWidthBody
import app.kotlin.currencyconverter.ui.styles.gapPositive200
import app.kotlin.currencyconverter.ui.styles.gapPositive400
import app.kotlin.currencyconverter.ui.styles.gapPositive600
import app.kotlin.currencyconverter.ui.styles.iconSizeExtraSmall
import app.kotlin.currencyconverter.ui.styles.noScale
import app.kotlin.currencyconverter.ui.styles.onSurfaceDarkColor
import app.kotlin.currencyconverter.ui.styles.onSurfaceLightColor
import app.kotlin.currencyconverter.ui.styles.onSurfaceVariantDarkColor
import app.kotlin.currencyconverter.ui.styles.onSurfaceVariantLightColor
import app.kotlin.currencyconverter.ui.styles.primaryDarkColor
import app.kotlin.currencyconverter.ui.styles.primaryLightColor
import app.kotlin.currencyconverter.ui.styles.surfaceDarkColor
import app.kotlin.currencyconverter.ui.styles.surfaceLightColor

@Composable
fun TipsScreen(
    navController: NavController,
    isDarkTheme: Boolean = false,
    hideTipsScreenPermanently: () -> Unit
) {
    val backgroundColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            surfaceDarkColor
        else
            surfaceLightColor,
        animationSpec = standardAnimation(),
        label = "color of Tips Screen surface"
    )

    val imageColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            primaryDarkColor
        else primaryLightColor,
        animationSpec = standardAnimation(),
        label = "color of the image"
    )

    val tipsColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            onSurfaceDarkColor
        else
            onSurfaceLightColor,
        animationSpec = standardAnimation(),
        label = "color used to filter image"
    )

    val doNotShowTipsColor: Color by animateColorAsState(
        targetValue = if (isDarkTheme)
            onSurfaceVariantDarkColor
        else
            onSurfaceVariantLightColor,
        animationSpec = standardAnimation(),
        label = "color of text tips"
    )

    var doNotShowTipsScreenAgainOption: Boolean by remember {
        mutableStateOf(value = false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind { drawRect(color = backgroundColor) }
    ) {
        Column(
            modifier = Modifier
                .width(width = 240.dp)
                .align(alignment = Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(space = gapPositive400),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.tips_image),
                contentDescription = "",
                modifier = Modifier.size(size = 120.dp),
                colorFilter = ColorFilter.tint(color = imageColor)
            )

            Text(
                text = stringResource(id = R.string.tips_screen_tips_message),
                style = compactWidthBody.noScale(),
                color = tipsColor,
                textAlign = TextAlign.Center
            )

            Button(
                isDarkTheme = isDarkTheme,
                type = ButtonType.PRIMARY,
                textLabel = R.string.tips_screen_primary_button_label,
                onPressed = {
                    navController.navigate(route = Destination.MAIN_SCREEN.route) {
                        popUpTo(id = 0) { inclusive = false }
                    }
                    if (doNotShowTipsScreenAgainOption){
                        hideTipsScreenPermanently()
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .offset(y = -gapPositive600),
            horizontalArrangement = Arrangement.spacedBy(space = gapPositive200),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    id = if (doNotShowTipsScreenAgainOption)
                        R.drawable.checked_box
                    else
                        R.drawable.unchecked_box
                ),
                contentDescription = "check box",
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                doNotShowTipsScreenAgainOption = !doNotShowTipsScreenAgainOption
                            }
                        )
                    }
                    .size(size = iconSizeExtraSmall),
                colorFilter = ColorFilter.tint(color = doNotShowTipsColor)
            )

            Text(
                text = stringResource(id = R.string.tips_screen_do_not_show_again_message),
                style = compactWidthBody.noScale(),
                color = doNotShowTipsColor
            )
        }
    }
}