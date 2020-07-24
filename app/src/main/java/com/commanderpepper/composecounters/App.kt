package com.commanderpepper.composecounters

import android.app.Application
import com.commanderpepper.composecounters.MainActivity.MainActivity
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import counter.CounterQueries
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(modules = [CounterQueriesModule::class])
interface ApplicationComponent {

    val counterQueries: CounterQueries

    fun inject(activity: MainActivity)
}

@Module
class CounterQueriesModule(val database: Database) {

    @Provides
    fun counterQueriesProvider(): CounterQueries {
        return database.counterQueries
    }
}

class App : Application() {

    private val databaseName = "counter.db"
    private lateinit var driver: SqlDriver
    private lateinit var database: Database

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        driver = AndroidSqliteDriver(Database.Schema, this.applicationContext, databaseName)
        database = Database(driver)

        val counterQueriesModule = CounterQueriesModule(database = database)

        applicationComponent = DaggerApplicationComponent.builder().counterQueriesModule(counterQueriesModule).build()
    }


}