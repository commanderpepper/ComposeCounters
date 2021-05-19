package com.commanderpepper.composecounters.Utils

import counter.Counter
import counter.CounterQueries

fun Counter.insert(counterQueries: CounterQueries){
    counterQueries.insertCounter(this.value, this.parentId, this.threshold, this.delta, this.operationId, this.name)
}