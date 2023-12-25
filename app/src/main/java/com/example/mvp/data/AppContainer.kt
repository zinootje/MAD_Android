package com.example.mvp.data

import android.content.Context
import com.example.mvp.network.RestoApiService
import com.example.mvp.data.database.MenuDatabase
import com.example.mvp.data.database.RestoDatabase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


interface AppContainer {
    val restoRepository: RestoRepository
    //TODO remove old maybe
    val oflRepository: RestoRepository
}

class DefaultAppContainer(
    private val context: Context
) : AppContainer {
    private val baseUrl = "https://horesto-api-b1545d045cdf.herokuapp.com/"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(
            Json.asConverterFactory("application/json".toMediaType())
        )
        .baseUrl(baseUrl)
        .build()

    private  val retrofitService: RestoApiService by lazy { retrofit.create(RestoApiService::class.java)  }

    override val restoRepository: RestoRepository by lazy { RestoRepositoryImpl(retrofitService)
    }

    override val oflRepository: RestoRepository by lazy {
        RestoOfflineRepositoryImpl(
            retrofitService,
            RestoDatabase.getDatabase(context).restoDao(),
            MenuDatabase.getDatabase(context).menuDao()
        )
    }
}