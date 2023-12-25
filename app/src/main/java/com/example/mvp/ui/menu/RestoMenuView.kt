@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.mvp.ui.menu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvp.model.Day
import com.example.mvp.model.Dish
import com.example.mvp.model.MenuData
import com.example.mvp.model.special
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
                    MenuTabScreen(apiState.data)
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



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuTabScreen(menuData: MenuData) {
    // Pager state
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0F
    ) {
       menuData.days.size
    }
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

    val animationScope = rememberCoroutineScope()
    Column {
        // Tab row
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            contentColor = MaterialTheme.colorScheme.onSurface,

        ) {
            days.forEachIndexed { index, day ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        // Animate to the selected page
                        animationScope.launch {
                            pagerState.animateScrollToPage(index)
                        }

                        //onTabChange(index)
                    },
                    text = { Text(day) }
                )
            }
        }

        // Horizontal pager for the content
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) { page ->
            // Content for the selected day
            DayMenuContent(day = menuData.days[page])
        }
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