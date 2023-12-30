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
import com.example.mvp.MvpApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RestoMenuViewmodel(
    private val restoRepository: RestoRepository,
    private val restoName: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestoMenuUiState())
    val uiState: StateFlow<RestoMenuUiState> = _uiState.asStateFlow()


    init {
        getRestoMenu()
    }

    //public restoname
    fun getRestoName(): String {
        return restoName
    }

    fun toastShown() {
        _uiState.value = _uiState.value.copy(toastDataShown = true)
    }

    private fun getRestoMenu() {
        viewModelScope.launch {
            restoRepository.getRestoMenuSt(restoName).asResult().collect {
                val state = when (it) {
                    is Result.Loading -> RestoMenuApiState.Loading
                    is Result.Error -> {

                        Log.e("RestoMenuViewmodel", "getRestoMenu: ", it.exception)
                        RestoMenuApiState.Error(it.exception?.message ?: "Unknown error")
                    }

                    is Result.Success -> {
                        //TODO remove double reassignment
                        if (it.data.isStale) {
                            _uiState.value = _uiState.value.copy(staleData = true, toastDataShown = false)
                        }
                        RestoMenuApiState.Success(it.data.data)
                    }

                }
                _uiState.value = _uiState.value.copy(restoMenuApiState = state)


            }
//            restoApiState = try {
//                //TODO Remove first() to get a flow
//                val restoMenu = restoRepository.getRestoMenu(restoName).first()
//                RestoMenuApiState.Success(restoMenu)
//            } catch (e: Exception) {
//                //Log error to logcat
//                Log.e("RestoMenuViewmodel", "getRestoMenu: ", e)
//                RestoMenuApiState.Error(e.message ?: "Unknown error")
//            }
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