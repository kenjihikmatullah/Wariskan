package com.wariskan.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.appcompat.view.ActionMode
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders.of
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.SelectionTracker.Builder
import androidx.recyclerview.selection.StorageStrategy
import com.kenji.waris.database.InheritanceDatabase.Companion.getInstance
import com.kenji.waris.model.Position.DECEASED
import com.wariskan.AddEditActivity
import com.wariskan.ui.inheritance.InheritanceActivity
import com.wariskan.MainActivity
import com.wariskan.R.layout.fragment_main
import com.wariskan.recyclerview.InheritanceCallback
import com.wariskan.recyclerview.InheritanceItemKeyProvider
import com.wariskan.recyclerview.InheritanceLookup
import com.wariskan.util.ID
import com.wariskan.util.ORDER
import com.wariskan.util.POSITION
import com.wariskan.databinding.FragmentMainBinding as Binding
import com.wariskan.ui.main.MainViewModel as ViewModel
import com.wariskan.ui.main.MainViewModelFactory as Factory

class MainFragment : Fragment() {

    private lateinit var activity: MainActivity
    private lateinit var binding: Binding
    private lateinit var viewModel: ViewModel
    private var actionMode: ActionMode? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity = getActivity() as MainActivity
        binding = inflate(inflater, fragment_main, container, false)
        setUpViewModel()
        adjustLayout()
        setUpAdapter()
        setOnAdd()
        setOnOpen()
        return binding.root
    }

    private fun setUpViewModel() {
        val database = getInstance(activity)
        val factory = Factory(database)
        viewModel = of(this, factory).get(ViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun adjustLayout() {
        viewModel.inheritances.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.startButton.visibility = VISIBLE
                binding.startInstruction.visibility = VISIBLE
                binding.rv.visibility = GONE
                binding.fab.visibility = INVISIBLE

            } else {
                binding.startButton.visibility = GONE
                binding.startInstruction.visibility = GONE
                binding.rv.visibility = VISIBLE
                binding.fab.visibility = VISIBLE
            }
        })
    }

    private fun setUpAdapter() {
        var tracker: SelectionTracker<String>? = null

        viewModel.let { vm ->
            binding.rv.adapter = vm.adapter
            vm.inheritances.observe(viewLifecycleOwner, Observer { list ->
                vm.adapter.submitList(list)

                tracker = Builder<String>(
                    "INHERITANCE_SELECTION",
                    binding.rv,
                    InheritanceItemKeyProvider(
                        1,
                        list
                    ),
                    InheritanceLookup(binding.rv),
                    StorageStrategy.createStringStorage()

                ).build()

                vm.adapter.tracker = tracker
                tracker?.setUp()
            })
        }

    }

    private fun SelectionTracker<String>.setUp() {
        val tracker = this
        val observer = object : SelectionTracker.SelectionObserver<String>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()

                if (hasSelection() && actionMode == null) {
                    val callback =
                        InheritanceCallback(
                            activity,
                            tracker
                        )
                    actionMode = activity.startSupportActionMode(callback)
                } else if (actionMode != null) {
                    actionMode?.finish()
                    actionMode = null
                }
            }
        }
        addObserver(observer)
    }

    private fun setOnAdd() {
        viewModel.onAdd.observe(viewLifecycleOwner, Observer { onAdd ->
            if (!onAdd) return@Observer

            val intent = Intent(activity, AddEditActivity::class.java)
            intent.putExtra(ID, -1)
            intent.putExtra(POSITION, DECEASED)
            intent.putExtra(ORDER, -1)
            startActivity(intent)

            viewModel.added()
        })
    }

    private fun setOnOpen() {
        viewModel.onOpen.observe(viewLifecycleOwner, Observer { onOpen ->
            if (!onOpen) return@Observer

            val intent = Intent(activity, InheritanceActivity::class.java)
            intent.putExtra(ID, viewModel.id)
            startActivity(intent)

            viewModel.opened()
        })
    }
}
