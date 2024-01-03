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

    @Test
    fun restoViewModel_init_RestoNameCorrect() {
        runTest {
            assertEquals(
                RESTO_NAME, restoMenuViewmodel.restoName,
            )
        }
    }


    //@OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun restoViewModel_Init_StartsInLoading() {
        runTest {
            assertEquals(
                RestoMenuUiState(
                    restoMenuApiState = RestoMenuApiState.Loading,
                ),
                restoMenuViewmodel.uiState.value,
            )
        }
    }

    @Test
    fun restoViewModel_DataLoaded_correctUiState() {
        runTest {
            restoRepository.sendRestoMenu(RESTO_NAME, StaleAbleData(FakeDataSource.restoMenu.asDomainObject(), false))
            assertEquals(
                RestoMenuUiState(
                    restoMenuApiState = RestoMenuApiState.Success(FakeDataSource.restoMenu.asDomainObject()),
                ), restoMenuViewmodel.uiState.value
            )
        }
    }

    @Test
    fun restoViewModel_StaleData_correctUiState() {
        runTest {
            restoRepository.sendRestoMenu(RESTO_NAME, StaleAbleData(FakeDataSource.restoMenu.asDomainObject(), true))
            assertEquals(
                RestoMenuUiState(
                    restoMenuApiState = RestoMenuApiState.Success(FakeDataSource.restoMenu.asDomainObject()),
                    staleData = true,
                    showRefreshingIndicator = true
                ), restoMenuViewmodel.uiState.value
            )
        }
    }

    @Test
    fun restoViewModel_Error_correctUiState() {
        runTest {
            restoRepository.throwErrorInRestoMenu(RESTO_NAME, RuntimeException())
            assertEquals(
                RestoMenuUiState(
                    restoMenuApiState = RestoMenuApiState.Error("Unknown error"),
                ), restoMenuViewmodel.uiState.value
            )
        }
    }

    @Test
    fun restoViewModel_StaleData_then_error_correctUiState() {
        runTest {
            restoRepository.sendRestoMenu(RESTO_NAME, StaleAbleData(FakeDataSource.restoMenu.asDomainObject(), true))
            restoRepository.throwErrorInRestoMenu(RESTO_NAME, RuntimeException())
            assertEquals(
                RestoMenuUiState(
                    restoMenuApiState = RestoMenuApiState.Success(FakeDataSource.restoMenu.asDomainObject()),
                    staleData = true,
                    showRefreshingIndicator = true,
                    //TODO error enum
                    errorSnackbar = "Network error"
                ),
                restoMenuViewmodel.uiState.value,
            )
        }
    }

    @Test
    fun restoViewModel_StaleData_then_freshData_correctUiState() {
        runTest {
            restoRepository.sendRestoMenu(RESTO_NAME, StaleAbleData(FakeDataSource.restoMenu.asDomainObject(), true))
            restoRepository.sendRestoMenu(RESTO_NAME, StaleAbleData(FakeDataSource.restoMenu2.asDomainObject(), false))
            assertEquals(
                RestoMenuUiState(
                    restoMenuApiState = RestoMenuApiState.Success(FakeDataSource.restoMenu2.asDomainObject()),
                    staleData = false,
                    showRefreshingIndicator = false,
                    errorSnackbar = null,
                ), restoMenuViewmodel.uiState.value
            )
        }
    }

}
