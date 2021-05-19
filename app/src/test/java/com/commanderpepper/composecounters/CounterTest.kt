package com.commanderpepper.composecounters

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import counter.CounterQueries
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.internal.runners.JUnit4ClassRunner
import org.junit.runner.RunWith

@RunWith(JUnit4ClassRunner::class)
class CounterTest {

    lateinit var driver: SqlDriver
    lateinit var counterQueries: CounterQueries

    @Before
    fun before(){
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        val database = Database(driver)
        Database.Schema.create(driver)
        counterQueries = database.counterQueries
    }

    @Test
    fun CounterQueries_INSERT_COUNTER_RETRIEVE_COUNTER(){
        counterQueries.insertCounter(
            0,
            null,
            1,
            1,
            1,
            "add a counter"
        )

        val retrievedCounter = counterQueries.getCounter(1).executeAsOne()

        Assert.assertNotNull(retrievedCounter)
        Assert.assertEquals(retrievedCounter.value, 0)
    }

    @Test
    fun CounterQueries_INSERT_VALUE_RETRIEVE_COUNTER(){
        counterQueries.insertValue(1L)

        val retrievedCounter = counterQueries.getCounter(1).executeAsOne()

        Assert.assertNotNull(retrievedCounter)
        Assert.assertEquals(retrievedCounter.value, 1L)
    }

    @Test
    fun CounterQueries_INSERT_PARENT_AND_CHILD_COUNTER_RETRIEVE_COUNTER(){
        counterQueries.insertValue(1L)

        counterQueries.insertCounter(
            0,
            1L,
            1L,
            1L,
            1L,
            "parent"
        )

        val childCounters = counterQueries.getChildCounters(1).executeAsList()

        Assert.assertNotNull(childCounters)
        Assert.assertTrue(childCounters.isNotEmpty())
        Assert.assertEquals(childCounters.first().value, 0)
    }

    @Test
    fun CounterQueries_INSERT_MANY_PARENT_COUNTERS_RETRIEVE_PARENT_COUNTERS(){
        repeat(5){
            counterQueries.insertCounter(
                0,
                null,
                1L,
                1L,
                1L,
                "parent $it"
            )
        }

        val parentCounters = counterQueries.getParentCounters().executeAsList()

        Assert.assertNotNull(parentCounters)
        Assert.assertTrue(parentCounters.isNotEmpty())
        Assert.assertTrue(parentCounters.size == 5)
    }

    @Test
    fun CounterQueries_INSERT_COUNTER_DELETE_COUNTER(){
        counterQueries.insertCounter(
            0,
            null,
            1,
            1,
            1,
            "add a counter"
        )

        counterQueries.deleteCounter(1L)

        val parentCounters = counterQueries.getParentCounters().executeAsList()

        Assert.assertTrue(parentCounters.isEmpty())
    }
}