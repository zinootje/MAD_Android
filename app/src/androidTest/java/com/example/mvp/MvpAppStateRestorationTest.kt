package com.example.mvp

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.mvp.Utils.setFakeAppContainer
import com.example.mvp.fake.FakeDataSource
import com.example.mvp.ui.MVPApp
import com.example.mvp.ui.Util.TabKey
import org.junit.Rule
import org.junit.Test

class MvpAppStateRestorationTest {


    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun Device_locationRetained_afterConfigChange() {

        composeTestRule.setFakeAppContainer()
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent { MVPApp(windowSize = WindowWidthSizeClass.Compact) }

        //go to first resto menu
        composeTestRule.onNodeWithText("Resto 1").performClick()

        //go to second menu tab
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(
                TabKey,
                FakeDataSource.restoMenu.days[1].dag
            )).performClick()

        //verfify correct location
        composeTestRule.onNodeWithText("Test message day2").assertExists()


        //simulate config change
        stateRestorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithText("Test message day2").assertExists()

        //Wait 10s
        Thread.sleep(10000)



    }



}