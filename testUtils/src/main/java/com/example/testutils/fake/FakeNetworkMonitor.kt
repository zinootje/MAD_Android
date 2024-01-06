package com.example.testutils.fake

import com.example.data.util.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Represents a fake implementation of the [NetworkMonitor] interface for testing purposes that always returns true. */
class FakeNetworkMonitor : NetworkMonitor {

    /**
     * FAKE VERSION ALWAYS RETURNS TRUE
     * Represents the online status of the device.
     *
     * This property is a [Flow] that emits a [Boolean] value indicating whether the device is currently online or not.
     *
     * @see NetworkMonitor
     */
    override val isOnline: Flow<Boolean>
        get() = flowOf(true)
}