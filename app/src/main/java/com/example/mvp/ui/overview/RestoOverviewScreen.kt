@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.mvp.ui.overview

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.core.model.Resto
import com.example.mvp.R
import com.example.mvp.ui.common.ErrorComponent
import com.example.mvp.ui.common.LoadingIndicator
import com.example.mvp.ui.util.GridSize
import com.example.mvp.ui.util.PreviewScreenSizes
import com.example.mvp.ui.util.contentDescription
import com.example.mvp.ui.util.getColorFromName
import com.theapache64.rebugger.Rebugger

/**
 * Composable function that displays the restaurant overview screen.
 *
 * @param navigateToMenu a function that takes a [String] parameter and does not return anything. It is used to navigate to the menu screen of a specific restaurant.
 * @param restoOverviewViewModel an instance of [RestoOverviewViewModel] that contains the necessary data for rendering the screen.
 * @param gridSize a [GridSize] enum value that determines the size of the grid used to display the restaurant tiles.
 *
 * @see RestoOverviewViewModel
 * @see GridSize
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoOverviewScreen(
    navigateToMenu: (String) -> Unit = {},
    restoOverviewViewModel: RestoOverviewViewModel,
    gridSize: GridSize = GridSize.Fixed
) {
    val restoOverviewUiState by restoOverviewViewModel.uiState.collectAsState()


    //Makes function stable to avoid recomposition
    val fav = remember {
        { name: String, isFavorite: Boolean ->
            restoOverviewViewModel.setFavorite(name, isFavorite)
        }
    }

    val nav = remember {
        { name: String ->
            navigateToMenu(name)
        }
    }

    val gridMode by remember {
        derivedStateOf {
            restoOverviewUiState.gridMode
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Resto Overview") },
                actions = {
                    GridToggleButton(
                        isGrid = gridMode,
                        toggleGridMode = { restoOverviewViewModel.toggleGridMode() }
                    )
                }
            )
        }
    ) { innerPadding ->
        ShowRestoOverview(innerPadding, restoOverviewUiState, nav,fav, gridSize)

    }


}

/**
 * Composable function that renders a toggle button for switching between grid and list view.
 *
 * @param isGrid a boolean indicating whether the current mode is grid view.
 * @param toggleGridMode a callback function that is called when the toggle button is clicked.
 */
@Composable
private fun GridToggleButton(
    isGrid: Boolean,
    toggleGridMode: () -> Unit
) {
    if (isGrid) {
        //TODO: icon button
        TextButton(onClick = { toggleGridMode() }) {
            Text(text = stringResource(R.string.list))
        }
    } else {
        TextButton(onClick = { toggleGridMode()}) {
            Text(text = stringResource(R.string.grid))
        }
    }
}

/**
 * Displays the restaurant overview screen.
 *
 * @param innerPadding The padding applied to the content inside the screen.
 * @param restoOverviewUiState The state of the restaurant overview UI, including the API state and grid mode.
 * @param navigateToMenu The callback function to navigate to the menu screen of a restaurant.
 * @param favoriteResto The callback function to mark a restaurant as favorite or remove it from favorites.
 * @param gridSize The size of the grid for the restaurant list.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
//private except for testing
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun ShowRestoOverview(
    innerPadding: PaddingValues,
    restoOverviewUiState: RestoOverviewUiState,
    navigateToMenu: (String) -> Unit,
    favoriteResto: (String, Boolean) -> Unit,
    gridSize: GridSize
) {
    Box(modifier = Modifier.padding(innerPadding)) {
        AnimatedContent(
            targetState = restoOverviewUiState,
            contentKey = {
                if (it.restoOverviewApiState is RestoOverviewApiState.Success) {
                    return@AnimatedContent it.gridMode
                } else {
                    return@AnimatedContent it
                }
            },
            transitionSpec = {
                if (targetState.gridMode) {
                    fadeIn() togetherWith fadeOut()
                } else {
                    fadeIn() togetherWith fadeOut()
                }
            }, label = ""
        ) { restoOverviewUiState ->
            when (restoOverviewUiState.restoOverviewApiState) {
                is RestoOverviewApiState.Loading -> {
                    LoadingIndicator()
                }

                is RestoOverviewApiState.Success -> {
                    val restoList = restoOverviewUiState.restoOverviewApiState.data
                    if (restoOverviewUiState.gridMode) {
                        LazyVerticalGrid(
                            columns = when (gridSize) {
                                GridSize.Fixed -> GridCells.Fixed(2)
                                GridSize.Adaptive -> GridCells.Adaptive(100.dp)
                            },
                            modifier = Modifier.contentDescription(
                                stringResource(
                                    id = R.string.resto_overview_grid
                                )
                            )
                        ) {
                            items(
                                restoList,
                                key = { resto -> resto.name }
                            ) { (name, isFavorite) ->
                                //Avoids recomposition due to animation
                                val modifier = remember {
                                    Modifier.animateItemPlacement()
                                }
                                RestoGridTile(
                                    modifier = modifier,
                                    name = name,
                                    isFavorite = isFavorite,
                                    navigateToMenu = {
                                        navigateToMenu(name)
                                    },
                                    onFavoriteClick = {
                                        favoriteResto(name, !isFavorite)
                                    })
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.contentDescription(
                                stringResource(
                                    id = R.string.resto_overview_list
                                )
                            )
                        ) {
                            items(
                                restoList,
                                key = { resto -> resto.name }
                            ) { (name, isFavorite) ->
                                //Avoids recomposition due to animation
                                val modifier = remember {
                                    Modifier.animateItemPlacement()
                                }
                                RestoListTile(
                                    modifier = modifier,
                                    name = name,
                                    isFavorite = isFavorite,
                                    navigateToMenu = {
                                        navigateToMenu(name)
                                    },
                                    favoriteResto = {
                                        favoriteResto(name, !isFavorite)
                                    })
                            }
                        }
                    }
                }

                is RestoOverviewApiState.Error -> {
                    ErrorComponent(message = restoOverviewUiState.restoOverviewApiState.message)
                }
            }
        }

    }
}


/**
 * Composable function that displays a tile representing a restaurant in a grid layout.
 *
 * @param modifier The modifier to be applied to the tile.
 * @param name The name of the restaurant.
 * @param isFavorite Whether the restaurant is marked as a favorite.
 * @param navigateToMenu The callback function to navigate to the menu screen of the restaurant.
 * @param onFavoriteClick The callback function to mark the restaurant as a favorite or remove it from favorites.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoGridTile(
    modifier: Modifier = Modifier,
    name: String,
    isFavorite: Boolean,
    navigateToMenu: () -> Unit = {},
    onFavoriteClick: () -> Unit
) {
    val initial = name.first().uppercase()
    Card(
        modifier = modifier
            .padding(8.dp)
            .aspectRatio(1f),
        elevation = CardDefaults.elevatedCardElevation(),
        onClick = { navigateToMenu() }
        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = initial,
                fontSize = 94.sp,
                fontWeight = FontWeight.Bold,
                color = getColorFromName(name),
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Text(
                text = name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = initial.length * 24.dp)
            )
            FavoriteButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                isFavorite = isFavorite,
                favoriteResto = onFavoriteClick
            )
        }
    }
}

/**
 * Composable function that displays a restaurant list tile.
 *
 * @param modifier The modifier to be applied to the tile.
 * @param name The name of the restaurant.
 * @param isFavorite Whether the restaurant is marked as a favorite. Default value is false.
 * @param navigateToMenu The callback function to navigate to the menu screen of the restaurant. Default value is an empty function.
 * @param favoriteResto The callback function to mark the restaurant as a favorite or remove it from favorites. Default value is an empty function.
 */
@Composable
private fun RestoListTile(
    modifier: Modifier = Modifier,
    name: String,
    isFavorite: Boolean = false,
    navigateToMenu: () -> Unit = {},
    favoriteResto: () -> Unit = {}
) {

    Rebugger(trackMap = mapOf("modifier" to modifier,
        "name" to name,
        "isFavorite" to isFavorite,
        "navigateToMenu" to navigateToMenu,
        "favoriteResto" to favoriteResto))

    ListItem(
        headlineContent = { Text(text = name) },
        trailingContent = {
            FavoriteButton(isFavorite = isFavorite, favoriteResto = favoriteResto)
        },
        modifier = modifier
            .padding(8.dp)
            .clickable(onClick = { navigateToMenu() })
    )
}

/**
 * Composable function that displays a favorite button.
 *
 * @param modifier The modifier to be applied to the button.
 * @param isFavorite Whether the button is in a favorited state. Default value is false.
 * @param favoriteResto The callback function to be called when the button is clicked. Default value is an empty function.
 */
@Composable
private fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    favoriteResto: () -> Unit = {}
) {
    IconButton(onClick = favoriteResto, modifier = modifier) {
        Icon(imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (isFavorite) stringResource(id = R.string.favorited) else stringResource(id = R.string.unfavorited),
            tint = if (isFavorite) Color.Yellow else Color.Gray)

    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun RestoOverviewPagePreview() {
//    val restoOverviewViewModel = RestoOverviewViewModel(FakeRestoRepository())
//    RestoOverviewPage(restoOverviewViewModel = restoOverviewViewModel)
//}


@PreviewScreenSizes
@Composable
private fun ShowRestoOverviewPreview() {

    ShowRestoOverview(
        innerPadding = PaddingValues(),
        restoOverviewUiState = RestoOverviewUiState(
            gridMode = false,
            restoOverviewApiState = RestoOverviewApiState.Success(
                data = listOf(
                    Resto("Restaurant 1", false),
                    Resto("Restaurant 2", true),
                    Resto("Restaurant 3", false),
                    Resto("D 4", true),
                )
            )
        ),
        navigateToMenu = {/*navigate to menu*/ },
        favoriteResto = { _, _ -> run {} },
        gridSize = GridSize.Fixed
    )
}

@Preview(showBackground = true)
@Composable
private fun RestoGridTilePreview() {
    RestoGridTile(
        name = "Restaurant",
        isFavorite = true,
        navigateToMenu = {/*navigate to menu*/ },
        onFavoriteClick = {/*favorite restaurant*/ })
}

@Preview
@Composable
private fun RestoListTilePreview() {
    RestoListTile(
        name = "Restaurant",
        isFavorite = true,
        navigateToMenu = { /*navigate to menu*/ },
        favoriteResto = {/*favorite restaurant*/ })
}

@Preview(showBackground = true)
@Composable
private fun FavoriteButtonPreview() {
    FavoriteButton(isFavorite = false, favoriteResto = { /*favorite restaurant*/ })
}
