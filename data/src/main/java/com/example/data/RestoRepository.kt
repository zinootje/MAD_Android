package com.example.data

import com.example.core.model.MenuData
import com.example.core.model.Resto
import com.example.data.database.MenuDao
import com.example.data.database.RestoDao
import com.example.data.database.asDbObject
import com.example.data.database.asDomainObject
import com.example.data.database.toDbMenu
import com.example.data.database.toMenuData
import com.example.network.RestoApiService
import com.example.network.asDomainObject
import com.example.network.getRestoListAsFlow
import com.example.network.getRestoMenuAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

interface RestoRepository {

    fun getRestoList(): Flow<List<Resto>>

    suspend fun getRestoMenu(name: String): Flow<MenuData>

    suspend fun setFavoriteResto(name: String, favorite: Boolean)



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

    override suspend fun getRestoMenu(name: String): Flow<MenuData> {
        //TODO move logic to different function
        //Needed because of the way the api works
        var shortName = ToShortName(name)
        return menuDao.getMenuData(shortName).onEach {
            if (it == null) {
                refreshRestoMenu(name)
            }
        }.filterNotNull().map { it.toMenuData() }
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