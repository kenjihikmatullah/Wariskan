package com.wariskan.ui.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.kenji.waris.database.InheritanceDatabase as Database

class AddEditViewModelFactory(private val database: Database) : Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddEditViewModel::class.java))
            AddEditViewModel(database) as T
        else
            throw IllegalArgumentException("Couldn't instantiate add edit view model")
    }
}