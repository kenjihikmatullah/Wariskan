package com.wariskan.ui.legacy

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdSize.LARGE_BANNER
import com.kenji.waris.model.Legacy
import com.wariskan.R
import com.wariskan.R.layout.fragment_legacy
import com.wariskan.R.string.et_blank
import com.wariskan.ui.inheritance.InheritanceActivity
import com.wariskan.ui.inheritance.InheritanceViewModel
import com.wariskan.util.getNumber
import com.wariskan.util.getStringNoComma
import com.wariskan.util.getWatcher
import kotlin.math.ceil
import com.wariskan.databinding.FragmentLegacyBinding as Binding
import com.wariskan.ui.legacy.LegacyViewModel as ViewModel

class LegacyFragment : Fragment() {

    private lateinit var binding: Binding
    private lateinit var inheritanceViewModel: InheritanceViewModel
    private lateinit var viewModel: ViewModel
    private var legacy = Legacy()

    val legacyWatcher: TextWatcher
        get() {
            return object : TextWatcher {
                val et = binding.legacyEt

                var lenBefore = 0
                var lenAfter = 0
                var lenBlocked = 0
                var selectionBefore = 0
                val selectionAfter: Int
                    get() {
                        val diffs = lenAfter - lenBefore
                        return if (diffs >= 0) {
                            selectionBefore + diffs

                        } else {
                            selectionBefore + diffs + lenBlocked
                        }
                    }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    s?.let {
                        lenBefore = it.length
                    }
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    selectionBefore = start
                    lenBlocked = before
                }

                override fun afterTextChanged(s: Editable?) {
                    et.removeTextChangedListener(this)
                    val text = et.text.toString().getStringNoComma()
                    if (!text.isBlank() && text.toDouble() <= Double.MAX_VALUE) {
                        val double = getNumber(resources, text.toDouble())
                        lenAfter = double.length
                        et.setText(double)
                        et.setSelection(selectionAfter)
                    }
                    et.addTextChangedListener(this)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = inflate(inflater, fragment_legacy, container, false)
        refreshAd()
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

    private fun refreshAd() {
        //        binding.adView.adUnitId = "ca-app-pub-3178233257268861/8063147418"
//        binding.adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111")
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
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

                /*
                 * Total of legacy
                 */
                legacyEt.apply {
                    setText(getNumber(resources, it.deceased.legacy.total))
                    addTextChangedListener(legacyWatcher)
                }

                /*
                 * Funeral costs
                 */
                legacyFuneralEt.apply {
                    setText(getNumber(resources, it.deceased.legacy.funeralCosts))
                    addTextChangedListener(getWatcher(this, resources))
                }

                /*
                 * Debt amount
                 */
                legacyDebtEt.apply {
                    setText(getNumber(resources, it.deceased.legacy.debtAmount))
                    addTextChangedListener(getWatcher(this, resources))
                }

                /*
                 * Will amount
                 */
                legacyWillEt.apply {
                    setText(getNumber(resources, it.deceased.legacy.willAmount))
                    addTextChangedListener(getWatcher(this, resources))
                }
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
                            hideKeyboard()
                            binding.apply {
                                repository.inheritance.value?.deceased?.legacy?.let {

                                    /*
                                     * Total of legacy
                                     */
                                    legacyEt.let { et ->
                                        if (et.text.isNullOrBlank())
                                            legacyFuneralEt.error = activity.getString(et_blank)
                                        else
                                            it.total = "${et.text}".getStringNoComma().toDouble()
                                    }

                                    /*
                                     * Funeral costs
                                     */
                                    legacyFuneralEt.let { et ->
                                        if (et.text.isNullOrBlank())
                                            legacyFuneralEt.error = activity.getString(et_blank)
                                        else
                                            it.funeralCosts = "${et.text}".getStringNoComma().toDouble()
                                    }

                                    /*
                                     * Debt amount
                                     */
                                    legacyDebtEt.let { et ->
                                        if (et.text.isNullOrBlank())
                                            et.error = activity.getString(et_blank)
                                        else
                                            it.debtAmount = "${et.text}".getStringNoComma().toDouble()
                                    }

                                    /*
                                     * Will amount
                                     */
                                    legacyWillEt.let { et ->
                                        if (et.text.isNullOrBlank())
                                            et.error = activity.getString(et_blank)
                                        else
                                            it.willAmount = "${et.text}".getStringNoComma().toDouble()
                                    }

                                    /*
                                     * Shareable
                                     */
                                    legacyTv.text = getNumber(resources, it.primaryShareable)
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

    private fun hideKeyboard() {
        activity?.let {
            val manager = it.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
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