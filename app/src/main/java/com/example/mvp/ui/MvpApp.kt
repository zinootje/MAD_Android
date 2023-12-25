package com.example.mvp.ui

import android.text.Spannable.Factory
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mvp.R
import com.example.mvp.ui.menu.RestoMenuPage
import com.example.mvp.ui.menu.RestoMenuViewmodel
import com.example.mvp.ui.overview.RestoOverviewPage
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


}


@Composable
fun MVPApp(navController: NavHostController = rememberNavController()) {


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
                    RestoOverviewPage(navigateToMenu = ::navigateToMenu, restoOverviewViewModel = viewModel(factory = RestoOverviewViewModel.Factory))
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
                    RestoMenuPage(viewModel = viewModel(factory= RestoMenuViewmodel.Factory(restoId)), onBack = { navController.popBackStack()
                    })
               }
           }
        }
    }
}