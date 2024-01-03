@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.mvp.ui.menu

import android.widget.Toast
import androidx.compose.animation.*
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.model.*
import com.example.mvp.R
import com.example.mvp.ui.common.LoadingIndicator
import com.example.mvp.ui.common.NiaOverlayLoadingWheel
import com.example.mvp.ui.util.TabRowType
import com.example.mvp.ui.util.tabKey
import com.theapache64.rebugger.Rebugger
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

/**
 * Composable function that represents the screen for displaying the menu of a restaurant.
 *
 * @param viewModel Intance of [RestoMenuViewmodel] to handle the business logic.
 * @param tabRowType The type of the tab row, can be either [TabRowType.Scrollable] or [TabRowType.Expanded].
 * @param onBack Callback function for navigating back from the screen.
 */
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
    val snackbarError by remember {
        derivedStateOf { uiState.errorSnackbar }
    }

    val shouldShowToast by remember {
        derivedStateOf { !uiState.toastDataShown && uiState.staleData }
    }
    val isRefreshing by remember {
        derivedStateOf { uiState.showRefreshingIndicator }
    }
    val toastMessage = stringResource(R.string.showing_stale_data)
    LaunchedEffect(shouldShowToast) {
        if (shouldShowToast) {
            Toast.makeText(
                context,
                toastMessage,
                Toast.LENGTH_SHORT
            ).show()
            viewModel.toastShown()
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(snackbarError) {
        if (snackbarError != null) {
            snackbarHostState.showSnackbar(
                snackbarError!!,
                duration = SnackbarDuration.Indefinite,
                withDismissAction = true,
            )
        } else {
            //dismiss existing snackbar
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }
    Scaffold(
        snackbarHost =
        {
            SnackbarHost(
                hostState = snackbarHostState,
            )
        },
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
            //show loading indicator when stale data is shown but is loading
            RestoMenu(
                apiState = apiState.value,
                tabRowType = tabRowType
            )
            AnimatedVisibility(
                visible = isRefreshing,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> -fullHeight },
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> -fullHeight },
                ) + fadeOut(),
            ) {
                val loadingContentDescription = stringResource(id = R.string.stale_data_refreshing)
                Box(
                    modifier = Modifier
                        //draw above other composables
                        .zIndex(1f)
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                ) {
                    NiaOverlayLoadingWheel(
                        modifier = Modifier
                            .align(Alignment.Center),
                        contentDesc = loadingContentDescription,
                    )
                }
            }

        }

    }
}


/**
 * Displays the restaurant menu based on the API state.
 *
 * @param modifier The modifier to be applied to the RestoMenu composable. Defaults to [Modifier].
 * @param apiState The API state of the restaurant menu.
 * @param tabRowType The type of the tab row. It can be either TabRowType.Scrollable or TabRowType.Expanded.
 */
@Composable
fun RestoMenu(
    modifier: Modifier = Modifier,
    apiState: RestoMenuApiState,
    tabRowType: TabRowType,
) {
    Crossfade(
        targetState = apiState, label = "stateTransition",
    )
    { state ->
        when (state) {
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
                    MenuContent(state.data, tabRowType)
                }
            }

            is RestoMenuApiState.Error -> {
                Box(
                    modifier = modifier
                ) {
                    //error
                    Text(text = state.message)
                }
            }
        }
    }

}


/**
 * Composable function that displays the menu content.
 *
 * @param menuData The menu data containing the location and days.
 * @param tabRowType The type of the tab row.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MenuContent(menuData: MenuData, tabRowType: TabRowType) {
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

/**
 * Private composable function that represents a tab in the MenuContent.
 *
 * @param selected True if the tab is currently selected, false otherwise.
 * @param onClick Callback function that is triggered when the tab is clicked.
 * @param text The text to be displayed on the tab.
 */
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


/**
 * Composable function that displays a tab row based on the tab row type.
 *
 * @param selectedTabIndex The index of the currently selected tab.
 * @param contentColor The color of the tab content.
 * @param tabRowType The type of the tab row.
 * @param tabs The composable function that renders the tabs.
 */
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

/**
 * Composable function that displays the menu content for a specific day.
 *
 * @param day The Day object containing the menu information for the day.
 */
@Composable
private fun DayMenuContent(day: Day) {
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


/**
 * Composable function that displays the menu category with its dishes.
 *
 * @param categoryName The name of the menu category.
 * @param dishes The list of [Dish]es belonging to the category.
 */
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

/**
 * Composable function that displays a dish item.
 *
 * @param dish The [Dish] object containing the name and special info.
 */
@Composable
private fun DishItem(dish: Dish) {
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

private val mndata = MenuData(
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
)

@Composable
@Preview(showBackground = true)
private fun MenuContentPreview() {
    MenuContent(
        menuData = mndata,
        tabRowType = TabRowType.Scrollable
    )
}

@Composable
@Preview(showBackground = true)
private fun RestoMenuPreviewExpanded() {
    RestoMenu(
        apiState = RestoMenuApiState.Success(mndata),
        tabRowType = TabRowType.Expanded
    )
}