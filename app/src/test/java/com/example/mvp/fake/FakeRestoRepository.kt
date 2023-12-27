package com.example.mvp.fake

import com.example.core.model.MenuData
import com.example.core.model.Resto
import com.example.data.RestoRepository
import com.example.network.asDomainObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRestoRepository: RestoRepository
{
    override  fun getRestoList(): Flow<List<Resto>> {
        return flow {
            emit(FakeDataSource.restoObjectList)
        }
    }

    override suspend fun getRestoMenu(name: String): Flow<MenuData> {
        return flow {
            emit(FakeDataSource.restoMenu.asDomainObject())
        }
    }

    override suspend fun setFavoriteResto(name: String, favorite: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun refreshRestoList() {
        TODO("Not yet implemented")
    }

    override suspend fun refreshRestoMenu(name: String) {
        TODO("Not yet implemented")
    }

}