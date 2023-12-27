package com.example.data

import android.content.Context
import com.example.data.database.MenuDatabase
import com.example.data.database.RestoDatabase
import com.example.network.RestoApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


interface AppContainer {
    val restoRepository: RestoRepository
}

class DefaultAppContainer(
    private val context: Context
) : AppContainer {
    private val baseUrl = "https://horesto-api-b1545d045cdf.herokuapp.com/"
    var json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(
            //ignoreUnknownKeys = true


            Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaType())
        )
        .baseUrl(baseUrl)
        .build()

    private  val retrofitService: RestoApiService by lazy { retrofit.create(RestoApiService::class.java)  }

    override val restoRepository: RestoRepository by lazy { RestoOfflineRepositoryImpl(
        retrofitService,
        RestoDatabase.getDatabase(context).restoDao(),
        MenuDatabase.getDatabase(context).menuDao()
    )
    }

}