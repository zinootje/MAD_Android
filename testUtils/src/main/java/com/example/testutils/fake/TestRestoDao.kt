package com.example.testutils.fake

import com.example.core.StaleAbleData
import com.example.core.model.MenuData
import com.example.data.database.Resto
import com.example.data.database.RestoDao
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestRestoDao : RestoDao {

    /**
     * The backing hot flow for the list of restaurants for testing.
     */
    private val restoListFlow: MutableSharedFlow<List<Resto>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * The backing hot flow for the menu of a specific restaurant for testing.
     * The key is the restaurant name.
     */
    private val restoMenuFlowMap = mutableMapOf<String, MutableSharedFlow<StaleAbleData<MenuData>>>()

    override suspend fun insert(resto: Resto) {
        val list: List<Resto> = restoListFlow.replayCache.firstOrNull()?.filter { it.name != resto.name } ?: emptyList()

        restoListFlow.tryEmit(list + resto)
    }

    override suspend fun update(resto: Resto) {
        // no-op
    }

    override suspend fun delete(resto: Resto) {
        // no-op
    }

    override fun getResto(name: String): Flow<Resto> {
        return restoListFlow.map {
            it.first { resto -> resto.name == name }
        }
    }

    override fun getRestoList(): Flow<List<Resto>> {
        return restoListFlow
    }

    override suspend fun setFavoriteResto(name: String, favorite: Boolean) {
        val list: List<Resto> = restoListFlow.replayCache.firstOrNull()?.map {
            if (it.name == name) {
                it.copy(favorite = favorite)
            } else {
                it
            }
        } ?: emptyList()
        if (list.isNotEmpty()) {
            restoListFlow.tryEmit(list)
        }
    }

    override suspend fun deleteNotInList(restoList: List<String>) {
        val list: List<Resto> = restoListFlow.replayCache.firstOrNull()?.filter { restoList.contains(it.name) }
            ?: emptyList()
        if (list.isNotEmpty()) {
            restoListFlow.tryEmit(list)
        }
    }

    /**
     * A test-only API to set the list of restaurants.
     *
     * @param restoList The list of restaurants to be set.
     */
    fun setRestoList(restoList: List<Resto>) {
        restoListFlow.tryEmit(restoList)
    }

    /**
     * A test-only API to set the menu of a specific restaurant.
     *
     * @param name The name of the restaurant.
     * @param menuData The menu data to be set. It should contain the menu location and a list of days with their respective menus.
     */
    fun setRestoMenu(name: String, menuData: StaleAbleData<MenuData>) {
        restoMenuFlowMap.getOrPut(name) {
            MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        }.tryEmit(menuData)
    }
}