package com.example.mvp.fake

import com.example.data.database.Resto
import com.example.data.database.RestoDao
import com.example.data.database.asDbObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRestoDao : RestoDao {
    override suspend fun insert(resto: Resto) {
        TODO("Not yet implemented")
    }

    override suspend fun update(resto: Resto) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(resto: Resto) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }
}