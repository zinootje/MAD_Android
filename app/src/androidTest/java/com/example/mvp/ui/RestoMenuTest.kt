package com.example.mvp.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.core.model.*
import com.example.mvp.R
import com.example.mvp.ui.Util.TabKey
import com.example.mvp.ui.Util.TabRowType
import com.example.mvp.ui.menu.RestoMenu
import com.example.mvp.ui.menu.RestoMenuApiState
import com.example.mvp.utils.onNodeWithContentDescriptionStringId
import com.example.mvp.utils.onNodeWithStringId
import org.junit.Rule
import org.junit.Test

class RestoMenuTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun restoMenu_Displays_showsLoadingState() {
        composeTestRule.setContent {
            CreateRestoMenu(
                apiState = RestoMenuApiState.Loading

            )
        }

        composeTestRule.onNodeWithContentDescriptionStringId(R.string.loading_indicator).assertExists()
    }

    @Test
    fun restoMenu_Displays_showsErrorState() {
        val error = "errorText"
        composeTestRule.setContent {
            CreateRestoMenu(
                apiState = RestoMenuApiState.Error(error)
            )
        }

        composeTestRule.onAllNodesWithText(error).assertAny(hasText(error))
    }

    @Test
    fun restoMenu_Displays_showsSuccessState() {
        // TODO: Replace with actual data
        val menuData = MenuData(
            location = "location",
            days = listOf(
                Day(
                    dag = "dayName",
                    message = "message",
                    menu = Menu(
                        items = mapOf(
                            "Breakfast" to listOf(
                                Dish(
                                    name = "Bread",
                                    Special.NONE
                                ),
                                Dish(
                                    name = "Cereal",
                                    Special.VEGAN
                                )
                            ),
                            "Drinks"
                                    to listOf(
                                Dish(
                                    name = "Coffee",
                                    Special.NONE
                                ),
                                Dish(
                                    name = "Tea",
                                    Special.VEGIE
                                )
                            )
                        )
                    )

                )
            )
        )
        composeTestRule.setContent {
            CreateRestoMenu(
                apiState = RestoMenuApiState.Success(menuData)
            )
        }

        //verify that the menu is displayed

        //is displayed on appbar
        //composeTestRule.onNodeWithText("location").assertExists()

        //There exist two on in a tab on int the menu
        composeTestRule.onAllNodesWithText("dayName").assertCountEquals(2)
        composeTestRule.onNodeWithText("message").assertExists()
        //Category titles are in all caps
        composeTestRule.onNodeWithText("Breakfast", ignoreCase = true).assertExists()
        composeTestRule.onNodeWithText("Bread").assertExists()
        composeTestRule.onNodeWithText("Cereal").assertExists()
        composeTestRule.onNodeWithText("Drinks", ignoreCase = true).assertExists()
        composeTestRule.onNodeWithText("Coffee").assertExists()
        composeTestRule.onNodeWithText("Tea").assertExists()

        //veriffy that vegan en veggie indicators are displayed
        composeTestRule.onNodeWithStringId(R.string.vegan).assertExists()
        composeTestRule.onNodeWithStringId(R.string.veggie).assertExists()

    }

    @Test
    fun restoMenu_switch_showsCorrectTab() {

        composeTestRule.setContent {
            CreateRestoMenu(
                apiState = RestoMenuApiState.Success(
                    MenuData(
                location = "location",
                days = listOf(
                    //showed on first tab
                    Day(
                        dag = "dayName",
                        message = "messageOfDay1",
                        menu = Menu(
                            items = mapOf(
                                "Breakfast" to listOf(
                                    Dish(
                                        name = "Bread",
                                        Special.NONE
                                    ),
                                    Dish(
                                        name = "Cereal",
                                        Special.VEGAN
                                    )
                                ),
                                "Drinks"
                                        to listOf(
                                    Dish(
                                        name = "Coffee",
                                        Special.NONE
                                    ),
                                    Dish(
                                        name = "Tea",
                                        Special.VEGIE
                                    )
                                )
                            )
                        )

                    )
                    //showed on second tab
                    , Day(
                        dag = "dayName2",
                        message = "message2",
                        menu = Menu(
                            items = mapOf(
                                "Breakfast2" to listOf(
                                    Dish(
                                        name = "Bread2",
                                        Special.NONE
                                    ),
                                    Dish(
                                        name = "Cereal2",
                                        Special.VEGAN
                                    )
                                ),
                                "Drinks2"
                                        to listOf(
                                    Dish(
                                        name = "Coffee2",
                                        Special.NONE
                                    ),
                                    Dish(
                                        name = "Tea2",
                                        Special.VEGIE
                                    )
                                )
                            )
                        )

                    )
                )
                    )
                )
            )
        }

        //simple check if the first tab is displayed
        composeTestRule.onNodeWithText("messageOfDay1").assertExists()

        //switch to seccond tab
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(
                TabKey,
                "dayName2"
            )).performClick()

        //check if the second tab is displayed
        composeTestRule.onNodeWithText("message2").assertExists()
        composeTestRule.onNodeWithText("Breakfast2", ignoreCase = true).assertExists()
        composeTestRule.onNodeWithText("Bread2").assertExists()
        composeTestRule.onNodeWithText("Cereal2").assertExists()
        composeTestRule.onNodeWithText("Drinks2", ignoreCase = true).assertExists()
        composeTestRule.onNodeWithText("Coffee2").assertExists()
        composeTestRule.onNodeWithText("Tea2").assertExists()

        //assert that first is not displayed
        //ignoring case better to sensitive
        composeTestRule.onNodeWithText("messageOfDay1", ignoreCase = true).assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Breakfast", ignoreCase = true).assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Bread", ignoreCase = true).assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Cereal", ignoreCase = true).assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Coffee", ignoreCase = true).assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Tea", ignoreCase = true).assertIsNotDisplayed()

    }

    @Composable
    private fun CreateRestoMenu(
        apiState: RestoMenuApiState
    ) {
        RestoMenu(
            apiState = apiState,
            tabRowType = TabRowType.Expanded
        )
    }
}