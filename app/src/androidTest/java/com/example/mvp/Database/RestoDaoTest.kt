package com.example.mvp.Database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.database.RestoDao
import com.example.data.database.RestoDatabase
import com.example.data.database.asDbObject
import com.example.mvp.data.database.*
import com.example.mvp.fake.FakeDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RestoDaoTest {
    private lateinit var menuDao: RestoDao
    private lateinit var database: RestoDatabase

    private val resto1 = FakeDataSource.restoObjectList[0].asDbObject()
    private val resto2 = FakeDataSource.restoObjectList[1].asDbObject()

    private suspend fun addTwoRestos() {
        menuDao.insert(resto1)
        menuDao.insert(resto2)
    }

    private suspend fun addOneResto() {
        menuDao.insert(resto1)
    }

    private suspend fun addOneFavoriteResto() {
        menuDao.insert(resto1.copy(favorite = true))
    }

    private suspend fun addOneNonFavoriteResto() {
        menuDao.insert(resto1.copy(favorite = false))
    }

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context, RestoDatabase::class.java)
            .allowMainThreadQueries().build()

        menuDao = database.restoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun dao_insertOneResto() {
        runBlocking {
            addOneResto()
            val resto = menuDao.getResto(resto1.name).first()
            assert(resto == resto1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun dao_getAllRestos() {
        runBlocking {
            addTwoRestos()
            val menu = menuDao.getRestoList().first()
            assert(menu == listOf(resto1, resto2))
        }
    }

    @Test
    @Throws(Exception::class)
    fun dao_setFavoriteToTrue() {
        runBlocking {
            addOneNonFavoriteResto()
            menuDao.setFavoriteResto(resto1.name, true)
            val resto = menuDao.getResto(resto1.name).first()
            assert(resto.favorite)
        }
    }

    @Test
    @Throws(Exception::class)
    fun dao_setFavoriteToFalse() {
        runBlocking {
            addOneFavoriteResto()
            menuDao.setFavoriteResto(resto1.name, false)
            val resto = menuDao.getResto(resto1.name).first()
            assert(!resto.favorite)
        }
    }


    //Makes sure that the favorite is not overwritten when reading the same resto
    @Test
    @Throws(Exception::class)
    fun dao_doesntOverwriteFavorite() {
        runBlocking {
            addTwoRestos()
            menuDao.setFavoriteResto(resto1.name, true)
            addTwoRestos()
            val resto = menuDao.getResto(resto1.name).first()
            assert(resto.favorite)
        }
    }



}