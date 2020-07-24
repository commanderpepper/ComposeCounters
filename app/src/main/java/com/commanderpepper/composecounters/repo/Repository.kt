package com.commanderpepper.composecounters.repo

import counter.CounterQueries
import javax.inject.Inject
import javax.inject.Singleton

class Repository @Inject constructor(private val counterQueries: CounterQueries) {
    fun getChildCounterCount(parentId: Long): Long {
        return counterQueries.getChildCountersCount(parentId).executeAsOneOrNull() ?: 0
    }
}


