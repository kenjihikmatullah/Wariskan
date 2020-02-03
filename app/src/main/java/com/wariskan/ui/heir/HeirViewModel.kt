package com.wariskan.ui.heir

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kenji.waris.model.Position
import com.kenji.waris.model.Position.DAD
import com.wariskan.ui.heir.HeirAdapter.Listener
import com.wariskan.util.POSITION

class HeirViewModel() : ViewModel() {

    private val _onAdd = MutableLiveData<Boolean>()
    val onAdd: LiveData<Boolean>
        get() = _onAdd

    private val _onEdit = MutableLiveData<Boolean>()
    val onEdit: LiveData<Boolean>
        get() = _onEdit

    private val _onDelete = MutableLiveData<Boolean>()
    val onDelete: LiveData<Boolean>
        get() = _onDelete

    val listener = Listener(
        editUnit = { order -> edit(order) },
        deleteUnit = { order -> delete(order) }
        )
    val adapter = HeirAdapter(listener)

    private var _position = DAD
    val position: Position
        get() = _position

    private var _order = -1
    val order: Int
        get() = _order

    fun handleArguments(args: Bundle) {
        args.getSerializable(POSITION)?.let {
            _position = it as Position
        }
    }

    fun add() {
        _onAdd.value = true
    }
    fun added() {
        _onAdd.value = false
    }

    fun edit(order: Int) {
        _order = order
        _onEdit.value = true
    }
    fun edited() {
        _onEdit.value = false
    }

    fun delete(order: Int) {
        _order = order
        _onDelete.value = true
    }
    fun deleted() {
        _onDelete.value = false
    }

}