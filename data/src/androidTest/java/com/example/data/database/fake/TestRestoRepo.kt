package com.example.data.database.fake

import com.example.core.StaleAbleData
import com.example.core.model.MenuData
import com.example.core.model.Resto
import com.example.data.RestoRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestRestoRepository : RestoRepository {
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

    override fun getRestoList(): Flow<List<Resto>> = restoListFlow

    override suspend fun getRestoMenu(name: String): Flow<MenuData> {
        return getRestoMenuSt(name).map { it.data }
    }

    override suspend fun setFavoriteResto(name: String, favorite: Boolean) {
        // Modify this method to update the favorite status in your test data, if needed
    }

    override suspend fun getRestoMenuSt(name: String): Flow<StaleAbleData<MenuData>> {
        return restoMenuFlowMap.getOrPut(name) {
            MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        }
    }

    override suspend fun refreshRestoList() {
        // no-op
    }

    override suspend fun refreshRestoMenu(name: String) {
        // no-op
    }

    /**
     * A test-only API to allow controlling the list of restaurants from tests.
     */
    fun sendRestoList(restos: List<Resto>) {
        restoListFlow.tryEmit(restos)
    }




    /**
     * A test-only API to allow controlling the menu of a specific restaurant from tests.
     */
    fun sendRestoMenu(name: String, menuData: StaleAbleData<MenuData>) {
        val menuFlow = restoMenuFlowMap.getOrPut(name) {
            MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        }
        menuFlow.tryEmit(menuData)
    }
}