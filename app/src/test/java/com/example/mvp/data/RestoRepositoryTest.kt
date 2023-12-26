package com.example.mvp.data

import com.example.mvp.network.asDomainObject
import com.example.mvp.data.RestoOfflineRepositoryImpl
import com.example.mvp.fake.FakeDataSource
import com.example.mvp.fake.FakeMenuDao
import com.example.mvp.fake.FakeRestoApiService
import com.example.mvp.fake.FakeRestoDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
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

    //TODO check reason for tests
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