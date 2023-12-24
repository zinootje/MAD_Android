package com.example.mvp.Network

import com.example.mvp.model.MenuData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET
import retrofit2.http.Path

interface RestoApiService {

    @GET("restos")
    suspend fun getRestoList(): List<String>

    @GET("restos/{name}")
    suspend fun getRestoMenu(@Path("name") name: String): ApiRestoMenu
}

fun RestoApiService.getRestoListAsFlow(): Flow<List<String>> = flow {
    emit(getRestoList())
}

fun RestoApiService.getRestoMenuAsFlow(name: String): Flow<ApiRestoMenu> = flow {
    emit(getRestoMenu(name))
}