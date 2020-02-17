package com.wariskan.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kenji.waris.database.Inheritance
import com.kenji.waris.database.InheritanceDatabase
import com.wariskan.network.Api
import com.wariskan.network.InheritanceNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class InheritanceRepository(private val database: InheritanceDatabase) {

    /*
     * Inheritance
     */
    val inheritance = MutableLiveData<Inheritance>()

    /*
     * Insert
     */
    suspend fun insert() {
        withContext(IO) {
            inheritance.value?.let { database.dao.insert(it) }
        }
    }

    /*
     * Update
     */
    suspend fun update() {
        withContext(IO) {
            inheritance.value?.let { database.dao.update(it) }
        }
    }

    /*
     * Get
     */
    suspend fun get(id: Int) : Inheritance? {
        return withContext(IO) {
            database.dao.get(id)
        }
    }

    suspend fun getAllFromNetwork() {
//        withContext(IO) {
//
//            val inheritances = Api.apiServices.getAll().inheritances
//            if (inheritances.isNotEmpty())
//                Log.i("HEHEHE", "${inheritances[0].id_room}")
//        }
    }

    /*
     * Get latest
     */
    suspend fun getLatest() : Inheritance? {
        return withContext(IO) {
            database.dao.getLatest()
        }
    }

}