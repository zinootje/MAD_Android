package com.example.mvp.ui.overview

import androidx.compose.runtime.Immutable

data class RestoOverviewUiState(
    val restoOverviewApiState: RestoOverviewApiState ,
    val gridMode : Boolean = false
)


@Immutable
sealed interface RestoOverviewApiState {
    object Loading : RestoOverviewApiState
    data class Success(val data: List<String>) : RestoOverviewApiState
    data class Error(val message: String) : RestoOverviewApiState
}