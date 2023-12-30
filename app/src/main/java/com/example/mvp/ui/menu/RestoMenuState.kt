package com.example.mvp.ui.menu

import com.example.core.model.MenuData


data class RestoMenuUiState(
    val currentTab: Int = 0,
    val restoMenuApiState: RestoMenuApiState = RestoMenuApiState.Loading,
    val restoName: String = "",
    val staleData: Boolean = false,
    val toastDataShown: Boolean = false,

)


sealed interface RestoMenuApiState {
    data object Loading : RestoMenuApiState
    data class Success(val data: MenuData) : RestoMenuApiState
    data class Error(val message: String) : RestoMenuApiState
}