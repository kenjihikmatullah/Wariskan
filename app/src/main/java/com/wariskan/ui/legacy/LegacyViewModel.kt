package com.wariskan.ui.legacy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LegacyViewModel() : ViewModel() {

    private val _onCalculate = MutableLiveData<Boolean>(false)
    val onCalculate: LiveData<Boolean>
        get() = _onCalculate

    private val _onShowStats = MutableLiveData<Boolean>(false)
    val onShowStats: LiveData<Boolean>
        get() = _onShowStats

    fun calculate() {
        _onCalculate.value = _onCalculate.value?.not()
    }

    fun showStats() {
        _onShowStats.value = _onShowStats.value?.not()
    }
}