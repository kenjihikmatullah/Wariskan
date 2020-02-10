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
import com.kenji.waris.model.Legacy
import com.wariskan.R
import com.wariskan.R.layout.fragment_legacy
import com.wariskan.R.string.et_blank
import com.wariskan.R.string.legacy
import com.wariskan.ui.inheritance.InheritanceActivity
import com.wariskan.ui.inheritance.InheritanceViewModel
import com.wariskan.util.getNumber
import com.wariskan.databinding.FragmentLegacyBinding as Binding
import com.wariskan.ui.legacy.LegacyViewModel as ViewModel

class LegacyFragment : Fragment() {

    private lateinit var binding: Binding
    private lateinit var inheritanceViewModel: InheritanceViewModel
    private lateinit var viewModel: ViewModel
    private var legacy = Legacy()

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
            inheritanceViewModel.repository.inheritance.value?.deceased?.let { deceased ->
                legacy = deceased.legacy
            }
//            binding.legacy = inheritanceViewModel.repository.inheritance.value?.deceased?.legacy
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
    }

    private fun adjustStatsLayout() {
        binding.apply {
            val res = resources

            statsSharedAmount.text = getNumber(res, legacy.shared)
            statsRestAmount.text = getNumber(res, legacy.rest)

            statsPrimaryAmount.text = getNumber(res, legacy.primaryShared)
            statsSecondaryAmount.text = getNumber(res, legacy.secondaryShared)
        }
    }

    private fun setOnCalculate() {
        viewModel.onCalculate.observe(viewLifecycleOwner, Observer { onCalculate ->
            binding.apply {
                if (onCalculate) {
                    activity?.let { activity ->
                        inheritanceViewModel.apply {
                            binding.apply {
                                repository.inheritance.value?.deceased?.legacy?.let {
                                    if (legacyEt.text.isNullOrBlank())
                                        legacyEt.error = activity.getString(et_blank)
                                    else
                                        it.total = "${legacyEt.text}".toDouble()
                                        legacyTv.text = getNumber(resources, it.total)
                                }
                            }
                            repository.inheritance.value?.calculate(activity)
                            adjustStatsLayout()
                            update()
                            inputLayout.visibility = GONE
                            outputLayout.visibility = VISIBLE
                            calculatedLayout.visibility = VISIBLE
                        }
                    }

                } else {
                    inputLayout.visibility = VISIBLE
                    outputLayout.visibility = GONE
                    calculatedLayout.visibility = GONE
                    statsLayout.visibility = GONE
                }
            }
        })
    }

    private fun setOnShowStats() {
        viewModel.onShowStats.observe(viewLifecycleOwner, Observer { onShowStats ->
            binding.apply {
                if (onShowStats) {
//                    calculatedLayout.visibility = GONE
                    statsLayout.visibility = VISIBLE
                    showStatsButton.text = "Hide Stats"

                } else {
//                    calculatedLayout.visibility = VISIBLE
                    statsLayout.visibility = GONE
                    showStatsButton.text = "Show Stats"
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            if (it is InheritanceActivity) {
                it.binding.toolbarTitle.text = it.getString(R.string.legacy)
            }
        }
    }
}