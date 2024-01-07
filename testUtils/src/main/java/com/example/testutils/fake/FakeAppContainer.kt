package com.example.testutils.fake

import com.example.data.AppContainer
import com.example.data.RestoRepository

class FakeAppContainer(
    override val restoRepository: RestoRepository = FakeRestoRepository(),
    //override val networkMonitor: NetworkMonitor = FakeNetworkMonitor()
) : AppContainer