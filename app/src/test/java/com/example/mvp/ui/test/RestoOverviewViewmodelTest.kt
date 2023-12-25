package com.example.mvp.ui.test

import com.example.mvp.fake.FakeDataSource
import com.example.mvp.fake.TestRestoRepository
import com.example.mvp.network.asDomainObject
import com.example.mvp.ui.menu.RestoMenuApiState
import com.example.mvp.ui.menu.RestoMenuViewmodel
import com.example.mvp.ui.overview.RestoOverviewApiState
import com.example.mvp.ui.overview.RestoOverviewViewModel
import com.example.mvp.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RestoOverviewViewmodelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    //const
    val RESTO_NAME = "test"


    private val restoRepository = TestRestoRepository()

    private lateinit var restoOverviewViewmodel: RestoOverviewViewModel

    @Before
    fun setup() {
        restoOverviewViewmodel = RestoOverviewViewModel(restoRepository)
    }


    //@OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun RestoOverviewViewModel_Init_StartsInLoading() {
        runTest {
            assert(restoOverviewViewmodel.uiState.value.restoOverviewApiState is RestoOverviewApiState.Loading)
        }
    }

    @Test
    fun RestoOverviewViewModel_Init_LoadsData() {
        runTest {
            restoRepository.sendRestoList(FakeDataSource.restoObjectList)
            assert(restoOverviewViewmodel.uiState.value.restoOverviewApiState is RestoOverviewApiState.Success)
            assert((restoOverviewViewmodel.uiState.value.restoOverviewApiState as RestoOverviewApiState.Success).data == FakeDataSource.restoObjectList)

        }
    }

}