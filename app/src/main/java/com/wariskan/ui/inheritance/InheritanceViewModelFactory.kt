package com.wariskan.ui.inheritance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.kenji.waris.database.InheritanceDatabase as Database

class InheritanceViewModelFactory(private val database: Database) : Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(InheritanceViewModel::class.java))
            InheritanceViewModel(database) as T
        else
            throw IllegalArgumentException("Couldn't instantitate view model")
    }
}
