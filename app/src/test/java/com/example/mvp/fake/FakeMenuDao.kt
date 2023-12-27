package com.example.mvp.fake

import com.example.data.database.MenuDao
import com.example.data.database.MenuDataEntity
import com.example.data.database.toDbMenu
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMenuDao : MenuDao {
    override suspend fun insertMenuData(menuData: MenuDataEntity) {
        TODO("Not yet implemented")
    }

    override fun getMenuData(location: String): Flow<MenuDataEntity?> {
        return flow {
            emit(FakeDataSource.restoMenu.toDbMenu())
        }
    }

}