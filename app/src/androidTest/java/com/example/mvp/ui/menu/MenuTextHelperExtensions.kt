package com.example.mvp.ui.menu

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.core.model.Day
import com.example.core.model.Special

/**
 * Asserts that the menu is displayed correctly in the UI.
 * Doesn't check for the [Special] indicator.
 *
 * @param menuDay The [Day] object representing the menu for a specific day.
 */
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.assertMenuIsDisplayedCorrectly(menuDay: Day) {


    // Verify that the menu is displayed
    this.onAllNodesWithText(menuDay.dag).assertCountEquals(2)
    this.onNodeWithText(menuDay.message).assertIsDisplayed()

    // Category titles are in all caps
    menuDay.menu.items.forEach { (category, dishes) ->
        this.onNodeWithText(category, ignoreCase = true).assertExists()

        // Check if each dish in the category is displayed
        dishes.forEach { dish ->
            this.onNodeWithText(dish.name).assertExists()
        }
    }


    // Verify that vegan and veggie indicators are displayed
    //this.onNodeWithStringId(R.string.vegan).assertExists()
    //this.onNodeWithStringId(R.string.veggie).assertExists()
}


fun assertMenuIsDisplayedCorrectly(menuDay: Day, node: SemanticsNodeInteraction) {


    node.onChildren().filterToOne(hasText(menuDay.dag)).assertExists()
    node.onChildren().filterToOne(hasText(menuDay.message)).assertExists()
    menuDay.menu.items.forEach { (category, dishes) ->
        node.onChildren().filterToOne(hasText(category, ignoreCase = true)).assertExists()
        dishes.forEach { dish ->
            node.onChildren().filterToOne(hasText(dish.name)).assertExists()
        }
    }


}
