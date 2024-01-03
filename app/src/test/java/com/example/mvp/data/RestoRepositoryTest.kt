package com.example.mvp.data

import com.example.data.RestoOfflineRepositoryImpl
import com.example.network.asDomainObject
import com.example.testutils.fake.FakeDataSource
import com.example.testutils.fake.FakeMenuDao
import com.example.testutils.fake.FakeRestoApiService
import com.example.testutils.fake.FakeRestoDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test


class RestoRepositoryTest {

    //TODO make more advanced tests

    @Test
    fun getRestoList() {
        runTest {
            val repo = RestoOfflineRepositoryImpl(FakeRestoApiService(), FakeRestoDao(), FakeMenuDao())
            val list = repo.getRestoList().first()
            assertEquals( FakeDataSource.restoObjectList , list)
        }
    }

    @Test
    fun getRestoMenu() {
        runTest {
            val repo = RestoOfflineRepositoryImpl(FakeRestoApiService(), FakeRestoDao(), FakeMenuDao())
            val menu = repo.getRestoMenu(FakeDataSource.restoMenu.location).first()
            //TODO what with flow
            assertEquals(FakeDataSource.restoMenu.asDomainObject(), menu)
        }
    }
}