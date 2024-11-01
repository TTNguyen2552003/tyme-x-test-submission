package app.kotlin.currencyconverter.ui.components

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.kotlin.currencyconverter.ui.styles.shapeMedium

@Composable
fun CurrencyDropdownMenu(
    isDarkTheme: Boolean = false,
    onDismissRequest: () -> Unit,
    currencyUnits: List<String>,
    updateCurrencyUnit: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        LazyColumn(
            modifier = Modifier
                .width(width = 200.dp)
                .heightIn(max = 280.dp)
                .clip(shape = RoundedCornerShape(size = shapeMedium))
        ) {
            items(currencyUnits.size) { index ->
                CurrencyDropdownMenuItem(
                    isDarkTheme = isDarkTheme,
                    unit = currencyUnits[index],
                    onTapped = {
                        updateCurrencyUnit(currencyUnits[index])
                        onDismissRequest()
                    }
                )
            }
        }
    }
}