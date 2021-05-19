package com.commanderpepper.composecounters.Repository

import com.commanderpepper.composecounters.Utils.CounterResult
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import counter.Counter
import counter.CounterQueries
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class Repository @Inject constructor(private val counterQueries: CounterQueries) {

    fun getParentCounters() : Flow<Result<List<Counter>>> {
        return flow {
            val meow = counterQueries.getParentCounters().executeAsList()
            emit(Result.success(meow))
        }.catch {
            throwable -> emit(Result.failure(throwable))
        }
    }

    fun getParentCounterTwo(): SharedFlow<Result<List<Counter>>>{
        return MutableSharedFlow()
    }

    /**
     * Action when user pressed add
     */
    suspend fun add(counterId: Long){
        val counter = counterQueries.getCounter(counterId).executeAsOne()

        val newCounterValue = counter.value + counter.delta
        val newThresholdValue = if(newCounterValue >= counter.threshold) counter.threshold + counter.delta else  counter.threshold

        if(newThresholdValue != counter.threshold){
            if(counter.parentId != null){
                add(counter.parentId)
            }
            counterQueries.updateCounter(counter.parentId, newThresholdValue, newCounterValue)
        }
    }

    /**
     * Action when user pressed subtraction
     */
    suspend fun subtraction(counterId: Long){
        val counter = counterQueries.getCounter(counterId).executeAsOne()

        val newCounterValue = counter.value - counter.delta
        val newThresholdValue = if(newCounterValue <= counter.threshold) counter.threshold - counter.delta else  counter.threshold

        if(newThresholdValue != counter.threshold){
            if(counter.parentId != null){
                subtraction(counter.parentId)
            }
            counterQueries.updateCounter(counter.parentId, newThresholdValue, newCounterValue)
        }
    }

}