package com.wariskan.ui.addedit

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kenji.waris.database.Inheritance
import com.kenji.waris.model.Deceased
import com.kenji.waris.model.Gender.MALE
import com.kenji.waris.model.Heir
import com.kenji.waris.model.Position
import com.kenji.waris.model.Position.DAD
import com.kenji.waris.model.Position.DECEASED
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.kenji.waris.database.InheritanceDatabase as Database
import com.wariskan.repository.InheritanceRepository as Repository
import kotlinx.coroutines.CoroutineScope as Scope

class AddEditViewModel(database: Database) : ViewModel() {

    private val job = Job()
    private val scope = Scope(Main + job)

    private val _onSave = MutableLiveData<Boolean>()
    val onSave: LiveData<Boolean>
        get() = _onSave

    private val _onDelete = MutableLiveData<Boolean>()
    val onDelete: LiveData<Boolean>
        get() = _onDelete

    private var _id = -1
    val id: Int
        get() = _id

    private var _position = DAD
    val position: Position
        get() = _position

    private var _order = -1
    val order: Int
        get() = _order

    val isEditing: Boolean
        get() {
            return (position == DECEASED && id != -1) || (position != DECEASED && order != -1)
        }

    var spinnerStatus = -1
    var spinnerFaith = -1
    var spinnerGender = -1
    var spinnerOne = -1
    var spinnerTwo = -1

    fun handleArguments(
        id: Int,
        position: Position,
        order: Int
    ) {
        _id = id
        _position = position
        _order = order

        /*
         * Existing
         */
        if (id != -1) {
            get()
        }
    }

    val repository = Repository(database)
    val inheritance: LiveData<Inheritance>
        get() = repository.inheritance


    fun addEditDeceased(deceased: Deceased) {
        if (id == -1) {
            val new = Inheritance().also {
                it.deceased = deceased
            }
            repository.inheritance.value = new
        } else {
            repository.inheritance.value?.let {
                it.deceased = deceased
                if (deceased.gender == MALE) {
                    it.husband.clear()
                } else {
                    it.wives.clear()
                }
            }

        }
    }

    fun addEditHeir(heir: Heir, context: Context) {
        inheritance.value?.apply {
            setHeir(position, order, heir, context)
        }
    }

    fun insert() {
        scope.launch {
            repository.insert()
        }
    }

    fun update() {
        scope.launch {
            repository.update()
        }
    }

    private fun get() {
        scope.launch {
            repository.apply {
                get(id)?.let { inheritance.value = it }
            }
        }
    }

    fun save() {
        _onSave.value = true
    }

    fun saved() {
        _onSave.value = false
    }

    fun delete() {
        _onDelete.value = true
    }

    fun deleted() {
        _onDelete.value = false
    }
}
