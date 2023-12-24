package com.example.mvp

import com.example.mvp.Network.asDomainObject
import com.example.mvp.data.RestoRepository
import com.example.mvp.data.RestoRepositoryImpl
import com.example.mvp.fake.FakeDataSource
import com.example.mvp.fake.FakeRestoApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class NetworkRestoRepositoryTest {


    @Test
    fun getRestoList() {
        runTest {
            val repo = RestoRepositoryImpl(FakeRestoApiService())
            val list = repo.getRestoList()
            assertEquals(list , FakeDataSource.restoList)
        }
    }

    //TODO check reason for tests
    @Test
    fun getRestoMenu() {
        runTest {
            val repo = RestoRepositoryImpl(FakeRestoApiService())
            val menu = repo.getRestoMenu("KFC").first()
            //TODO what with flow
            assertEquals(menu , FakeDataSource.restoMenu.asDomainObject())
        }
    }
}