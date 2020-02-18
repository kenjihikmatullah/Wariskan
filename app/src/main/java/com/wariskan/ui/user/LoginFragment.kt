package com.wariskan.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wariskan.R.layout.fragment_login
import com.wariskan.databinding.FragmentLoginBinding
import com.wariskan.model.LoginResponse
import com.wariskan.network.Api
import com.wariskan.network.Api.apiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = inflate(inflater, fragment_login, container, false)
        setUpViewModel()
        setOnLogin()
        return binding.root
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private fun setOnLogin() {
        binding.loginBtn.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                viewModel.apply {
                    binding.apply {
                        val email = "${emailEt.text}".trim()
                        val password = "${passwordEt.text}".trim()
                        var filled = true

                        if (email.isEmpty()) {
                            emailEt.error = "Please fill the email"
                            emailEt.requestFocus()
                            filled = false
                        }

                        if (password.isEmpty()) {
                            passwordEt.error = "Please fill the password"
                            passwordEt.requestFocus()
                            filled = false
                        }

                        if (filled)
                            loginToNetwork(email, password)
                    }
                }
            }
        })
    }
}
