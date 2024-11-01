package app.kotlin.currencyconverter.ui.styles

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle

@Composable
fun TextStyle.noScale(): TextStyle {
    val fontScale: Float = LocalConfiguration.current.fontScale
    return this.copy(fontSize = this.fontSize / fontScale)
}

val compactWidthTitle: TextStyle = TextStyle(
    fontFamily = fontFamilyBrand,
    fontWeight = fontWeightSemiBold,
    fontSize = typeScaleCompactWidthScaleUp4,
    lineHeight = typeScaleCompactWidthScaleUp4 * COMPACT_WIDTH_LINE_HEIGHT
)

val compactWidthDisplay: TextStyle = TextStyle(
    fontFamily = fontFamilyBrand,
    fontWeight = fontWeightMedium,
    fontSize = typeScaleCompactWidthScaleUp3,
    lineHeight = typeScaleCompactWidthScaleUp3 * COMPACT_WIDTH_LINE_HEIGHT
)

val compactWidthLabelLarge: TextStyle = TextStyle(
    fontFamily = fontFamilyBrand,
    fontWeight = fontWeightMedium,
    fontSize = typeScaleCompactWidthScaleUp2,
    lineHeight = typeScaleCompactWidthScaleUp2 * COMPACT_WIDTH_LINE_HEIGHT
)

val compactWidthLabelMedium: TextStyle = TextStyle(
    fontFamily = fontFamilyBrand,
    fontWeight = fontWeightMedium,
    fontSize = typeScaleCompactWidthScaleUp1,
    lineHeight = typeScaleCompactWidthScaleUp1 * COMPACT_WIDTH_LINE_HEIGHT
)

val compactWidthBody: TextStyle = TextStyle(
    fontFamily = fontFamilyPlain,
    fontWeight = fontWeightRegular,
    fontSize = typeScaleCompactWidthRootSize,
    lineHeight = typeScaleCompactWidthRootSize * COMPACT_WIDTH_LINE_HEIGHT
)

val compactWidthSupportingText: TextStyle = TextStyle(
    fontFamily = fontFamilyPlain,
    fontWeight = fontWeightRegular,
    fontSize = typeScaleCompactWidthScaleDown1,
    lineHeight = typeScaleCompactWidthScaleDown1 * COMPACT_WIDTH_LINE_HEIGHT
)