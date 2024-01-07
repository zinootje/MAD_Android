package com.example.mvp.ui

import android.util.Log
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvp.ui.menu.RestoMenuScreen
import com.example.mvp.ui.menu.RestoMenuViewmodel
import com.example.mvp.ui.navigation.MvpScreens
import com.example.mvp.ui.overview.RestoOverviewScreen
import com.example.mvp.ui.overview.RestoOverviewViewModel
import com.example.mvp.ui.theme.MVPTheme
import com.example.mvp.ui.util.GridSize
import com.example.mvp.ui.util.TabRowType


/**
 * Composable function representing the MVPApp.
 *
 * @param navController The instance of [NavHostController] used for navigation.
 * @param windowSize The [WindowWidthSizeClass] representing the size of the window.
 */
@Composable
fun MVPApp(navController: NavHostController = rememberNavController(), windowSize: WindowWidthSizeClass) {


    //val backStackEntry = navController.currentBackStackEntryAsState()

    //TODO type safe navigation

    fun navigateToMenu(restoName: String) {
        //TODO fix null safety
        navController.navigate(
            MvpScreens.RestoMenu.toRoute().replace("{${MvpScreens.RestoMenu.arguments!!}}", restoName)
        )
    }


    val tabRowType = when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            TabRowType.Scrollable
        }

        WindowWidthSizeClass.Medium -> {
            TabRowType.Expanded
        }

        WindowWidthSizeClass.Expanded -> {
            TabRowType.Expanded
        }

        else -> {
            TabRowType.Scrollable
        }


    }

    val gridSize = when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            GridSize.Fixed
        }

        WindowWidthSizeClass.Medium -> {
            GridSize.Adaptive
        }

        WindowWidthSizeClass.Expanded -> {
            GridSize.Adaptive
        }

        else -> {
            GridSize.Adaptive
        }
    }

    MVPTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(navController = navController, startDestination = MvpScreens.Start.name) {
                composable(MvpScreens.Start.toRoute()) {
                    RestoOverviewScreen(
                        navigateToMenu = ::navigateToMenu,
                        restoOverviewViewModel = viewModel(factory = RestoOverviewViewModel.Factory),
                        gridSize = gridSize
                    )
                }

                composable(
                    MvpScreens.RestoMenu.toRoute(),
                    enterTransition = {
                        //slide in from the right
                        slideInHorizontally(
                            initialOffsetX = { it / 2 }
                        )
                    },
                    exitTransition = {
                        //slide out to the right
                        slideOutHorizontally(
                            targetOffsetX = { it },
                        )
                    }
                ) {
                    val restoId = it.arguments?.getString(MvpScreens.RestoMenu.arguments)

                    if (restoId == null) {
                        Log.e("MVPApp", "RestoMenu route called without restoId")
                        navController.popBackStack()
                        return@composable
                    }
                    RestoMenuScreen(
                        viewModel = viewModel<RestoMenuViewmodel>(factory = RestoMenuViewmodel.Factory(restoId)),
                        onBack = { navController.popBackStack() },
                        tabRowType = tabRowType
                    )
                }
            }
        }
    }
}