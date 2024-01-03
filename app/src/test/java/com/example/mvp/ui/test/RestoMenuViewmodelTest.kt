package com.example.mvp.ui.test

import com.example.core.StaleAbleData
import com.example.mvp.ui.menu.RestoMenuApiState
import com.example.mvp.ui.menu.RestoMenuUiState
import com.example.mvp.ui.menu.RestoMenuViewmodel
import com.example.mvp.util.MainDispatcherRule
import com.example.network.asDomainObject
import com.example.testutils.fake.FakeDataSource
import com.example.testutils.fake.FakeNetworkMonitor
import com.example.testutils.fake.TestRestoRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RestoMenuViewmodelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    //const
    val RESTO_NAME = "test"


    private val restoRepository = TestRestoRepository()

    private lateinit var restoMenuViewmodel: RestoMenuViewmodel

    @Before
    fun setup() {
        restoMenuViewmodel = RestoMenuViewmodel(restoRepository, RESTO_NAME, FakeNetworkMonitor())
    }


    //@OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun restoViewModel_Init_StartsInLoading() {
        runTest {
            assertEquals(
                restoMenuViewmodel.uiState.value, RestoMenuUiState(
                    restoMenuApiState = RestoMenuApiState.Loading,
                )
            )
        }
    }

    @Test
    fun restoViewModel_DataLoaded_correctUiState() {
        runTest {
            restoRepository.sendRestoMenu(RESTO_NAME, StaleAbleData(FakeDataSource.restoMenu.asDomainObject(), false))
            assertEquals(
                restoMenuViewmodel.uiState.value, RestoMenuUiState(
                    restoMenuApiState = RestoMenuApiState.Success(FakeDataSource.restoMenu.asDomainObject()),
                )
            )
        }
    }

    @Test
    fun restoViewModel_StaleData_correctUiState() {
        runTest {
            restoRepository.sendRestoMenu(RESTO_NAME, StaleAbleData(FakeDataSource.restoMenu.asDomainObject(), true))
            assertEquals(
                restoMenuViewmodel.uiState.value, RestoMenuUiState(
                    restoMenuApiState = RestoMenuApiState.Success(FakeDataSource.restoMenu.asDomainObject()),
                    staleData = true,
                    showRefreshingIndicator = true
                )
            )
        }
    }

    @Test
    fun restoViewModel_Error_correctUiState() {
        runTest {
            restoRepository.throwErrorInRestoMenu(RESTO_NAME, RuntimeException())
            assertEquals(
                restoMenuViewmodel.uiState.value, RestoMenuUiState(
                    restoMenuApiState = RestoMenuApiState.Error("Unknown error"),
                )
            )
        }
    }

}
