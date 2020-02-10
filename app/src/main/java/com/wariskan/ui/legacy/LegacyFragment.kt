package com.wariskan.ui.legacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wariskan.R.layout.fragment_legacy
import com.wariskan.R.string.et_blank
import com.wariskan.R.string.legacy
import com.wariskan.ui.inheritance.InheritanceActivity
import com.wariskan.ui.inheritance.InheritanceViewModel
import com.wariskan.databinding.FragmentLegacyBinding as Binding
import com.wariskan.ui.legacy.LegacyViewModel as ViewModel

class LegacyFragment : Fragment() {

    private lateinit var binding: Binding
    private lateinit var inheritanceViewModel: InheritanceViewModel
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = inflate(inflater, fragment_legacy, container, false)
        setUpViewModel()
        adjustLayout()
        setOnCalculate()
        setOnShowStats()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.calculatedTitle.visibility = GONE
        binding.calculatedInstruction.visibility = GONE
    }

    private fun setUpViewModel() {
        activity?.let {
            inheritanceViewModel = ViewModelProvider(it).get(InheritanceViewModel::class.java)
            viewModel = ViewModelProvider(this).get(ViewModel::class.java)
            binding.lifecycleOwner = this
            binding.legacy = inheritanceViewModel.repository.inheritance.value?.deceased?.legacy
            binding.viewModel = viewModel
        }
    }

    private fun adjustLayout() {
        inheritanceViewModel.repository.inheritance.observe(viewLifecycleOwner, Observer {
            binding.apply {
                legacyEt.setText(
                    String.format("%.0f", it.deceased.legacy.total)
                )
            }
        })
//        TODO('Set text of stats')
    }

    private fun setOnCalculate() {
        viewModel.onCalculate.observe(viewLifecycleOwner, Observer { onCalculate ->
            if (!onCalculate) return@Observer

            activity?.let { activity ->
                inheritanceViewModel.apply {
                    binding.apply {
                        repository.inheritance.value?.deceased?.legacy?.let {
                            if (legacyEt.text.isNullOrBlank())
                                legacyEt.error = activity.getString(et_blank)
                            else
                                it.total = "${legacyEt.text}".toDouble()
                        }
                    }
                    repository.inheritance.value?.calculate(activity)
                    update()
                    binding.calculatedTitle.visibility = VISIBLE
                    binding.calculatedInstruction.visibility = VISIBLE

                }
            }

            viewModel.calculated()
        })
    }

    private fun setOnShowStats() {
        viewModel.onShowStats.observe(viewLifecycleOwner, Observer { onShowStats ->
            if (!onShowStats) return@Observer

            binding.apply {
                calculatedLayout.visibility = GONE
                statsLayout.visibility = VISIBLE
            }
            viewModel.showedStats()
        })
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            if (it is InheritanceActivity) {
                it.binding.toolbarTitle.text = it.getString(legacy)
            }
        }
    }
}