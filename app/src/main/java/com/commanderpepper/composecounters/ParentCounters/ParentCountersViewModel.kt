package com.commanderpepper.composecounters.ParentCounters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commanderpepper.composecounters.Repository.Repository
import com.commanderpepper.composecounters.Utils.CounterResult
import counter.Counter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ParentCountersViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

//    private val _parentCounters: MutableSharedFlow<Result<List<Counter>>> = MutableSharedFlow()
    lateinit var parentCounters: SharedFlow<Result<List<Counter>>>

//    private val _counterResponse: MutableStateFlow<CounterResult<List<Any>>> = MutableStateFlow(CounterResult.Loading("Loading"))
//    val counterResponse: StateFlow<CounterResult<List<Any>>> = _counterResponse

    init {
        viewModelScope.launch {
            parentCounters = repository.getParentCounters().shareIn(viewModelScope, SharingStarted.Lazily, 0)
        }
    }
}