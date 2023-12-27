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

    private fun getRestoMenu() {
        viewModelScope.launch {
            restoRepository.getRestoMenu(restoName).asResult().collect {
                val state = when (it) {
                    is Result.Loading -> RestoMenuApiState.Loading
                    is Result.Error -> {

                        Log.e("RestoMenuViewmodel", "getRestoMenu: ", it.exception)
                        RestoMenuApiState.Error(it.exception?.message ?: "Unknown error")
                    }

                    is Result.Success -> {
                        //TODO remover one of the two orzfwdxs
                        _uiState.value = _uiState.value.copy()
                        RestoMenuApiState.Success(it.data)
                    }

                }
                //TODO remive one of the two
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