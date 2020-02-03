package com.wariskan.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.kenji.waris.database.InheritanceDatabase as Database

class MainViewModelFactory(private val database: Database) : Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java))
            MainViewModel(database) as T
        else
            throw IllegalArgumentException("Couldn't instantitate main view model")
    }
}