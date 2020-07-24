package com.commanderpepper.composecounters.MainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.commanderpepper.composecounters.repo.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CounterViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    fun getChildCounterCount(parentId: Long): StateFlow<Long> {
        return MutableStateFlow(repository.getChildCounterCount(parentId))
    }
}