package com.example.data.database.fake

class FakeRestoApiService : com.example.network.RestoApiService {
    override suspend fun getRestoList(): List<String> {
        return FakeDataSource.restoList
    }

    override suspend fun getRestoMenu(name: String): com.example.network.ApiRestoMenu {
        return FakeDataSource.restoMenu
    }

}