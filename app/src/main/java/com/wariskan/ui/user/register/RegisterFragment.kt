package com.wariskan.ui.user.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wariskan.R.layout.fragment_register
import com.wariskan.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = inflate(inflater, fragment_register, container, false)
        setUpViewModel()
        setOnLogin()
        return binding.root
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    private fun setOnLogin() {
        binding.loginBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                viewModel.apply {
                    binding.apply {
                        val name = "${nameEt.text}".trim()
                        val email = "${emailEt.text}".trim()
                        val password = "${passwordEt.text}".trim()
                        val c_password = "${confirmPasswordEt.text}".trim()
                        var ready = true

                        if (name.isEmpty()) {
                            nameEt.error = "Please fill the name"
                            nameEt.requestFocus()
                            ready = false
                        }

                        if (email.isEmpty()) {
                            emailEt.error = "Please fill the email"
                            emailEt.requestFocus()
                            ready = false
                        }

                        if (password.isEmpty()) {
                            passwordEt.error = "Please fill the password"
                            passwordEt.requestFocus()
                            ready = false
                        }

                        if (c_password.isEmpty()) {
                            confirmPasswordEt.error = "Please fill the password confirmation"
                            confirmPasswordEt.requestFocus()
                            ready = false
                        }

                        if (password != c_password) {
                            confirmPasswordEt.error = "The password confirmation doesn't match"
                            passwordEt.requestFocus()
                            confirmPasswordEt.requestFocus()
                            ready = false
                        }

                        if (ready) {
                            registerToNetwork(name, email, password, c_password)
                        }
                    }
                }
            }
        })
    }
}
