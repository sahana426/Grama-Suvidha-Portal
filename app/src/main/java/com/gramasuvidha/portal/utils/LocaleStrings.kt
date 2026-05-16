package com.gramasuvidha.portal.utils

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
fun localized(@StringRes id: Int, languageTag: String): String {
    val context = LocalContext.current
    return remember(id, languageTag) {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale.forLanguageTag(languageTag))
        context.createConfigurationContext(configuration).getString(id)
    }
}
