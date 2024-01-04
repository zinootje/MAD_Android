package com.example.testutils.fake

import com.example.data.database.MenuDao
import com.example.data.database.MenuDataEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestMenuDao : MenuDao {

    /**
     * The backing hot flow for the menu of a specific restaurant for testing.
     * The key is the restaurant name.
     */
    private val restoMenuFlowMap = mutableMapOf<String, MutableSharedFlow<MenuDataEntity>>()

    override suspend fun insertMenuData(menuData: MenuDataEntity) {
        restoMenuFlowMap.getOrPut(menuData.location) {
            MutableSharedFlow(replay = 1)
        }.emit(MenuDataEntity(menuData.location, menuData.days))
    }

    override fun getMenuData(location: String): Flow<MenuDataEntity?> {
        return restoMenuFlowMap.getOrPut(location) {
            MutableSharedFlow(replay = 1)
        }
    }

    /**
     * A test-only API to set the menu data for a specific restaurant.
     *
     * @param menu The menu data to be set.
     */
    fun setMenuData(menu: MenuDataEntity) {
        restoMenuFlowMap.getOrPut(menu.location) {
            MutableSharedFlow(replay = 1)
        }.tryEmit(menu)
    }
}