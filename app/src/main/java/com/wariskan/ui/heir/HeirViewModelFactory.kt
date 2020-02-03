package com.wariskan.ui.heir

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class HeirViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HeirViewModel::class.java))
            HeirViewModel() as T
        else
            throw IllegalArgumentException("Couldn't instantiate heirs view model")
    }
}