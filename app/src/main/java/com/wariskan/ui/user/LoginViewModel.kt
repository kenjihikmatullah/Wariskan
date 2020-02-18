package com.wariskan.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wariskan.model.LoginResponse
import com.wariskan.network.Api
import com.wariskan.network.Api.apiServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(Main + job)

    fun loginToNetwork(email: String, password: String) {
        fun backup() {
//            scope.launch {
//                apiServices.login(email, password)
//                    .enqueue(object : Callback<LoginResponse> {
//                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                            Log.i("HEHEHE", "Ndak bisa request login: ${t.message}")
//                        }
//                        override fun onResponse(
//                            call: Call<LoginResponse>,
//                            response: Response<LoginResponse>
//                        ) {
//                            response.body()?.let {
//                                Log.i("HEHEHE", "name = ${it.user.name}")
//
//                            }
//
//                            if (response.body() == null)
//                                Log.i("HEHEHE", "respon body kosong")
//                        }
//                    })
//            }
        }

        scope.launch {
            val response = apiServices.login(email, password)
            Log.i("HEHEHE", "nama: ${response.user.name}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
