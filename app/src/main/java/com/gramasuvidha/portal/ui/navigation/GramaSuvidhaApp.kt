package com.gramasuvidha.portal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gramasuvidha.portal.GramaSuvidhaApplication
import com.gramasuvidha.portal.ui.screens.HomeScreen
import com.gramasuvidha.portal.ui.screens.ProjectDetailsScreen
import com.gramasuvidha.portal.ui.screens.SplashScreen
import com.gramasuvidha.portal.viewmodel.HomeViewModel
import com.gramasuvidha.portal.viewmodel.ProjectDetailsViewModel

@Composable
fun GramaSuvidhaApp() {
    val navController = rememberNavController()
    val repository = (LocalContext.current.applicationContext as GramaSuvidhaApplication).projectRepository

    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable(Routes.Splash) {
            SplashScreen(onTimeout = {
                navController.navigate(Routes.Home) { popUpTo(Routes.Splash) { inclusive = true } }
            })
        }
        composable(Routes.Home) {
            val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory(repository))
            HomeScreen(viewModel = viewModel, onProjectClick = { navController.navigate(Routes.details(it)) })
        }
        composable(
            route = Routes.Details,
            arguments = listOf(navArgument("projectId") { type = NavType.IntType })
        ) { entry ->
            val projectId = entry.arguments?.getInt("projectId") ?: return@composable
            val viewModel: ProjectDetailsViewModel = viewModel(factory = ProjectDetailsViewModel.Factory(repository, projectId))
            ProjectDetailsScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
    }
}
