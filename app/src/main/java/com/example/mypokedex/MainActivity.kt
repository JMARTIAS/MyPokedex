package com.example.mypokedex

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.mypokedex.presentation.LanguageViewModel
import com.example.mypokedex.presentation.navigation.NavGraph
import com.example.mypokedex.ui.theme.MyPokedexTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val languageViewModel: LanguageViewModel by viewModels()
    private var currentLanguage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyPokedex)

        setContent {
            val language by languageViewModel.language.collectAsState()
            val activity = LocalContext.current as Activity

            if (currentLanguage == null) {
                currentLanguage = language
            }

            LaunchedEffect(language) {
                if (currentLanguage != language) {
                    currentLanguage = language
                    activity.recreate()
                }
            }

            // Set the locale for the current composition
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources = activity.resources
            val configuration = resources.configuration
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)

            MyPokedexTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
