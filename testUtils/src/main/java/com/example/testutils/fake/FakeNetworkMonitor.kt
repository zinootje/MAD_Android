package com.example.testutils.fake

import com.example.data.util.NetworkMonitor
import kotlinx.coroutines.flow.Flow

class FakeNetworkMonitor : NetworkMonitor {
    override val isOnline: Flow<Boolean>
        get() = TODO("Not yet implemented")
}