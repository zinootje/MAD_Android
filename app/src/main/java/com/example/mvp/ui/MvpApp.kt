package com.example.mvp.ui

import android.util.Log
import androidx.annotation.StringRes
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mvp.R
import com.example.mvp.ui.Util.GridSize
import com.example.mvp.ui.Util.TabRowType
import com.example.mvp.ui.menu.RestoMenuScreen
import com.example.mvp.ui.menu.RestoMenuViewmodel
import com.example.mvp.ui.overview.RestoOverviewScreen
import com.example.mvp.ui.overview.RestoOverviewViewModel
import com.example.mvp.ui.theme.MVPTheme


/**
 * Enumeration class representing the screens in the MVP app.
 *
 * @param title The string resource ID for the screen title.
 * @param arguments Optional string value representing the screen argument.
 *
 *
 */
enum class MvpScreens(@StringRes val title: Int, val arguments: String? = null) {
    /**
     * This class represents the Start screen in the MVP app.
     *
     * @param title The string resource ID for the screen title.
     */
    Start(title = R.string.app_name),

    /**
     * Represents a restaurant menu in the MVP app.
     *
     * @property title The title of the menu, retrieved from a string resource ID.
     * @property arguments Optional string value representing the menu argument.
     */
    RestoMenu(title = R.string.resto_menu_title, arguments = "restoName");


    /**
     * Converts the enumeration class representing the screens in the MVP app to a route string.
     *
     * @return The route string corresponding to the screen.
     */
    fun toRoute(): String = buildString {
        append(name)
        arguments?.let { append("/{$it}") }
    }

    //TODO maybe otherways
    fun toRouteWithArgument(argument: String): String = buildString {
        append(name)
        append("/$argument")
    }


}


/**
 * Composable function representing the MVPApp.
 *
 * @param navController The instance of [NavHostController] used for navigation.
 * @param windowSize The [WindowWidthSizeClass] representing the size of the window.
 */
@Composable
fun MVPApp(navController: NavHostController = rememberNavController(),windowSize: WindowWidthSizeClass){


    val backStackEntry = navController.currentBackStackEntryAsState()

    //TODO type safe navigation

    fun navigateToMenu(restoName: String) {
        //TODO fix null safety
        navController.navigate(MvpScreens.RestoMenu.toRoute().replace("{${MvpScreens.RestoMenu.arguments!!}}", restoName))
    }

    MVPTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
           NavHost(navController = navController, startDestination = MvpScreens.Start.name) {
               composable(MvpScreens.Start.toRoute()) {
                    RestoOverviewScreen(navigateToMenu = ::navigateToMenu, restoOverviewViewModel = viewModel(factory = RestoOverviewViewModel.Factory), gridSize = when(windowSize){
                        WindowWidthSizeClass.Compact -> {
                            GridSize.Fixed
                        }
                        WindowWidthSizeClass.Medium -> {
                            GridSize.Fixed
                        }
                        WindowWidthSizeClass.Expanded -> {
                            GridSize.Fixed
                        }

                        else -> {
                            GridSize.Fixed
                        }
                    })
               }
               composable(MvpScreens.RestoMenu.toRoute(),
                   enterTransition = {
                       //slide in from the right
                       slideInHorizontally(
                           //slide in from the right
                           initialOffsetX = { it / 2 }
                       )
                   },
                   exitTransition = {
                       //slide out to the left
                       slideOutHorizontally(

                           //slide out to the left
                           targetOffsetX = { it },
                       )
                   }
               ) {
                   val restoId = it.arguments?.getString(MvpScreens.RestoMenu.arguments)
                   val tabRowType = when (windowSize) {
                       WindowWidthSizeClass.Compact -> {
                           TabRowType.Scrollable
                       }

                       WindowWidthSizeClass.Medium -> {
                           TabRowType.Scrollable
                       }

                       WindowWidthSizeClass.Expanded -> {
                           TabRowType.Expanded
                       }

                       else -> {
                           TabRowType.Scrollable
                       }
                   }
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