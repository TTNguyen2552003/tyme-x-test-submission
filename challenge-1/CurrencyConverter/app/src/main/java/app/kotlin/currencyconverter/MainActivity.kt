package app.kotlin.currencyconverter

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import app.kotlin.currencyconverter.ui.navigation.CurrencyConverterNavigation

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyConverterNavigation()
        }
    }
}