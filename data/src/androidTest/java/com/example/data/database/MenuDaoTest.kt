package com.example.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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
    private lateinit var database: RestoDatabase

    private val menu1 = com.example.testutils.fake.FakeDataSource.restoMenu.toDbMenu()
    private val menu2 = com.example.testutils.fake.FakeDataSource.restoMenu2.toDbMenu()

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
            context, RestoDatabase::class.java
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
