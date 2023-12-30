package com.example.mvp

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.mvp.fake.FakeDataSource
import com.example.mvp.ui.MVPApp
import com.example.mvp.ui.MvpScreens
import com.example.mvp.utils.assertCurrentRouteName
import com.example.mvp.utils.setFakeAppContainer
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MvpNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupCupcakeNavHost() {
        composeTestRule.setFakeAppContainer()
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            MVPApp(navController, windowSize = WindowWidthSizeClass.Compact)

        }
    }


    @Test
    fun MvpNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(MvpScreens.Start.toRoute())
    }

    @Test
    fun MvpNavHost_navigateToMenuPage() {
        val firstRestoName = FakeDataSource.restoObjectList.first().name
        composeTestRule.onAllNodesWithText(firstRestoName)[0].performClick()
        navController.assertCurrentRouteName(MvpScreens.RestoMenu.toRoute())
    }
}