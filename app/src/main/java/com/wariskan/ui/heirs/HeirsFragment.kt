package com.wariskan.ui.heirs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders.of
import com.google.android.material.tabs.TabLayoutMediator
import com.kenji.waris.model.Gender.MALE
import com.kenji.waris.model.Position
import com.wariskan.ui.inheritance.InheritanceViewModel
import com.wariskan.R.layout.fragment_heirs
import com.wariskan.R.string.*
import com.wariskan.ui.heirs.HeirsAdapter.Companion.getOrder
import com.wariskan.ui.inheritance.InheritanceActivity
import com.wariskan.util.POSITION
import com.wariskan.databinding.FragmentHeirsBinding as Binding

class HeirsFragment : Fragment() {

    private lateinit var binding: Binding
    private lateinit var inheritanceViewModel: InheritanceViewModel
    var male = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = inflate(inflater, fragment_heirs, container, false)
        setUpViewModel()
        return binding.root
    }

    private fun setUpViewModel() {
        activity?.let {
            inheritanceViewModel = of(it).get(InheritanceViewModel::class.java).apply {
                repository.inheritance.value?.deceased?.gender?.let { gender ->
                    male = (gender == MALE)
                }
            }
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        setUpAdapter()
        setUpTab()
        handleArguments()
    }

    private fun setUpAdapter() {
        val adapter = HeirsAdapter(this)
        binding.pager.adapter = adapter

    }

    private fun setUpTab() {
        activity?.let {
            binding.apply {
                TabLayoutMediator(tabLayout, pager) { tab, order ->
                    tab.text = when (order) {
                        0 -> it.getString(dad)
                        1 -> it.getString(mom)
                        2 -> if (male) {
                            it.getString(wife)
                        } else {
                            it.getString(husband)
                        }
                        3 -> it.getString(child)
                        4 -> it.getString(sibling)
                        5 -> it.getString(grandpa)
                        6 -> it.getString(grandma)
                        7 -> it.getString(grandchild)
                        8 -> it.getString(uncle)
                        9 -> it.getString(male_cousin)
                        else -> it.getString(nephew)
                    }
                }.attach()
            }
        }
    }

    private fun handleArguments() {
        arguments?.getSerializable(POSITION)?.let {
            val position = it as Position
            val order = getOrder(position)
            binding.pager.setCurrentItem(order)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            if (it is InheritanceActivity) {
                it.binding.toolbarTitle.text = it.getString(heirs)
            }
        }
    }
}