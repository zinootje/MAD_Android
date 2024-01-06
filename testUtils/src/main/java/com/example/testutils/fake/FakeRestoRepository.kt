package com.example.testutils.fake

import com.example.core.StaleAbleData
import com.example.core.model.MenuData
import com.example.core.model.Resto
import com.example.data.RestoRepository
import com.example.network.asDomainObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


/**
 * Represents a fake implementation of the [RestoRepository] interface.
 * This return static data from the [FakeDataSource] object.
 * for more advanced testing, you can use [TestRestoApiService] instead.
 *
 * The [FakeRestoRepository] class is used for test purposes and contains dummy implementation for the [RestoRepository] functions.
 *
 */
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
        //no-op
    }

    override suspend fun getRestoMenuSt(name: String): Flow<StaleAbleData<MenuData>> {
        return flow {
            emit(StaleAbleData(FakeDataSource.restoMenu.asDomainObject(), false))
        }
    }

    override suspend fun refreshRestoList() {
        //no-op
    }

    override suspend fun refreshRestoMenu(name: String) {
        //no-op
    }

}