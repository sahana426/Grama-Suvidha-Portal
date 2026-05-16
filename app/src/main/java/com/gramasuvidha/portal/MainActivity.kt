package com.gramasuvidha.portal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gramasuvidha.portal.ui.navigation.GramaSuvidhaApp
import com.gramasuvidha.portal.ui.theme.GramaSuvidhaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GramaSuvidhaTheme {
                GramaSuvidhaApp()
            }
        }
    }
}
