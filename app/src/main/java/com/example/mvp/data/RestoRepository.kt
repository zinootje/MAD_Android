package com.example.mvp.data

import com.example.mvp.Network.RestoApiService
import com.example.mvp.Network.asDomainObject
import com.example.mvp.Network.getRestoListAsFlow
import com.example.mvp.Network.getRestoMenuAsFlow
import com.example.mvp.data.room.RestoDao
import com.example.mvp.data.room.asDomainObject
import com.example.mvp.model.MenuData
import com.example.mvp.model.Resto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

interface RestoRepository {

    fun getRestoList(): Flow<List<Resto>>

    suspend fun getRestoMenu(name: String): Flow<MenuData>

    suspend fun favoriteResto(name: String)

    suspend fun unFavoriteResto(name: String)

    suspend fun refreshRestoList()


}


class RestoRepositoryImpl(private val RestoApiService: RestoApiService) : RestoRepository {
    override fun getRestoList(): Flow<List<Resto>> {
        return RestoApiService.getRestoListAsFlow().map { it.map { Resto(it) } }
    }

    override suspend fun getRestoMenu(name: String): Flow<MenuData> {
        return RestoApiService.getRestoMenuAsFlow(name).map { it.asDomainObject() }
    }

    override suspend fun favoriteResto(name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun unFavoriteResto(name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun refreshRestoList() {
        TODO("Not yet implemented")
    }

}

class RestoOfflineRepositoryImpl(
    private val RestoApiService: RestoApiService,
    private val RestoDao: RestoDao
) : RestoRepository {
    override fun getRestoList(): Flow<List<Resto>> {

        var ll = RestoApiService.getRestoListAsFlow().map { it.map { Resto(it) } }

        return RestoDao.getRestoList().map { it.asDomainObject() }.onEach {
            if (it.isEmpty()) {
                refreshRestoList()
            }
        }
    }

    override suspend fun getRestoMenu(name: String): Flow<MenuData> {
        return RestoApiService.getRestoMenuAsFlow(name).map { it.asDomainObject() }
    }

    override suspend fun favoriteResto(name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun unFavoriteResto(name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun refreshRestoList() {
        TODO("Not yet implemented")
    }

}