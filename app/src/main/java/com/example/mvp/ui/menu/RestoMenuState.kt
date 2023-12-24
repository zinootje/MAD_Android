package com.example.mvp.ui.menu

import com.example.mvp.model.MenuData


data class RestoMenuUiState(
    val currentTab: Int = 0,

)


sealed interface RestoMenuApiState {
    data object Loading : RestoMenuApiState
    data class Success(val data: MenuData) : RestoMenuApiState
    data class Error(val message: String) : RestoMenuApiState
}