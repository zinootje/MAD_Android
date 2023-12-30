package com.example.mvp.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.database.MenuDao
import com.example.data.database.MenuDatabase
import com.example.data.database.toDbMenu
import com.example.mvp.fake.FakeDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MenuDaoTest {
    private lateinit var menuDao: MenuDao
    private lateinit var database: MenuDatabase

    private val menu1 = FakeDataSource.restoMenu.toDbMenu()
    private val menu2 = FakeDataSource.restoMenu2.toDbMenu()

    private suspend fun insertMenus() {
        menuDao.insertMenuData(menu1)
        menuDao.insertMenuData(menu2)
    }

    private suspend fun addOneMenu() {
        menuDao.insertMenuData(menu1)
    }

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context, MenuDatabase::class.java
        ).allowMainThreadQueries().build()

        menuDao = database.menuDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun dao_insertMenuData() {
        runBlocking {
            addOneMenu()
            val menu = menuDao.getMenuData(menu1.location).first()
            assert(menu == menu1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun dao_getMenuData() {
        runBlocking {
            insertMenus()
            val menu = menuDao.getMenuData(menu1.location).first()
            assert(menu == menu1)
        }
    }


}