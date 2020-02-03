package com.wariskan.ui.inheritance

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.kenji.waris.database.InheritanceDatabase as Database
import com.wariskan.repository.InheritanceRepository as Repository
import kotlinx.coroutines.CoroutineScope as Scope

class InheritanceViewModel(database: Database) : ViewModel() {

    private val job = Job()
    private val scope = Scope(Main + job)

    private var _id = 1
    val id: Int
        get() = _id

    val repository = Repository(database)

    fun handleExtras(id: Int) {
        _id = id

        if (id != -1) get() else getLatest()
    }

    fun get() {
        scope.launch {
            repository.apply {
                get(id)?.let { inheritance.value = it }
            }
        }
    }

    fun getLatest() {
        scope.launch {
            repository.apply {
                getLatest()?.let {
                    inheritance.value = it
                    _id = it.id
                }
            }
        }
    }

    fun update() {
        scope.launch {
            repository.update()
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}