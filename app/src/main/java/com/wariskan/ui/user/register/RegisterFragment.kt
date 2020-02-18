package com.wariskan.ui.user.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.wariskan.R
import com.wariskan.databinding.FragmentRegisterBinding
import com.wariskan.ui.user.register.RegisterViewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        setUpViewModel()
        setOnLogin()
        return binding.root
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private fun setOnLogin() {
        binding.loginBtn.setOnClickListener(object : View.OnClickListener {
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
