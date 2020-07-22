package com.commanderpepper.composecounters.repo

import android.app.Application
import android.content.Context
import com.commanderpepper.composecounters.Database
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import counter.CounterQueries
import counter.OperationQueries

object Repository {

    private const val databaseName = "counter.db"

    private var context: Context? = null
    private lateinit var database: Database
    private lateinit var counterQueries: CounterQueries
    private lateinit var operationQueries: OperationQueries

    /**
     * To be called before object use
     */
    fun initialize(application: Application) {
        context = application.applicationContext
        val driver: SqlDriver = AndroidSqliteDriver(Database.Schema, context!!, databaseName)
        database = Database(driver = driver)
        counterQueries = database.counterQueries
        operationQueries = database.operationQueries
    }

    suspend fun deleteCounter(counterId: Long) {
        counterQueries.deleteCounter(counterId)
    }
}


