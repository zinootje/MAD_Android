package com.example.mvp.ui.overview

import androidx.compose.runtime.Immutable
import com.example.core.model.Resto

data class RestoOverviewUiState(
    val restoOverviewApiState: RestoOverviewApiState ,
    val gridMode : Boolean = false
)


@Immutable
sealed interface RestoOverviewApiState {
    data object Loading : RestoOverviewApiState
    data class Success(val data: List<Resto>) : RestoOverviewApiState
    data class Error(val message: String) : RestoOverviewApiState
}