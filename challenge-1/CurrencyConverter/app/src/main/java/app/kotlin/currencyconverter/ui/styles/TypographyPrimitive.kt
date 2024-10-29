package app.kotlin.currencyconverter.ui.styles

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import app.kotlin.currencyconverter.R

private val interFamily: FontFamily = FontFamily(
    Font(resId = R.font.inter_regular, weight = FontWeight.Normal),
    Font(resId = R.font.inter_medium, weight = FontWeight.Medium),
    Font(resId = R.font.inter_semi_bold, weight = FontWeight.SemiBold)
)

val fontFamilyBrand: FontFamily = interFamily
val fontFamilyPlain: FontFamily = interFamily

val fontWeightRegular: FontWeight = FontWeight.Normal
val fontWeightMedium: FontWeight = FontWeight.Normal
val fontWeightSemiBold: FontWeight = FontWeight.SemiBold

const val COMPACT_WIDTH_LINE_HEIGHT = 1.2
val typeScaleCompactWidthRootSize: TextUnit = 14.sp
val typeScaleCompactWidthScaleUp1: TextUnit = 16.sp
val typeScaleCompactWidthScaleUp2: TextUnit = 18.sp
val typeScaleCompactWidthScaleUp3: TextUnit = 25.sp
val typeScaleCompactWidthScaleUp4: TextUnit = 28.sp