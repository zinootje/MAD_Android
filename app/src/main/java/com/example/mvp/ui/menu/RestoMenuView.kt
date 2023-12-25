@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mvp.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvp.model.Day
import com.example.mvp.model.Dish
import com.example.mvp.model.MenuData
import com.example.mvp.model.special

@Composable
fun RestoMenuPage(
    viewModel: RestoMenuViewmodel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val apiState = viewModel.restoApiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Menu of ${viewModel.getRestoName()}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                })
        }
    ) {
        innerPadding ->
        when (apiState) {
            is RestoMenuApiState.Loading -> {
                Box(modifier = Modifier.padding(innerPadding)) {
                    //loading
                    Text(text = "Loading")
                }
            }
            is RestoMenuApiState.Success -> {
                Box(modifier = Modifier.padding(innerPadding)) {
                    //success
                    MenuTabScreen(apiState.data, uiState.currentTab, viewModel::switchTab)
                }
            }
            is RestoMenuApiState.Error -> {
                Box(modifier = Modifier.padding(innerPadding)) {
                    //error
                    Text(text = apiState.message)
                }
            }
    }
}
}



@Composable
fun MenuTabScreen(menuData: MenuData, currentTabIndex: Int = 0, onTabChange: (Int) -> Unit = {}) {
    // Tab state

    Column {
        // Tab row for each day
        ScrollableTabRow(
            selectedTabIndex = currentTabIndex,
            contentColor = MaterialTheme.colorScheme.onSurface,
            divider = {},
            edgePadding = 16.dp
        ) {
            menuData.days.forEachIndexed { index, day ->
                Tab(
                    selected = index == currentTabIndex,
                    onClick = { onTabChange(index) },
                    text = { Text(day.dag) }
                )
            }
        }

        // Content for the selected day
        DayMenuContent(day = menuData.days[currentTabIndex])
    }
}

@Composable
fun DayMenuContent(day: Day) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp)) {
        item {
            Text(
                text = day.dag,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                text = day.message, // Dynamic day message
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(day.menu.items.toList()) { (categoryName, dishes) ->
            MenuCategory(categoryName, dishes)
        }
    }
}



@Composable
fun MenuCategory(categoryName: String, dishes: List<Dish>) {
    Text(
        text = categoryName.uppercase(),
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 18.sp
    )
    dishes.forEach { dish ->
        DishItem(dish)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun DishItem(dish: Dish) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = dish.name,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        when (dish.special) {
            special.NONE, special.UNKNOWN -> {} //Only show special if it's not NONE or UNKNOWN
            else -> {
                //TODO this textbox is being squished or cut off
                Text(
                    text = stringResource(id = dish.special.title),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground

                )
            }
        }

    }
}