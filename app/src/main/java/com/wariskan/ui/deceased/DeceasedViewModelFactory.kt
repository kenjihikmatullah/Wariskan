package com.wariskan.ui.deceased

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class DeceasedViewModelFactory()
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DeceasedViewModel::class.java))
            DeceasedViewModel() as T
        else
            throw IllegalArgumentException("Couldn't instantitate deceased view model.")
    }
}