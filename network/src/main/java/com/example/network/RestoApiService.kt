package com.example.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET
import retrofit2.http.Path

interface RestoApiService {

    /**
     * Retrieves a list of restaurant names from the API.
     *
     * @return The list of restaurant names.
     */
    @GET("restos")
    suspend fun getRestoList(): List<String>

    /**
     * Retrieves the menu of a specific restaurant from the API.
     *
     * @param name The name of the restaurant.
     * @return The menu of the restaurant.
     */
    @GET("restos/{name}")
    suspend fun getRestoMenu(@Path("name") name: String): ApiRestoMenu


}

/**
 * Retrieves a list of restaurant names as a flow from the REST API service.
 * @see RestoApiService.getRestoList
 *
 * @return A flow emitting a list of restaurant names.
 */
fun RestoApiService.getRestoListAsFlow(): Flow<List<String>> = flow {
    emit(getRestoList())
}

/**
 * Retrieves the menu of a specific restaurant from the API as a Kotlin Flow.
 * @see RestoApiService.getRestoMenu
 *
 * @param name The name of the restaurant.
 * @return A flow emitting the menu of the restaurant as a [ApiRestoMenu] object.
 */
fun RestoApiService.getRestoMenuAsFlow(name: String): Flow<ApiRestoMenu> = flow {
    emit(getRestoMenu(name))
}