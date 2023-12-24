package com.example.mvp.ui.overview

import androidx.compose.ui.tooling.data.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mvp.MvpApplication
import com.example.mvp.core.asResult
import com.example.mvp.core.Result
import com.example.mvp.data.RestoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class RestoOverviewViewModel(private val restoRepository: RestoRepository): ViewModel() {
    val _uiState = MutableStateFlow(RestoOverviewUiState(restoOverviewApiState = RestoOverviewApiState.Loading))
    val uiState : StateFlow<RestoOverviewUiState> = _uiState.asStateFlow()


    init {
        getRestoList()
    }

    fun getRestoList() {
       viewModelScope.launch {
           restoRepository.getRestoList().asResult().collect() {it ->
               val state = when (it) {
                   is Result.Loading -> RestoOverviewApiState.Loading
                   is Result.Error -> RestoOverviewApiState.Error(it.exception?.message ?: "Unknown error")
                   is Result.Success -> RestoOverviewApiState.Success(it.data.map { it.name })
               }

               _uiState.value = _uiState.value.copy(restoOverviewApiState = state)

           }
       }
    }

    fun toggleGridMode() {
        _uiState.value = _uiState.value.copy(gridMode = !_uiState.value.gridMode)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory { initializer {
            val application = (this[APPLICATION_KEY] as MvpApplication)
            val restoRepository = application.container.oflRepository
            RestoOverviewViewModel(restoRepository)
        } }
    }
}