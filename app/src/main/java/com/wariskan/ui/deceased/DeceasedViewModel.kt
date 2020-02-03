package com.wariskan.ui.deceased

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DeceasedViewModel(): ViewModel() {

    private val _onEdit = MutableLiveData<Boolean>()
    val onEdit: LiveData<Boolean>
        get() = _onEdit

    fun edit() {
        _onEdit.value = true
    }
    fun edited() {
        _onEdit.value = false
    }
}