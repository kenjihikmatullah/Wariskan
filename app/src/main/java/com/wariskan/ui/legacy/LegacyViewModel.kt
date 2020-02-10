package com.wariskan.ui.legacy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LegacyViewModel() : ViewModel() {

    private val _onCalculate = MutableLiveData<Boolean>()
    val onCalculate: LiveData<Boolean>
        get() = _onCalculate

    private val _onShowStats = MutableLiveData<Boolean>()
    val onShowStats: LiveData<Boolean>
        get() = _onShowStats

    fun calculate() {
        _onCalculate.value = true
    }
    fun calculated() {
        _onCalculate.value = false
    }

    fun showStats() {
        _onShowStats.value = true
    }
    fun showedStats() {
        _onShowStats.value = false
    }
}