package com.wariskan.ui.user.register

import android.util.Log
import androidx.lifecycle.ViewModel
import com.wariskan.network.Api
import com.wariskan.network.Api.apiServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    fun registerToNetwork(
        name: String,
        email: String,
        password: String,
        c_password: String
    ) {
        scope.launch {
//            TODO("try and catch block in kotlin codelabs")
            val response = apiServices.register(name, email, password, c_password)
            Log.i("HEHEHE", "dah, coba cek")
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
