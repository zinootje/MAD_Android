package com.example.mvp.fake

import com.example.mvp.Network.ApiRestoMenu
import com.example.mvp.Network.RestoApiService

class FakeRestoApiService: RestoApiService {
    override suspend fun getRestoList(): List<String> {
        return FakeDataSource.restoList
    }

    override suspend fun getRestoMenu(name: String): ApiRestoMenu {
        return FakeDataSource.restoMenu
    }

}