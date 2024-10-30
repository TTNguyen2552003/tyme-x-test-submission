package app.kotlin.currencyconverter.ui.motion

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import app.kotlin.currencyconverter.STANDARD_MOTION_DURATION

fun <T> standardAnimation():TweenSpec<T> {
    return tween(
        durationMillis = STANDARD_MOTION_DURATION,
        easing = EaseOut
    )
}