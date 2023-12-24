package com.example.mvp.ui.menu

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mvp.MvpApplication
import com.example.mvp.data.RestoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RestoMenuViewmodel(
    private val restoRepository: RestoRepository,
    private val restoName: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestoMenuUiState())
    val uiState: StateFlow<RestoMenuUiState> = _uiState.asStateFlow()

    var restoApiState: RestoMenuApiState by mutableStateOf(RestoMenuApiState.Loading)
        private set

    init {
        getRestoMenu()
    }

    //public restoname
    fun getRestoName(): String {
        return restoName
    }

    fun getRestoMenu() {
        viewModelScope.launch {
            restoApiState = try {
                //TODO Remove first() to get a flow
                val restoMenu = restoRepository.getRestoMenu(restoName).first()
                RestoMenuApiState.Success(restoMenu)
            } catch (e: Exception) {
                //Log error to logcat
                Log.e("RestoMenuViewmodel", "getRestoMenu: ", e)
                RestoMenuApiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun switchTab(tabNumber: Int) {
        _uiState.value = _uiState.value.copy(currentTab = tabNumber)
    }

    companion object {
        fun Factory(name: String): ViewModelProvider.Factory = viewModelFactory { initializer {
            val application = (this[APPLICATION_KEY] as MvpApplication)
            val restoRepository = application.container.restoRepository
            RestoMenuViewmodel(restoRepository, name)
        } }
    }

}