package com.example.data

import app.cash.turbine.test
import com.example.core.StaleAbleData
import com.example.data.database.MenuDataEntity
import com.example.data.database.toDbMenu
import com.example.network.asDomainObject
import com.example.testutils.fake.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test


class RestoRepositoryTest {

    //TODO make more advanced tests

    @Test
    fun restoOfflineRepositoryImpl_getRestoList_shouldReturnRestoList() {
        runTest {
            val repo = RestoOfflineRepositoryImpl(FakeRestoApiService(), FakeRestoDao(), FakeMenuDao())
            val list = repo.getRestoList().first()
            assertEquals( FakeDataSource.restoObjectList , list)
        }
    }

    @Test
    fun restoOfflineRepositoryImpl_getRestoMenu_shouldReturnRestoMenu() {
        runTest {
            val repo = RestoOfflineRepositoryImpl(FakeRestoApiService(), FakeRestoDao(), FakeMenuDao())
            val menu = repo.getRestoMenu(FakeDataSource.restoMenu.location).first()
            //TODO what with flow
            assertEquals(FakeDataSource.restoMenu.asDomainObject(), menu)
        }
    }

    @Test
    fun restoOfflineRepositoryImpl_getRestoMenuSt_whenStale_shouldReturnStaleData() {
        runTest {
            val testRestoDao = TestRestoDao()
            val testMenuDao = TestMenuDao()
            val repo = RestoOfflineRepositoryImpl(FakeRestoApiService(), testRestoDao, testMenuDao)
            testMenuDao.setMenuData(
                MenuDataEntity(
                    FakeDataSource.restoMenu.location,
                    FakeDataSource.restoMenu.toDbMenu().days,
                    //makes the data stale
                    1
                )
            )
            val menu = repo.getRestoMenuSt(FakeDataSource.restoMenu.location).first()
            //TODO what with flow
            assertEquals(StaleAbleData(FakeDataSource.restoMenu.asDomainObject(), true), menu)
        }
    }

    @Test
    fun restoOfflineRepositoryImpl_getRestoMenuSt_whenNotStale_shouldReturnNotStaleData() {
        runTest {
            val repo = RestoOfflineRepositoryImpl(FakeRestoApiService(), FakeRestoDao(), FakeMenuDao())
            val menu = repo.getRestoMenuSt(FakeDataSource.restoMenu.location).first()
            //TODO what with flow
            assertEquals(StaleAbleData(FakeDataSource.restoMenu.asDomainObject(), false), menu)
        }
    }

    @Test
    fun restoOfflineRepositoryImpl_refreshRestoList_shouldReturnRestoList() {
        runTest {
            val testRestoDao = TestRestoDao()
            val testMenuDao = TestMenuDao()
            val testResoApiService = TestResoApiService()
            val repo = RestoOfflineRepositoryImpl(FakeRestoApiService(), testRestoDao, testMenuDao)
            repo.refreshRestoList()
            repo.getRestoList().test {
                testResoApiService.setRestoList(FakeDataSource.restoObjectList.map { it.name })
                assertEquals(FakeDataSource.restoObjectList.map {
                    //set favorite to false
                    it.copy(isFavorite = false)
                }, awaitItem())
            }
        }
    }



    //https://developer.android.com/kotlin/flow/test#turbine
    //TODO maybe use turbine in all tests
    @Test
    fun restoOfflineRepositoryImpl_getRestoMenuSt_shouldRefreshWhenStale() {
        runTest {
            val testRestoDao = TestRestoDao()
            val testMenuDao = TestMenuDao()
            val testResoApiService = TestResoApiService()
            val repo = RestoOfflineRepositoryImpl(testResoApiService, testRestoDao, testMenuDao)
            val restoMenu = FakeDataSource.restoMenu
            testMenuDao.setMenuData(
                MenuDataEntity(
                    restoMenu.location,
                    restoMenu.toDbMenu().days,
                    //makes the data stale
                    1
                )
            )
            repo.getRestoMenuSt(restoMenu.location).test {
                val menu = awaitItem()
                val expectecdUpdateMenu = restoMenu.copy(
                    days = FakeDataSource.restoMenu2.days
                )
                //return data from api
                testResoApiService.setRestoMenu(restoMenu.location, expectecdUpdateMenu)
                val updatedMenu = awaitItem()
                assertEquals(StaleAbleData(FakeDataSource.restoMenu.asDomainObject(), true), menu)
                assertEquals(StaleAbleData(expectecdUpdateMenu.asDomainObject(), false), updatedMenu)

            }
        }


    }

}