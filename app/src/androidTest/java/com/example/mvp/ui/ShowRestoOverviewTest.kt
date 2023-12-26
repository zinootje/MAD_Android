package com.example.mvp.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.mvp.Utils.onNodeWithContentDescriptionStringId
import com.example.mvp.ui.Util.GridSize
import com.example.mvp.ui.overview.RestoOverviewApiState
import com.example.mvp.ui.overview.RestoOverviewUiState
import com.example.mvp.ui.overview.ShowRestoOverview
import com.example.mvp.R
import com.example.mvp.model.Resto
import org.junit.Rule
import org.junit.Test
import kotlin.concurrent.thread

class ShowRestoOverviewTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showRestoOverview_Displays_showsLoadingState() {
        composeTestRule.setContent {
            CreateShowRestoOverview(
                restoOverviewUiState = RestoOverviewUiState(
                    RestoOverviewApiState.Loading,
                    false
                )
            )
        }

        composeTestRule.onNodeWithContentDescriptionStringId(R.string.loading_indicator).assertExists()
    }

    @Test
    fun showRestoOverview_Displays_showsErrorState() {

        val error = "error"
        composeTestRule.setContent {
            CreateShowRestoOverview(
                restoOverviewUiState = RestoOverviewUiState(
                    RestoOverviewApiState.Error(error),
                    false
                )
            )
        }
        //at least one node with text error
        composeTestRule.onAllNodesWithText(error).assertAny(hasText(error))
    }

    @Test
    fun showRestoOverview_Displays_showsSuccessState() {
        composeTestRule.setContent {
            CreateShowRestoOverview(
                RestoOverviewUiState(
                    RestoOverviewApiState.Success(
                        listOf(
                            Resto("name", false),
                            Resto("name2", false),
                            Resto("name3", false),

                            )
                    ),
                    false
                ),
            )
        }
        //at least one node with text error
        composeTestRule.onNodeWithText("name").assertExists()
        composeTestRule.onNodeWithText("name2").assertExists()
        composeTestRule.onNodeWithText("name3").assertExists()

        //make sure favorited icon is not shown as this is no favorited resto
        composeTestRule.onNodeWithContentDescriptionStringId(R.string.favorited).assertDoesNotExist()
    }


    @Test
    fun showRestoOverview_Displays_FavoritedResto() {
        composeTestRule.setContent {
            ShowRestoOverview(
                innerPadding = PaddingValues(),
                restoOverviewUiState = RestoOverviewUiState(
                    RestoOverviewApiState.Success(
                        listOf(
                            com.example.mvp.model.Resto("name", true),
                            com.example.mvp.model.Resto("name2", false),
                            com.example.mvp.model.Resto("name3", false),

                            )
                    ),
                    false
                ),
                navigateToMenu = {},
                favoriteResto = { _, _ -> },
                gridSize = GridSize.Fixed
            )
        }
        composeTestRule.onNodeWithText("name").assertExists()
        composeTestRule.onNodeWithText("name2").assertExists()
        composeTestRule.onNodeWithText("name3").assertExists()

        //make sure favorited icon is not shown as this is no favorited resto
        composeTestRule.onNodeWithContentDescriptionStringId(R.string.favorited).assertExists()
    }

    @Test
    fun showRestoOverview_Displays_withFavoritedResto() {
        composeTestRule.setContent {
            ShowRestoOverview(
                innerPadding = PaddingValues(),
                restoOverviewUiState = RestoOverviewUiState(
                    RestoOverviewApiState.Success(
                        listOf(
                            com.example.mvp.model.Resto("name", true),
                            )
                    ),
                    true
                ),
                navigateToMenu = {},
                favoriteResto = { _, _ -> },
                gridSize = GridSize.Fixed
            )
        }
        composeTestRule.onNodeWithText("name").assertExists()


        //make sure favorited icon is show as this is a favorited resto
        composeTestRule.onNodeWithContentDescriptionStringId(R.string.favorited).assertExists()
        //make sure unfavorited icon is not shown as this is a favorited resto
        composeTestRule.onNodeWithContentDescriptionStringId(R.string.unfavorited).assertDoesNotExist()
    }

    @Test
    fun showRestoOverview_Displays_GridModeCorretly() {
        composeTestRule.setContent {
            ShowRestoOverview(
                innerPadding = PaddingValues(),
                restoOverviewUiState = RestoOverviewUiState(
                    RestoOverviewApiState.Success(
                        listOf(
                            com.example.mvp.model.Resto("name", false),
                            com.example.mvp.model.Resto("name2", true),
                        )
                    ),
                    true
                ),
                navigateToMenu = {},
                favoriteResto = { _, _ -> },
                gridSize = GridSize.Fixed
            )
        }
        composeTestRule.onNodeWithContentDescriptionStringId(R.string.resto_overview_grid).assertExists()
        composeTestRule.onNodeWithContentDescriptionStringId(R.string.resto_overview_list).assertDoesNotExist()

        composeTestRule.onNodeWithText("name").assertExists()
        composeTestRule.onNodeWithText("name2").assertExists()

        composeTestRule.onNodeWithContentDescriptionStringId(R.string.favorited).assertExists()
        composeTestRule.onNodeWithContentDescriptionStringId(R.string.unfavorited).assertExists()
    }

    @Test
    fun showRestoOverview_Displays_ListModeCorretly() {
        composeTestRule.setContent {
            ShowRestoOverview(
                innerPadding = PaddingValues(),
                restoOverviewUiState = RestoOverviewUiState(
                    RestoOverviewApiState.Success(
                        listOf(
                            com.example.mvp.model.Resto("name", false),
                            com.example.mvp.model.Resto("name2", true),
                        )
                    ),
                    false
                ),
                navigateToMenu = {},
                favoriteResto = { _, _ -> },
                gridSize = GridSize.Fixed
            )
        }
        composeTestRule.onNodeWithContentDescriptionStringId(R.string.resto_overview_list).assertExists()
        composeTestRule.onNodeWithContentDescriptionStringId(R.string.resto_overview_grid).assertDoesNotExist()

        composeTestRule.onNodeWithText("name").assertExists()
        composeTestRule.onNodeWithText("name2").assertExists()

        composeTestRule.onNodeWithContentDescriptionStringId(R.string.favorited).assertExists()
        composeTestRule.onNodeWithContentDescriptionStringId(R.string.unfavorited).assertExists()
    }





    @Composable
    private fun CreateShowRestoOverview(
        restoOverviewUiState: RestoOverviewUiState
    ) {
        ShowRestoOverview(
            innerPadding = PaddingValues(),
            restoOverviewUiState = restoOverviewUiState,
            navigateToMenu = {},
            favoriteResto = { _, _ -> },
            gridSize = GridSize.Fixed
        )
    }
}