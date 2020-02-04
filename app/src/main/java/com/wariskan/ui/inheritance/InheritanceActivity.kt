package com.wariskan.ui.inheritance

import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProviders.of
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.MobileAds
import com.kenji.waris.database.InheritanceDatabase.Companion.getInstance
import com.kenji.waris.model.Position
import com.wariskan.R.id.nav_host_fragment
import com.wariskan.R.id.navigation_heirs
import com.wariskan.R.layout.activity_inheritance
import com.wariskan.util.ID
import com.wariskan.util.POSITION
import androidx.appcompat.app.AppCompatActivity as Activity
import com.wariskan.databinding.ActivityInheritanceBinding as Binding
import com.wariskan.ui.inheritance.InheritanceViewModel as ViewModel
import com.wariskan.ui.inheritance.InheritanceViewModelFactory as Factory
class InheritanceActivity : Activity() {

    lateinit var binding: Binding
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, activity_inheritance)

        setUpViewModel()
        handleExtras()
        setUpNavigation()
    }

    override fun onResume() {
        super.onResume()
        viewModel.apply {
            if (id != -1) get()
        }
        setUpAds()
    }

    private fun setUpViewModel() {
        val database = getInstance(this)
        val factory = Factory(database)
        viewModel = of(this, factory).get(ViewModel::class.java)
    }

    private fun handleExtras() {

        fun toHeir(position: Position) {
            val bundle = Bundle().apply {
                putSerializable(POSITION, position)
            }
            val controller = findNavController(nav_host_fragment)
            controller.navigate(navigation_heirs, bundle)
        }
        intent?.getSerializableExtra(POSITION)?.let {
            toHeir(it as Position)
        }

        intent?.extras?.apply {
            viewModel.handleExtras(
                getInt(ID, -1)
            )
        }
    }

    private fun setUpNavigation() {
        binding.apply {
            val controller = findNavController(nav_host_fragment)

            /*
             * Bottom navigation
             */
            bottomNavigation.setupWithNavController(controller)
        }
    }

    private fun setUpAds() {
        MobileAds.initialize(this){}
    }

}
