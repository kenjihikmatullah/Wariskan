package com.wariskan.ui.legacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory

class LegacyViewModelFactory() : Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LegacyViewModel::class.java))
            LegacyViewModel() as T
        else
            throw IllegalArgumentException("Couldn't instantitate legacy view model")
    }
}