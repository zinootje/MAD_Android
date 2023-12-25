package com.example.mvp

import com.example.mvp.data.AppContainer
import com.example.mvp.data.RestoRepository
import com.example.mvp.fake.FakeRestoRepository

class FakeAppContainer:AppContainer {
    override val restoRepository: RestoRepository
        get() = FakeRestoRepository()
}