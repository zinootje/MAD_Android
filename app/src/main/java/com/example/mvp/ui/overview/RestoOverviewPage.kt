@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.mvp.ui.overview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star as FilledStar
import androidx.compose.material.icons.outlined.Star as OutlinedStar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mvp.ui.Util.getColorFromName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoOverviewPage(
    navigateToMenu: (String) -> Unit = {},
    restoOverviewViewModel: RestoOverviewViewModel
) {
    val restoOverviewUiState by restoOverviewViewModel.uiState.collectAsState()

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
        ShowRestoOverview(innerPadding, restoOverviewUiState, restoOverviewViewModel, navigateToMenu)

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
private fun ShowRestoOverview(
    innerPadding: PaddingValues,
    restoOverviewUiState: RestoOverviewUiState,
    restoOverviewViewModel: RestoOverviewViewModel,
    navigateToMenu: (String) -> Unit
) {
    Box(modifier = Modifier.padding(innerPadding)) {
        when (restoOverviewUiState.restoOverviewApiState) {
            is RestoOverviewApiState.Loading -> {
                Text(text = "Loading")
            }

            is RestoOverviewApiState.Success -> {
                val restoList = (restoOverviewUiState.restoOverviewApiState as RestoOverviewApiState.Success).data
                if (restoOverviewUiState.gridMode) {
                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        items(
                            restoList.size,
                            key = { index -> restoList[index].name },
                        ) { index ->
                            RestoGridTile(
                                modifier = Modifier.animateItemPlacement(),
                                name = restoList[index].name,
                                isFavorite = restoList[index].isFavorite,
                                onFavoriteClick = {
                                    restoOverviewViewModel.setFavorite(restoList[index].name, it)
                                },
                                navigateToMenu = {
                                    navigateToMenu(restoList[index].name)
                                }
                            )
                        }
                    }
                } else {
                    LazyColumn {
                        items(
                            restoList.size,
                            key = { index -> restoList[index].name },
                        ) { index ->
                            RestoListTile(
                                modifier = Modifier.animateItemPlacement(),
                                name = restoList[index].name,
                                isFavorite = restoList[index].isFavorite,
                                navigateToMenu = {
                                    navigateToMenu(restoList[index].name)
                                },
                                favoriteResto = {
                                    restoOverviewViewModel.setFavorite(
                                        restoList[index].name,
                                        !restoList[index].isFavorite
                                    )
                                })
                        }
                    }
                }
            }

            is RestoOverviewApiState.Error -> {
                Text(text = "Error")
            }
        }


    }
}


@Composable
fun RestoGridTile(
    modifier: Modifier = Modifier,
    name: String,
    isFavorite: Boolean,
    navigateToMenu: () -> Unit = {},
    onFavoriteClick: (Boolean) -> Unit
) {
    val initial = name.first().uppercase()
    Card(
        modifier = modifier
            .padding(8.dp).size(200.dp, 200.dp),
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
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isFavorite) "Unfavorite" else "Favorite",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clickable { onFavoriteClick(!isFavorite) },
                tint = if (isFavorite) Color.Red else Color.Gray
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
    ListItem(
        headlineContent = { Text(text = name) },
        trailingContent = {
            Icon(imageVector = if (isFavorite) Icons.Filled.FilledStar else Icons.Outlined.OutlinedStar,
                contentDescription = if (isFavorite) "Star Icon filled" else "Star Icon border",
                tint = if (isFavorite) Color.Yellow else Color.Gray,
                modifier = Modifier.clickable { favoriteResto() })
        },
        modifier = modifier
            .padding(8.dp)
            .clickable(onClick = navigateToMenu)
    )
}