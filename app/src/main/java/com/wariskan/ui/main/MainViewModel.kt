package com.wariskan.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kenji.waris.database.Inheritance
import com.wariskan.recyclerview.InheritanceAdapter.Listener
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.kenji.waris.database.InheritanceDatabase as Database
import com.wariskan.recyclerview.InheritanceAdapter as Adapter
import kotlinx.coroutines.CoroutineScope as Scope

class MainViewModel(private val database: Database) : ViewModel() {

    private val job = Job()
    private val scope = Scope(Main + job)

    private val _onAdd = MutableLiveData<Boolean>()
    val onAdd: LiveData<Boolean>
        get() = _onAdd

    private val _onOpen = MutableLiveData<Boolean>()
    val onOpen: LiveData<Boolean>
        get() = _onOpen

    private var _id = -1
    val id: Int
        get() = _id

    val listener = Listener(
        {
            id -> open(id)
        }, {
            inheritance -> delete(inheritance)
        }
    )
    val adapter = Adapter(listener)
    val inheritances = database.dao.getAll()


    fun add() {
        _onAdd.value = true
    }
    fun added() {
        _onAdd.value = false
    }

    fun open(id: Int) {
        _id = id
        _onOpen.value = true
    }
    fun opened() {
        _onOpen.value = false
    }

    fun delete(inheritance: Inheritance) {
        scope.launch {
            deleteFromDatabase(inheritance)
        }
    }
    private suspend fun deleteFromDatabase(inheritance: Inheritance) {
        withContext(IO) {
            database.dao.delete(inheritance)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
