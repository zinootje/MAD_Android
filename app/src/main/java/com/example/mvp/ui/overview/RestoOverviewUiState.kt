package com.example.mvp.ui.overview

import androidx.compose.runtime.Immutable
import com.example.core.model.Resto
import com.example.mvp.ui.overview.RestoOverviewApiState.*

/**
 * Represents the state of the restaurant overview UI.
 *
 * @property restoOverviewApiState The state of the API response for the restaurant overview.
 * @property gridMode Indicates whether the grid mode is enabled or not.
 */
data class RestoOverviewUiState(
    val restoOverviewApiState: RestoOverviewApiState ,
    val gridMode : Boolean = false
)


/**
 * Represents the various states of the restaurant overview API. With three possible states: [Loading], [Success] and [Error].
 */
@Immutable
sealed interface RestoOverviewApiState {
    data object Loading : RestoOverviewApiState
    data class Success(val data: List<Resto>) : RestoOverviewApiState
    data class Error(val message: String) : RestoOverviewApiState
}