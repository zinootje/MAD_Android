package com.example.mvp.integration

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.core.StaleAbleData
import com.example.mvp.R
import com.example.mvp.ui.MVPApp
import com.example.mvp.ui.menu.assertMenuIsDisplayedCorrectly
import com.example.mvp.ui.util.TabKey
import com.example.mvp.utils.assertNodeCountIsNotZero
import com.example.mvp.utils.onNodeWithContentDescriptionStringId
import com.example.mvp.utils.setFakeAppContainer
import com.example.network.asDomainObject
import com.example.testutils.fake.FakeAppContainer
import com.example.testutils.fake.FakeDataSource
import com.example.testutils.fake.TestRestoRepository
import org.junit.Rule
import org.junit.Test


class ViewingMenuTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()


    /**
     * Check happy flow of the app with a fake repository
     * Navigates to a menu and ensures that it is displayed correctly. switches to another day and checks if it is displayed correctly
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    //TODO name
    fun happyPath_navigateToMenuAndDisplayCorrectly() {
        val restoRepository = TestRestoRepository()
        val fakeAppContainer = FakeAppContainer(restoRepository = restoRepository)
        composeTestRule.setFakeAppContainer(fakeAppContainer)
        composeTestRule.setContent {
            MVPApp(windowSize = WindowWidthSizeClass.Compact)
        }

        composeTestRule.onNodeWithContentDescriptionStringId(R.string.loading_indicator).assertExists()

        val restos = FakeDataSource.restoObjectList
        restoRepository.sendRestoList(restos)
        //Wait for loading to be done
        //TODO maybe use a better way to wait for loading to be done
        composeTestRule.waitUntilDoesNotExist(
            hasContentDescription(
                composeTestRule.activity.getString(
                    R.string.loading_indicator
                )
            ), 2_000L
        )
        composeTestRule.onNodeWithContentDescriptionStringId(R.string.loading_indicator).assertDoesNotExist()
        for (resto in restos) {
            composeTestRule.onNodeWithText(resto.name).assertExists()
        }
        val selectedRestaurant = restos[1]
        composeTestRule.onNodeWithText(selectedRestaurant.name).performClick()

        composeTestRule.onNodeWithContentDescriptionStringId(R.string.loading_indicator).assertExists()
        composeTestRule.onAllNodesWithText(selectedRestaurant.name, substring = true, ignoreCase = true)
            .assertNodeCountIsNotZero()
        restoRepository.sendRestoMenu(
            selectedRestaurant.name,
            StaleAbleData(FakeDataSource.restoMenu.asDomainObject(), false)
        )
        composeTestRule.onNodeWithContentDescriptionStringId(R.string.loading_indicator).assertDoesNotExist()


        composeTestRule.assertMenuIsDisplayedCorrectly(FakeDataSource.restoMenu.asDomainObject().days[0])


        //switch to another day
        val day2 = FakeDataSource.restoMenu.asDomainObject().days[1]
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(
                TabKey,
                day2.dag
            )
        ).performClick()

        //get second page of menu and check if it is displayed correctly
        //just asserting existence of the menu can give error if there are similar items in the menu and the menu of screen because they both exist

        val page = composeTestRule.onNodeWithTag("page-1")
        //assert page is displayed
        page.assertIsDisplayed()
        //assert menu is displayed correctly
        assertMenuIsDisplayedCorrectly(day2, page)

    }

    companion object {
    }

}