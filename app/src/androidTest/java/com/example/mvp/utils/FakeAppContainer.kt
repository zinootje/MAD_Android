package com.example.mvp.utils

import com.example.data.AppContainer
import com.example.data.RestoRepository
import com.example.mvp.fake.FakeRestoRepository

class FakeAppContainer : AppContainer {
    override val restoRepository: RestoRepository
        get() = FakeRestoRepository()
}