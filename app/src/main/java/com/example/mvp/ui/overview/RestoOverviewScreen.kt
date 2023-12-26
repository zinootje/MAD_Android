@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.mvp.ui.overview

import androidx.annotation.VisibleForTesting
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.mvp.R
import com.example.mvp.ui.Util.GridSize
import com.example.mvp.ui.Util.contentDescription
import com.example.mvp.ui.Util.getColorFromName
import com.example.mvp.ui.common.ErrorComponent
import com.example.mvp.ui.common.LoadingIndicator
import com.theapache64.rebugger.Rebugger

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
                    GriddTogleButton(
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

@Composable
private fun GriddTogleButton(
    isGrid: Boolean,
    toggleGridMode: () -> Unit
) {
    if (isGrid) {
        //TODO: icon button
        TextButton(onClick = { toggleGridMode() }) {
            Text(text = "List")
        }
    } else {
        TextButton(onClick = { toggleGridMode()}) {
            Text(text = "Grid")
        }
    }
}

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
        when (restoOverviewUiState.restoOverviewApiState) {
            is RestoOverviewApiState.Loading -> {
                LoadingIndicator()
            }

            is RestoOverviewApiState.Success -> {
                val restoList = restoOverviewUiState.restoOverviewApiState.data
                if (restoOverviewUiState.gridMode) {
                    LazyVerticalGrid(columns = when (gridSize) {
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

@Composable
fun RestoListTile(
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

@Composable
fun FavoriteButton(
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