package com.example.mvp.ui.test

import com.example.mvp.fake.FakeDataSource
import com.example.mvp.fake.TestRestoRepository
import com.example.mvp.ui.menu.RestoMenuApiState
import com.example.mvp.ui.menu.RestoMenuViewmodel
import com.example.mvp.util.MainDispatcherRule
import com.example.network.asDomainObject
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RestoMenuViewmodelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    //const
    val RESTO_NAME = "test"


    private val RestoRepository = TestRestoRepository()

    private lateinit var restoMenuViewmodel: RestoMenuViewmodel

    @Before
    fun setup() {
        restoMenuViewmodel = RestoMenuViewmodel(RestoRepository, RESTO_NAME)
    }


    //@OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun restoViewModel_Init_StartsInLoading() {
        runTest {
            assert(restoMenuViewmodel.restoApiState is RestoMenuApiState.Loading)
        }
    }

    @Test
    fun restoViewModel_Init_LoadsData() {
        runTest {
            RestoRepository.sendRestoMenu(RESTO_NAME, FakeDataSource.restoMenu.asDomainObject())
            assert(restoMenuViewmodel.restoApiState is RestoMenuApiState.Success)
            assert((restoMenuViewmodel.restoApiState as RestoMenuApiState.Success).data == FakeDataSource.restoMenu.asDomainObject())
        }
    }

}
