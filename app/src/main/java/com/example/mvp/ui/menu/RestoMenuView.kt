@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.mvp.ui.menu

import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.model.*
import com.example.mvp.R
import com.example.mvp.ui.Util.TabRowType
import com.example.mvp.ui.Util.tabKey
import com.example.mvp.ui.common.LoadingIndicator
import com.theapache64.rebugger.Rebugger
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoMenuScreen(
    viewModel: RestoMenuViewmodel,
    tabRowType: TabRowType = TabRowType.Scrollable,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val apiState = remember {
        derivedStateOf { uiState.restoMenuApiState }
    }

    val shouldShowToast by remember {
        derivedStateOf { !uiState.toastDataShown && uiState.staleData }
    }
    val toastMessage = stringResource(R.string.showing_stale_data)
    LaunchedEffect(key1 = shouldShowToast) {
        if (shouldShowToast) {
            Toast.makeText(
                context,
                toastMessage,
                Toast.LENGTH_SHORT
            ).show()
            viewModel.toastShown()
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Menu of ${viewModel.getRestoName()}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            RestoMenu(
                apiState = apiState.value,
                tabRowType = tabRowType
            )
        }
    }
}


@Composable
fun RestoMenu(
    modifier: Modifier = Modifier,
    apiState: RestoMenuApiState,
    tabRowType: TabRowType,
) {
    when (apiState) {
        is RestoMenuApiState.Loading -> {
            Box(
                modifier = modifier
            ) {
                LoadingIndicator()
            }
        }

        is RestoMenuApiState.Success -> {
            Box(
                modifier = modifier
            ) {
                //success
                MenuContent(apiState.data, tabRowType)
            }
        }

        is RestoMenuApiState.Error -> {
            Box(
                modifier = modifier
            ) {
                //error
                Text(text = apiState.message)
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuContent(menuData: MenuData, tabRowType: TabRowType) {
    // Pager state
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0F
    ) {
        menuData.days.size
    }


    val animationScope = rememberCoroutineScope()

    //ToAvoid recomposition on every page change
    val animatedNavigate = remember {
        { page: Int ->
            animationScope.launch {
                pagerState.animateScrollToPage(page)
            }
        }
    }

    Column {
        // Tab row
        MvpTabRow(
            selectedTabIndex = pagerState.currentPage,
            contentColor = MaterialTheme.colorScheme.onSurface,
            tabRowType = tabRowType
        ) {
            menuData.days.forEachIndexed { index, day ->


                DayTab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        animatedNavigate(index)
                    },
                    text = day.dag
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
private fun DayTab(
    selected: Boolean,
    onClick: () -> Unit,
    text: String
) {
    Tab(
        modifier = Modifier.semantics { tabKey = text },
        selected = selected,
        onClick = onClick,
        text = { Text(text = text) }
    )
    Rebugger(trackMap = mapOf("selected" to selected, "text" to text, "onClick" to onClick))
}


@Composable
fun MvpTabRow(
    selectedTabIndex: Int,
    contentColor: Color,
    tabRowType: TabRowType,
    tabs: @Composable () -> Unit
) {
    when (tabRowType) {
        TabRowType.Scrollable -> {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                contentColor = contentColor,
                edgePadding = 0.dp,
                tabs = tabs
            )
        }

        TabRowType.Expanded -> {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                contentColor = contentColor,
                tabs = tabs
            )
        }
    }

}

@Composable
fun DayMenuContent(day: Day) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
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
            modifier = Modifier.weight(5f),
            maxLines = 5,
            text = dish.name,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        when (dish.special) {
            Special.NONE, Special.UNKNOWN -> {} //Only show special if it's not NONE or UNKNOWN
            else -> {
                Text(
                    modifier = Modifier.weight(1f, false),
                    maxLines = 1,
                    text = stringResource(id = dish.special.title),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground

                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun DishItemPreview() {
    DishItem(
        dish = Dish(
            name = "Dish names",
            special = Special.VEGIE
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun DishItemCutoffPreview() {
    DishItem(
        dish = Dish(
            name = "Dish namessdqfsdqf dsq fsqdf sdf sdqf sdqf sqdfsdqfsqdf sdfsdqfsdqsdf sdfq",
            special = Special.VEGIE
        )
    )
}

@Composable
@Preview(showBackground = true)
private fun MenuContentPreview() {
    MenuContent(
        menuData = MenuData(
            location = "location",
            days = listOf(
                Day(
                    dag = "Monday",
                    message = "message",
                    menu = Menu(
                        items = persistentMapOf(
                            "Breakfast" to listOf(
                                Dish(
                                    name = "Bread",
                                    special = Special.NONE
                                ),
                                Dish(
                                    name = "Cereal",
                                    special = Special.VEGAN
                                )
                            ),
                            "Drinks" to listOf(
                                Dish(
                                    name = "Coffee",
                                    special = Special.NONE
                                ),
                                Dish(
                                    name = "Tea",
                                    special = Special.VEGIE
                                )
                            )
                        )
                    )
                ),
                Day(
                    dag = "Tuesday",
                    message = "message2",
                    menu =
                    Menu(
                        persistentMapOf(
                            "Breakfast2" to listOf(
                                Dish(
                                    name = "Bread2",
                                    special = Special.NONE
                                ),
                                Dish(
                                    name = "Cereal2",
                                    special = Special.VEGAN
                                )
                            ),
                            "Drinks2" to listOf(
                                Dish(
                                    name = "Coffee2",
                                    special = Special.NONE
                                ),
                                Dish(
                                    name = "Tea2",
                                    special = Special.VEGIE
                                )
                            )
                        )

                    )
                )
            ).toImmutableList()
        ),
        tabRowType = TabRowType.Scrollable
    )
}