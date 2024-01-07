package com.example.mvp.ui.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.core.Result
import com.example.core.asResult
import com.example.core.model.ErrorMessage
import com.example.data.RestoRepository
import com.example.data.getErrorMessage
import com.example.mvp.MvpApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/**
 * The RestoOverviewViewModel class represents a ViewModel that provides data and functionality for the restaurant overview screen.
 *
 * @property restoRepository The repository used to interact with restaurant data.
 * @property _uiState The private mutable state flow representing the UI state of the restaurant overview screen.
 * @property uiState The public state flow representing the UI state of the restaurant overview screen.
 */
class RestoOverviewViewModel(private val restoRepository: RestoRepository): ViewModel() {
    private val _uiState = MutableStateFlow(RestoOverviewUiState(restoOverviewApiState = RestoOverviewApiState.Loading))
    val uiState : StateFlow<RestoOverviewUiState> = _uiState.asStateFlow()


    init {
        getRestoList()
    }


    /**
     * Sets the favorite status of a restaurant.
     *
     * @param restoName the name of the restaurant
     * @param isFavorite true if the restaurant should be marked as favorite, false otherwise
     */
    fun setFavorite(restoName: String, isFavorite: Boolean) {
        viewModelScope.launch {
            restoRepository.setFavoriteResto(restoName, isFavorite)
        }
    }

    private fun getRestoList() {
       viewModelScope.launch {
           restoRepository.getRestoList().asResult().collect {
               val state: RestoOverviewApiState = when (it) {
                   is Result.Loading -> RestoOverviewApiState.Loading
                   is Result.Error -> {
                       Log.e("RestoOverviewViewModel", "getRestoList: ", it.exception)
                       RestoOverviewApiState.Error(it.exception?.getErrorMessage() ?: ErrorMessage.Unknown)
                   }

                   is Result.Success -> RestoOverviewApiState.Success(it.data)
               }

               _uiState.value = _uiState.value.copy(restoOverviewApiState = state)

           }
       }
    }

    /**
     * Toggles the grid mode of the restaurant overview UI.
     *
     * This function updates the `_uiState` value by toggling the `gridMode` property. The new value is determined by negating the current value of `gridMode`. The updated `_uiState
     *` value is then assigned back to `_uiState`.
     *
     * @see RestoOverviewViewModel
     * @see RestoOverviewUiState
     */
    fun toggleGridMode() {
        _uiState.value = _uiState.value.copy(gridMode = !_uiState.value.gridMode)
    }

    companion object {
        /**
         * Represents a ViewModelProvider.Factory for creating instances of the RestoOverviewViewModel class.
         */
        val Factory: ViewModelProvider.Factory = viewModelFactory { initializer {
            val application = (this[APPLICATION_KEY] as MvpApplication)
            val restoRepository = application.container.restoRepository
            RestoOverviewViewModel(restoRepository)
        } }
    }
}