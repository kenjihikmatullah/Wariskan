package com.wariskan.ui.heir

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders.of
import com.kenji.waris.model.Position.*
import com.wariskan.AddEditActivity
import com.wariskan.R.drawable.ic_add
import com.wariskan.R.drawable.baseline_edit_white_48dp
import com.wariskan.ui.inheritance.InheritanceViewModel
import com.wariskan.R.layout.fragment_heir
import com.wariskan.databinding.FragmentHeirBinding
import com.wariskan.util.ID
import com.wariskan.util.ORDER
import com.wariskan.util.POSITION
import com.wariskan.ui.heir.HeirViewModel as ViewModel

class HeirFragment() : Fragment() {

    private lateinit var binding: FragmentHeirBinding
    private lateinit var inheritanceViewModel: InheritanceViewModel
    private lateinit var viewModel: ViewModel

    private val addListener: OnClickListener
        get() {
            return object : OnClickListener {
                override fun onClick(v: View?) {
                    viewModel.add()
                }
            }
        }

    private val editListener: OnClickListener
        get() {
            return object : OnClickListener {
                override fun onClick(v: View?) {
                    viewModel.edit(0)
                }
            }
        }

    /**
     * Limit of list size
     * for each position
     */
    private val limit: Int
        get() {
            return when (viewModel.position) {
                DAD -> 1
                MOM -> 1
                HUSBAND -> 1
                WIFE -> 4
                GRANDPA -> 2
                GRANDMA -> 2
                else -> Int.MAX_VALUE
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = inflate(inflater, fragment_heir, container, false)

        setUpViewModel()
        handleArguments()
        setUpAdapter()
        setOnAdd()
        setOnEdit()
        setOnDelete()

        return binding.root
    }

    private fun setUpViewModel() {
        activity?.let {
            inheritanceViewModel = of(it).get(InheritanceViewModel::class.java)

            viewModel = of(this).get(ViewModel::class.java)
            binding.lifecycleOwner = this
            binding.viewModel = viewModel
        }
    }

    private fun handleArguments() {
        arguments?.let { viewModel.handleArguments(it) }
    }

    private fun setUpAdapter() {
        viewModel.let { vm ->
            binding.rv.adapter = vm.adapter
            inheritanceViewModel.repository.inheritance.observe(viewLifecycleOwner, Observer {
                val list = it.getHeirList(vm.position)
                vm.adapter.submitList(list)
                updateFab(list.size)
            })
        }
    }

    private fun updateFab(size: Int) {

        viewModel.apply {
            binding.apply {

                fab.visibility = VISIBLE

                if (size < limit) {
                    fab.setImageResource(ic_add)
                    fab.setOnClickListener(addListener)

                } else if (position in listOf(DAD, MOM, HUSBAND)) {
                    fab.setImageResource(baseline_edit_white_48dp)
                    fab.setOnClickListener(editListener)

                } else {
                    fab.visibility = GONE
                }
            }
        }
    }

    private fun setOnAdd() {
        viewModel.onAdd.observe(viewLifecycleOwner, Observer { onAdd ->
            if (!onAdd) return@Observer

            viewModel.let { vm ->
                val intent = Intent(activity, AddEditActivity::class.java)
                intent.putExtra(ID, inheritanceViewModel.id)
                intent.putExtra(POSITION, vm.position)
                intent.putExtra(ORDER, -1)
                startActivity(intent)
                vm.added()
            }
        })
    }

    private fun setOnEdit() {
        viewModel.onEdit.observe(viewLifecycleOwner, Observer { onEdit ->
            if (!onEdit) return@Observer

            viewModel.let {
                val intent = Intent(activity, AddEditActivity::class.java)
                intent.putExtra(ID, inheritanceViewModel.id)
                intent.putExtra(POSITION, it.position)
                intent.putExtra(ORDER, it.order)
                startActivity(intent)
                it.edited()
                it.adapter.notifyDataSetChanged()
            }
        })
    }

    private fun setOnDelete() {
        viewModel.apply {
            onDelete.observe(viewLifecycleOwner, Observer {
                if (!it) return@Observer

                inheritanceViewModel.apply {
                    repository.inheritance.value?.deleteHeir(position, order)
                    update()
                    adapter.notifyDataSetChanged()
                    inheritanceViewModel.repository.inheritance.value?.getHeirList(position)?.let { list ->
                        updateFab(list.size)
                    }
                }
                deleted()

            })
        }
    }

    override fun onStart() {
        super.onStart()
        inheritanceViewModel.get()
    }
}
