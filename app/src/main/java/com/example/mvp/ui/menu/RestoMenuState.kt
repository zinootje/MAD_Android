package com.example.mvp.ui.menu

import com.example.core.model.MenuData
import com.example.mvp.ui.menu.RestoMenuApiState.*


/**
 * Represents the current UI state of the restaurant menu screen.
 *
 * @property currentTab The current tab index selected in the UI.
 * @property restoMenuApiState The API state of the restaurant menu as a [RestoMenuApiState] object.
 * @property restoName The name of the restaurant.
 * @property staleData Indicates whether the data is stale or not.
 * @property toastDataShown Indicates whether the toast data has been shown or not.
 * @property errorSnackbar The error message to be shown in the snackbar. Null if no error.
 */
data class RestoMenuUiState(
    val currentTab: Int = 0,
    val restoMenuApiState: RestoMenuApiState = Loading,
    val restoName: String = "",
    val staleData: Boolean = false,
    val showRefreshingIndicator: Boolean = false,
    val toastDataShown: Boolean = false,
    //null if no error
    val errorSnackbar: String? = null

)


/**
 * Represents the state of the restaurant menu API. With three possible states: [Loading], [Success] and [Error].
 */
sealed interface RestoMenuApiState {
    data object Loading : RestoMenuApiState
    data class Success(val data: MenuData) : RestoMenuApiState
    data class Error(val message: String) : RestoMenuApiState
}