package com.example.testutils.fake

import com.example.data.database.Resto
import com.example.data.database.RestoDao
import com.example.data.database.asDbObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Represents a fake implementation of the [RestoDao] interface.
 * This return static data from the [FakeDataSource] object.
 * for more advanced testing, you can use [TestRestoRepository] instead.
 *
 * The [FakeRestoDao] class is used for test purposes and contains dummy implementation for the [RestoDao] functions.
 *
 * @constructor Creates an instance of [FakeRestoDao].
 */
class FakeRestoDao : RestoDao {
    override suspend fun insert(resto: Resto) {
        //no-op
    }

    override suspend fun update(resto: Resto) {
        //no-op
    }

    override suspend fun delete(resto: Resto) {
        //no-op
    }

    override fun getResto(name: String): Flow<Resto> {
        return flow {
            emit(FakeDataSource.restoObjectList.first { it.name == name }.asDbObject())
        }
    }

    override fun getRestoList(): Flow<List<Resto>> {
        return flow {
            emit(FakeDataSource.restoObjectList.map { it.asDbObject() })
        }
    }

    override suspend fun setFavoriteResto(name: String, favorite: Boolean) {
        //no-op
    }

    override suspend fun deleteNotInList(restoList: List<String>) {
        //no-op
    }
}