package com.example.data.database.fake

import com.example.data.AppContainer
import com.example.data.RestoRepository
import com.example.data.util.NetworkMonitor

class FakeAppContainer(
    override val restoRepository: RestoRepository = FakeRestoRepository(),
    override val networkMonitor: NetworkMonitor = FakeNetworkMonitor()
) : AppContainer {

}