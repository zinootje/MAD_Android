@file:Suppress("unused")

package com.example.mvp.ui.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.core.Result
import com.example.core.asResult
import com.example.data.RestoRepository
import com.example.data.getErrorMessage
import com.example.data.isNoNetworkError
import com.example.mvp.MvpApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * The RestoMenuViewmodel class represents the ViewModel for the restaurant menu screen.
 *
 * @property restoRepository The repository for restaurant data.
 * @property restoName The name of the restaurant.
 * @property _uiState The mutable state flow for the UI state of the restaurant menu screen.
 * @property uiState The state flow for the UI state of the restaurant menu screen.
 */
class RestoMenuViewmodel(
    private val restoRepository: RestoRepository,
    val restoName: String,
    //private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestoMenuUiState())
    val uiState: StateFlow<RestoMenuUiState> = _uiState.asStateFlow()
    //val networkStatus: StateFlow<Boolean> = networkMonitor.isOnline.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), false)





    init {
        getRestoMenu()
    }


    /**
     * Updates the UI state to indicate that the toast has been shown.
     * Sets the `toastDataShown` property of the UI state to `true`.
     */
    fun toastShown() {
        _uiState.value = _uiState.value.copy(toastDataShown = true)
    }

    private fun getRestoMenu() {
        viewModelScope.launch {
            restoRepository.getRestoMenuSt(restoName).asResult().collect {


                when (it) {
                    is Result.Loading -> _uiState.update { uiState ->

                        //If stale data, show refreshing indicator but keep stale data
                        if (uiState.restoMenuApiState is RestoMenuApiState.Success) {
                            uiState.copy(
                                showRefreshingIndicator = true
                            )
                        } else
                        uiState.copy(
                            restoMenuApiState = RestoMenuApiState.Loading
                        )
                    }

                    is Result.Error -> {
                        Log.e("RestoMenuViewmodel", "getRestoMenu: ", it.exception)

                        //If data shown or no network error, show error snackbar but keep stale data
                        if ((_uiState.value.restoMenuApiState is RestoMenuApiState.Success) || (it.exception?.isNoNetworkError() == true)) {
                            _uiState.update { uiState ->
                                uiState.copy(
                                    errorSnackbar = it.exception?.getErrorMessage(),
                                    //show refreshing indicator if not on loading screen
                                    showRefreshingIndicator = uiState.restoMenuApiState !is RestoMenuApiState.Loading
                                )
                            }
                        } else
                        _uiState.update { uiState ->
                            uiState.copy(
                                restoMenuApiState = RestoMenuApiState.Error(it.exception?.message ?: "Unknown error")
                            )
                        }
                    }

                    is Result.Success -> {
                        if (it.data.isStale) {
                            _uiState.update { uiState ->
                                uiState.copy(
                                    restoMenuApiState = RestoMenuApiState.Success(it.data.data),
                                    staleData = true,
                                    toastDataShown = false,
                                    showRefreshingIndicator = true,
                                    errorSnackbar = null,
                                )
                            }
                        } else {
                            _uiState.update { uiState ->
                                uiState.copy(
                                    restoMenuApiState = RestoMenuApiState.Success(it.data.data),
                                    staleData = false,
                                    toastDataShown = false,
                                    showRefreshingIndicator = false,
                                    errorSnackbar = null,

                                    )
                            }
                        }
                    }
                }
            }
        }
    }

    fun switchTab(tabNumber: Int) {
        _uiState.value = _uiState.value.copy(currentTab = tabNumber)
    }

    companion object {
        /**
         * Creates a [ViewModelProvider.Factory] for creating instances of [RestoMenuViewmodel] with the provided name.
         *
         * @param name The name of the restaurant.
         * @return A [ViewModelProvider.Factory] object.
         */
        fun Factory(name: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MvpApplication)
                val restoRepository = application.container.restoRepository
                RestoMenuViewmodel(
                    restoRepository, name,
                    //application.container.networkMonitor
                )
            }
        }
    }

}