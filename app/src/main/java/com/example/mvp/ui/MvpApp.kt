package com.example.mvp.ui

import android.util.Log
import androidx.annotation.StringRes
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



enum class MvpScreens(@StringRes val title: Int,val arguments: String? = null) {
    Start(title = R.string.app_name),
    RestoOverview(title = R.string.resto_overview_title),
    RestoMenu(title = R.string.resto_menu_title, arguments = "restoName");


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
               composable(MvpScreens.RestoOverview.toRoute()) {
               }
               composable(MvpScreens.RestoMenu.toRoute()) {
                   val restoId = it.arguments?.getString(MvpScreens.RestoMenu.arguments)
                   if (restoId == null) {
                       Log.e("MVPApp", "RestoMenu route called without restoId")
                       navController.popBackStack()
                       return@composable
                   }
                   //TODO fix null safety
                    RestoMenuScreen(viewModel = viewModel(factory= RestoMenuViewmodel.Factory(restoId)), onBack = { navController.popBackStack()
                    }, tabRowType = when(windowSize){
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
                    })
               }
           }
        }
    }
}