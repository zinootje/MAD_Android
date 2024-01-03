package com.example.mvp.ui.test

import com.example.mvp.ui.overview.RestoOverviewApiState
import com.example.mvp.ui.overview.RestoOverviewViewModel
import com.example.mvp.util.MainDispatcherRule
import com.example.testutils.fake.FakeDataSource
import com.example.testutils.fake.TestRestoRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RestoOverviewViewmodelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val restoRepository = TestRestoRepository()

    private lateinit var restoOverviewViewmodel: RestoOverviewViewModel

    @Before
    fun setup() {
        restoOverviewViewmodel = RestoOverviewViewModel(restoRepository)
    }


    //@OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun restoOverviewViewModel_Init_StartsInLoading() {
        runTest {
            assert(restoOverviewViewmodel.uiState.value.restoOverviewApiState is RestoOverviewApiState.Loading)
        }
    }

    @Test
    fun restoOverviewViewModel_Init_LoadsData() {
        runTest {
            restoRepository.sendRestoList(FakeDataSource.restoObjectList)
            assert(restoOverviewViewmodel.uiState.value.restoOverviewApiState is RestoOverviewApiState.Success)
            assert((restoOverviewViewmodel.uiState.value.restoOverviewApiState as RestoOverviewApiState.Success).data == FakeDataSource.restoObjectList)

        }
    }

    @Test
    fun restoOverviewViewModel_OnError_showError() {
        runTest {
            restoRepository.throwErrorInRestoList(RuntimeException())
            assert(restoOverviewViewmodel.uiState.value.restoOverviewApiState is RestoOverviewApiState.Error)
        }
    }

}