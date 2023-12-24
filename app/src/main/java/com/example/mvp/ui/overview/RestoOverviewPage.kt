package com.example.mvp.ui.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoOverviewPage(
    navigateToMenu: (String) -> Unit = {},
    restoOverviewViewModel: RestoOverviewViewModel = viewModel(factory = RestoOverviewViewModel.Factory)) {
    val restoOverviewUiState by restoOverviewViewModel.uiState.collectAsState()
    val apiState = restoOverviewUiState.restoOverviewApiState
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Resto Overview") },
                actions = {
                    if (restoOverviewUiState.gridMode) {
                        //TODO: icon button
                        TextButton(onClick = { restoOverviewViewModel.toggleGridMode() }) {
                            Text(text = "List")
                        }
                    } else {
                        TextButton(onClick = { restoOverviewViewModel.toggleGridMode() }) {
                            Text(text = "Grid")
                        }
                    }
                }
            )
        }
    ) {innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (apiState) {
                is RestoOverviewApiState.Loading -> {
                    Text(text = "Loading")
                }

                is RestoOverviewApiState.Success -> {
                    val restoList = apiState.data
                    if (restoOverviewUiState.gridMode) {
                        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                            items(apiState.data.size) { index ->
                                RestoGridTile(name = restoList[index]) {
                                    navigateToMenu(
                                        restoList[index]
                                    )
                                }
                            }
                        }
                    } else {
                        LazyColumn {
                            items(restoList.size) { index ->
                                RestoListTile(name = restoList[index], navigateToMenu = {
                                    navigateToMenu(
                                        restoList[index]
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




}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoGridTile(name: String,
                  navigateToMenu: () -> Unit = {}

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp),
        onClick =  navigateToMenu
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Text(text = name, modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun RestoListTile(name: String,
                  navigateToMenu: () -> Unit = {}
) {
    ListItem(
        headlineContent = { Text(text = name) },
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = navigateToMenu)
    )
}