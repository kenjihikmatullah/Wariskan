package com.wariskan

import android.os.Build
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.wariskan.R.layout.activity_add_edit

class AddEditActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_add_edit)
    }

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
    }
}
