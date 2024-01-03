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

    /**
     * Retrieves a flow of a list of Resto objects.
     *
     * @return A flow of a list of Resto objects. The emitted list represents the available restaurants.
     */
    fun getRestoList(): Flow<List<Resto>>

    /**
     * Retrieves the menu data for a restaurant with the given name.
     * This method is suspended and returns a flow of [MenuData].
     *
     * @param name The name of the restaurant.
     * @return A flow of [MenuData] containing the menu information for the restaurant.
     */
    suspend fun getRestoMenu(name: String): Flow<MenuData>

    /**
     * Sets the favorite status of a restaurant.
     *
     * @param name The name of the restaurant.
     * @param favorite True if the restaurant should be marked as favorite, false otherwise.
     */
    suspend fun setFavoriteResto(name: String, favorite: Boolean)

    /**
     * Retrieves the menu data for a restaurant with the given name.
     * This method is suspended and returns a flow of [StaleAbleData] of [MenuData].
     *
     * @param name The name of the restaurant.
     * @return A flow of [StaleAbleData] containing the menu information for the restaurant.
     */
    suspend fun getRestoMenuSt(name: String): Flow<StaleAbleData<MenuData>>


    /**
     * Suspends the execution and refreshes the list of restaurants.
     *
     * This method is responsible for refreshing the list of restaurants.
     * It suspends the execution until the operation is completed.
     */
    suspend fun refreshRestoList()

    /**
     * Suspends the execution and refreshes the menu for a restaurant with the given name.
     *
     * This method is responsible for refreshing the menu data for a specific restaurant.
     * It suspends the execution until the operation is completed.
     *
     * @param name The name of the restaurant.
     */
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
        val shortName = toShortName(name)
        return menuDao.getMenuData(shortName).transform {
            if (it == null) {
                refreshRestoMenu(name)
            } else {
                if (it.timestamp + 86400000 < System.currentTimeMillis()) {
                    emit(StaleAbleData(it.toMenuData(), true))
                    refreshRestoMenu(name)
                    return@transform
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
        val menuData = restoApiService.getRestoMenuAsFlow(name).first().asDomainObject()
        menuDao.insertMenuData(menuData.toDbMenu())
    }
}


fun toShortName(name: String): String {
    //if it contains "schoonmeersen-" remove it
    if (name.contains("schoonmeersen-")) {
        return name.replace("schoonmeersen-", "")
    }
    return name
}