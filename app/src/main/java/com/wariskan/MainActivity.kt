package com.wariskan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.wariskan.R.layout.activity_main
import com.wariskan.R.style.App
import com.wariskan.databinding.ActivityMainBinding as Binding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(App)
        super.onCreate(savedInstanceState)
        binding = setContentView(this, activity_main)
    }
}
