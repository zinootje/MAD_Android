package com.example.testutils.fake

import com.example.network.ApiRestoMenu
import com.example.network.RestoApiService
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class TestRestoApiService : RestoApiService {

    /**
     * The backing hot flow for the list of restaurants for testing.
     */
    private val restoListFlow: MutableSharedFlow<List<String>> =
        MutableSharedFlow(replay = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1)

    /**
     * The backing hot flow for the menu of a specific restaurant for testing.
     * The key is the restaurant name.
     */
    private val restoMenuFlowMap = mutableMapOf<String, MutableSharedFlow<ApiRestoMenu>>()
    override suspend fun getRestoList(): List<String> {
        return restoListFlow.first()
    }

    override suspend fun getRestoMenu(name: String): ApiRestoMenu {
        return restoMenuFlowMap.getOrPut(name) {
            MutableSharedFlow(replay = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1)
        }.first()
    }

    /**
     * A test-only API to set the list of restaurants.
     *
     * @param restoList The list of restaurants to be set.
     */
    fun setRestoList(restoList: List<String>) {
        restoListFlow.tryEmit(restoList)
    }

    /**
     * A test-only API to set the menu of a specific restaurant.
     *
     * @param name The name of the restaurant.
     * @param menu The menu of the restaurant.
     */
    fun setRestoMenu(name: String, menu: ApiRestoMenu) {
        restoMenuFlowMap.getOrPut(name) {
            MutableSharedFlow(replay = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1)
        }.tryEmit(menu)
    }
}