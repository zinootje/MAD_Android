package com.example.data

import android.content.Context
import com.example.data.database.RestoDatabase
import com.example.network.RestoApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


interface AppContainer {
    val restoRepository: RestoRepository
    //val networkMonitor: NetworkMonitor
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

    override val restoRepository: RestoRepository by lazy { RestoOfflineRepositoryImpl(
        retrofitService,
        RestoDatabase.getDatabase(context).restoDao(),
        RestoDatabase.getDatabase(context).menuDao()
    )
    }

    //override val networkMonitor: NetworkMonitor by lazy { ConnectivityManagerNetworkMonitor(context) }

}