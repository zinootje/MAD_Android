package com.example.data

import com.example.core.StaleAbleData
import com.example.core.model.MenuData
import com.example.core.model.Resto
import com.example.data.database.*
import com.example.network.RestoApiService
import com.example.network.asDomainObject
import com.example.network.getRestoListAsFlow
import com.example.network.getRestoMenuAsFlow
import kotlinx.coroutines.flow.*

interface RestoRepository {

    fun getRestoList(): Flow<List<Resto>>

    suspend fun getRestoMenu(name: String): Flow<MenuData>

    suspend fun setFavoriteResto(name: String, favorite: Boolean)

    suspend fun getRestoMenuSt(name: String): Flow<StaleAbleData<MenuData>>


    suspend fun refreshRestoList()

    suspend fun refreshRestoMenu(name: String)


}

class RestoOfflineRepositoryImpl(
    private val restoApiService: RestoApiService,
    private val restoDao: RestoDao,
    private val menuDao: MenuDao
) : RestoRepository {
    override fun getRestoList(): Flow<List<Resto>> {
        return restoDao.getRestoList().map { it.asDomainObject() }.onEach {
            if (it.isEmpty()) {
                refreshRestoList()
            }
        }
    }


    private suspend fun getRestoMenuInternal(name: String): Flow<StaleAbleData<MenuData>> {
        //TODO move logic to different function
        //Needed because of the way the api works
        var shortName = ToShortName(name)
        return menuDao.getMenuData(shortName).transform {
            if (it == null) {
                refreshRestoMenu(name)
            } else {
                if (it.timestamp + 86400000 < System.currentTimeMillis()) {
                    refreshRestoMenu(name)
                    return@transform emit(StaleAbleData(it.toMenuData(), true))
                } else {
                    return@transform emit(StaleAbleData(it.toMenuData(), false))
                }
            }
        }
    }

    override suspend fun getRestoMenuSt(name: String): Flow<StaleAbleData<MenuData>> {
        return getRestoMenuInternal(name)
    }


    override suspend fun getRestoMenu(name: String): Flow<MenuData> {
        return getRestoMenuInternal(name).map { it.data }
    }


    override suspend fun setFavoriteResto(name: String, favorite: Boolean) {
        restoDao.setFavoriteResto(name, favorite)
    }
    override suspend fun refreshRestoList() {
        //TODO maybe error handling
        val restos = restoApiService.getRestoListAsFlow().map { it -> it.map { Resto(it) } }.first()
        for (resto in restos) {
            restoDao.insert(resto.asDbObject())
        }
    }

    override suspend fun refreshRestoMenu(name: String) {
        var menuData = restoApiService.getRestoMenuAsFlow(name).first().asDomainObject()
        menuDao.insertMenuData(menuData.toDbMenu())
    }
}


fun ToShortName(name: String): String {
    //if it contains "schoonmeersen-" remove it
    if (name.contains("schoonmeersen-")) {
        return name.replace("schoonmeersen-", "")
    }
    return name
}