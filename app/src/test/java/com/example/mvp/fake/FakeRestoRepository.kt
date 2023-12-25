package com.example.mvp.fake

import com.example.mvp.network.asDomainObject
import com.example.mvp.data.RestoRepository
import com.example.mvp.model.MenuData
import com.example.mvp.model.Resto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRestoRepository: RestoRepository
{
    override suspend fun getRestoList(): Flow<List<Resto>> {
        return flow {
            emit(FakeDataSource.restoList.map { Resto(it) })
        }
    }

    override suspend fun getRestoMenu(name: String): Flow<MenuData> {
        return flow {
            emit(FakeDataSource.restoMenu.asDomainObject())
        }
    }

}