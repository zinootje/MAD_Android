package com.example.testutils.fake

import com.example.core.StaleAbleData
import com.example.core.model.MenuData
import com.example.core.model.Resto
import com.example.data.RestoRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map


sealed class ResultOrError<out T> {
    data class Result<out T>(val data: T) : ResultOrError<T>()
    data class Error(val error: Throwable) : ResultOrError<Nothing>()
}

class TestRestoRepository : RestoRepository {


    /**
     * The backing hot flow for the list of restaurants for testing.
     */
    private val restoListFlow: MutableSharedFlow<ResultOrError<List<Resto>>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * The backing hot flow for the menu of a specific restaurant for testing.
     * The key is the restaurant name.
     */
    private val restoMenuFlowMap = mutableMapOf<String, MutableSharedFlow<ResultOrError<StaleAbleData<MenuData>>>>()

    override fun getRestoList(): Flow<List<Resto>> = restoListFlow.map {
        when (it) {
            is ResultOrError.Result -> it.data
            is ResultOrError.Error -> throw it.error
        }
    }

    override suspend fun getRestoMenu(name: String): Flow<MenuData> {
        return getRestoMenuSt(name).map { it.data }
    }

    override suspend fun setFavoriteResto(name: String, favorite: Boolean) {
        // Modify this method to update the favorite status in your test data, if needed
    }

    override suspend fun getRestoMenuSt(name: String): Flow<StaleAbleData<MenuData>> {
        return restoMenuFlowMap.getOrPut(name) {
            MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        }.map { resultOrError ->
            when (resultOrError) {
                is ResultOrError.Result -> resultOrError.data
                is ResultOrError.Error -> throw resultOrError.error
            }
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
        restoListFlow.tryEmit(ResultOrError.Result(restos))
    }

    /**
     * A test-only API to throw an error int flow returned by [getRestoList].
     */
    fun throwErrorInRestoList(throwable: Throwable) {
        restoListFlow.tryEmit(ResultOrError.Error(throwable))
    }

    /**
     * A test-only API to throw an error int flow returned by [getRestoMenu].
     */
    fun throwErrorInRestoMenu(name: String, throwable: Throwable) {
        restoMenuFlowMap.getOrPut(name) {
            MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        }.tryEmit(ResultOrError.Error(throwable))
    }


    /**
     * A test-only API to allow controlling the menu of a specific restaurant from tests.
     */
    fun sendRestoMenu(name: String, menuData: StaleAbleData<MenuData>) {
        val menuFlow = restoMenuFlowMap.getOrPut(name) {
            MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        }
        menuFlow.tryEmit(ResultOrError.Result(menuData))
    }
}