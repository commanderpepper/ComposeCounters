package com.commanderpepper.composecounters

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var database: Database

    @Before
    fun init() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val driver = AndroidSqliteDriver(Database.Schema, context, "counter.db")
        database = Database(driver)

    }

    @Test
    fun check_database() {
        Assert.assertTrue(::database.isInitialized)
    }

    @Test
    fun check_for_no_counters() {
        val counters = database.counterQueries.selectAll().executeAsList()
        Assert.assertEquals(0, counters.size)
    }

    @Test
    fun insert_counter_get_counter() {
        database.counterQueries.insertValue(5L)
        val counters = database.counterQueries.selectAll().executeAsList()
        Assert.assertEquals(1, counters.size)

        val firstCounter = counters.first()
        Assert.assertEquals(5L, firstCounter.value)
    }

    @Test
    fun insert_many_counters_get_specific_counter() {
        database.counterQueries.insertValue(1L)
        database.counterQueries.insertValue(2L)

        val counters = database.counterQueries.selectAll().executeAsList()
        Assert.assertEquals(2, counters.size)

        val counter = database.counterQueries.getCounter(1)
        Assert.assertNotNull(counter)
    }

    @Test
    fun insert_child_counters_get_all_child_counters() {
        database.counterQueries.insertCounter(1L, null, 1L, 1L, 1L, "testA")
        database.counterQueries.insertCounter(1L, 1L, 1L, 1L, 1L, "testB")
        database.counterQueries.insertCounter(1L, 1L, 1L, 1L, 1L, "testC")

        val childCounters = database.counterQueries.getChildCounters(1L).executeAsList()
        Assert.assertEquals(2, childCounters.size)
    }

    @Test
    fun insert_counter_get_counter_object() {
        database.counterQueries.insertCounter(3L, null, 4L, 5L, 1L, "testA")
        val counter = database.counterQueries.getCounter(1L).executeAsOne()
        Assert.assertEquals(1L, counter.id)
        Assert.assertEquals(3L, counter.value)
        Assert.assertNull(counter.parentId)
        Assert.assertEquals(4L, counter.threshold)
        Assert.assertEquals(5L, counter.delta)
        val operation = database.operationQueries.getOperation(counter.operationId).executeAsOne()
        Assert.assertEquals("ADD", operation)
        Assert.assertEquals("testA", counter.name)
    }

    @Test
    fun check_operations() {
        val list = database.operationQueries.selectAllOperations().executeAsList()
        Assert.assertEquals(4, list.size)

        val firstOperation = database.operationQueries.getOperation(1).executeAsOne()
        Assert.assertEquals("ADD", firstOperation)
    }
}