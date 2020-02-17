package com.wariskan.ui.heir

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdSize.BANNER
import com.kenji.waris.model.Heir
import com.wariskan.R
import com.wariskan.R.drawable.baseline_expand_less_white_24
import com.wariskan.R.drawable.baseline_expand_more_white_24
import com.wariskan.R.id.*
import com.wariskan.R.menu.heir_popup_menu
import com.wariskan.R.string.*
import com.wariskan.databinding.HeirItemBinding
import com.wariskan.databinding.HeirItemBinding.inflate
import com.wariskan.ui.heir.HeirAdapter.ViewHolder
import com.wariskan.util.getNumber

/**
 * Adapter
 * for heir item in recycler view
 */
class HeirAdapter(private val listener: Listener) : ListAdapter<Heir, ViewHolder>(DiffCallback()) {

    class Listener(
        val editUnit: (order: Int) -> Unit,
        val deleteUnit: (order: Int) -> Unit
    ) {
        val isExpanded = MutableLiveData<Boolean>(false)
        val isDetailed = MutableLiveData<Boolean>(false)
        fun edit(order: Int) = editUnit(order)
        fun delete(order: Int) = deleteUnit(order)
        fun inverse() {
            isExpanded.value = isExpanded.value?.not()
        }
    }

    /**
     * View holder class
     */
    class ViewHolder(val binding: HeirItemBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Difference callback class
     */
    class DiffCallback : DiffUtil.ItemCallback<Heir>() {
        override fun areItemsTheSame(oldItem: Heir, newItem: Heir): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Heir, newItem: Heir): Boolean {
            return oldItem.equals(newItem)
        }
    }

    /**
     * Create view holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = inflate(inflater, parent, false)
        parent.context.let {
            if (it is AppCompatActivity) {
                binding.lifecycleOwner = it
            }
        }
        return ViewHolder(binding)
    }

    /**
     * Bind view holder
     */
    override fun onBindViewHolder(holder: ViewHolder, order: Int) {
        holder.binding.let {

            fun refreshAd() {
                //        binding.adView.adUnitId = "ca-app-pub-3178233257268861/3852439663"
//                it.adView.adUnitId = "ca-app-pub-3940256099942544/6300978111" // testing
                val adRequest = AdRequest.Builder().build()
                it.adView.loadAd(adRequest)
                it.adView.adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        it.adPlaceholder.visibility = GONE
                        it.adView.visibility = VISIBLE
                    }
                    override fun onAdFailedToLoad(p0: Int) {
                        super.onAdFailedToLoad(p0)
                        it.adPlaceholder.visibility = VISIBLE
                        it.adView.visibility = GONE
                    }
                }
            }

            refreshAd()

            /*
             * Layout variables
             */
            val heir = getItem(order)
            it.heir = heir
            it.order = order
            it.listener = listener

            /*
             * Initial of dad and mom
             */
            /**
             * if (item.name.isBlank()) {
            it.statusBody.visibility = GONE
            it.faithBody.visibility = GONE
            it.inBody.visibility = GONE
            it.detailsLayout.visibility = GONE
            } else {
            it.statusBody.visibility = VISIBLE
            it.faithBody.visibility = VISIBLE
            it.inBody.visibility = VISIBLE
            it.detailsLayout.visibility = VISIBLE
            }
             */

            /*
             * Calculation
             */
            it.root.context.resources.let { res ->
                it.calcPrimaryAmount.text = getNumber(res, heir.`in`.primary)
//                it.calcSpecialAmount.text = getNumber(res, heir.`in`.specialAmount)
                it.calcSecondaryAmount.text = getNumber(res, heir.`in`.secondary)
                it.calcTotalAmount.text = getNumber(res, heir.`in`.total)
            }

            /*
             * Details
             */
            it.root.context.let { context ->

                /*
                             * Ineligible
                             */
                if (!heir.eligibleOne) {
                    it.ineligibleLayout.visibility = VISIBLE
                    it.ineligibleDetails.text = heir.`in`
                        .getIneligible(heir, it.ineligibleDetails.context)
                } else {
                    it.ineligibleLayout.visibility = GONE
                }

                /*
                 * Disentitled
                 */
                if (!heir.eligibleTwo) {
                    it.disentitledDetails.text = heir.`in`.disentitler
                    it.disentitledLayout.visibility = VISIBLE
                } else {
                    it.disentitledLayout.visibility = GONE
                }

                /*
                 * Primary
                 */
                if (heir.`in`.primary > 0) {
                    it.primaryLayout.visibility = VISIBLE
                    it.primaryHeader.apply {
                        val one = context.getString(ashabul_furudh)
//                        val two = getNumber(resources, heir.`in`.primary)
//                        text = context.getString(legacy_details_header, one, two)
                        text = one
                    }

                } else {
                    it.primaryLayout.visibility = GONE
                }

                /*
                 * Special
                 */
//                if (heir.`in`.specialAmount > 0) {
//                    it.specialLayout.visibility = VISIBLE
////                                it.specialDetails.text = item.`in`.special
//                    it.specialHeader.apply {
//                        val one = context.getString(special)
////                        val two = getNumber(resources, heir.`in`.specialAmount)
////                        text = context.getString(legacy_details_header, one, two)
//                        text = one
//                    }
//                } else {
//                    it.specialLayout.visibility = GONE
//                }

                /*
                 * Secondary
                 */
                if (heir.`in`.secondary > 0) {
                    it.secondaryLayout.visibility = VISIBLE
//                                it.secondarDetails.text = item.`in`.two
                    it.secondaryHeader.apply {
                        val one = context.getString(ashabah)
//                        val two = getNumber(resources, heir.`in`.secondary)
//                        text = context.getString(legacy_details_header, one, two)
                        text = one
                    }
                } else {
                    it.secondaryLayout.visibility = GONE
                }
            }

            /*
             * Expand button
             */
            it.expandButton.context?.let { context ->
                if (context is AppCompatActivity) {
                    listener.isExpanded.observe(context, Observer { isExpanded ->

                        /*
                         * Expanded
                         */
                        if (isExpanded) {
                            it.expandable.visibility = VISIBLE
                            it.expandButton.setImageResource(baseline_expand_less_white_24)
                        }

                        /*
                         * Not expanded
                         */
                        else {
                            it.expandable.visibility = GONE
                            it.expandButton.setImageResource(baseline_expand_more_white_24)
                        }
                    })
                }
            }

            /*
             * Switch button
             */
            it.switchButton.setOnCheckedChangeListener { _, isChecked ->
                listener.isDetailed.value = isChecked
            }

            /*
             * Popup menu
             */
            it.menu.setOnClickListener { v ->
                val popup = PopupMenu(v.context, v)
                popup.menuInflater
                    .inflate(heir_popup_menu, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    if (menuItem.itemId == R.id.edit) {
                        listener.edit(order)

                    } else if (menuItem.itemId == R.id.delete) {
                        listener.delete(order)
                    }

                    return@setOnMenuItemClickListener true
                }
                popup.show()
            }
        }
    }
}