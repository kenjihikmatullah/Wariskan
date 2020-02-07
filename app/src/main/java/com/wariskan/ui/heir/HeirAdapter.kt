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
import com.kenji.waris.model.Heir
import com.wariskan.R.drawable.baseline_expand_less_white_24
import com.wariskan.R.drawable.baseline_expand_more_white_24
import com.wariskan.R.id.delete
import com.wariskan.R.id.edit
import com.wariskan.R.menu.heir_popup_menu
import com.wariskan.databinding.HeirItemBinding
import com.wariskan.databinding.HeirItemBinding.inflate
import com.wariskan.ui.heir.HeirAdapter.ViewHolder

/**
 * Adapter
 * for heir item in recycler view
 */
class HeirAdapter(private val listener: Listener)
    : ListAdapter<Heir, ViewHolder>(DiffCallback()) {

    class Listener(
        val editUnit: (order: Int) -> Unit,
        val deleteUnit: (order: Int) -> Unit
    ) {
        val isExpanded = MutableLiveData<Boolean>(false)
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
        return ViewHolder(binding)
    }

    /**
     * Bind view holder
     */
    override fun onBindViewHolder(holder: ViewHolder, order: Int) {
        holder.binding.let {
            val item = getItem(order)
            it.heir = item
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
             * Expand button
             */
            it.expandButton.context?.let { context ->
                if (context is AppCompatActivity) {
                    listener.isExpanded.observe(context, Observer { isExpanded ->

                        /*
                         * Expanded
                         */
                        if (isExpanded && item.name.isNotBlank()) {
                            it.expandButton.setImageResource(baseline_expand_less_white_24)
                            it.detailsLayout.visibility = VISIBLE

                            /*
                             * Ineligible
                             */
                            if (!item.eligibleOne) {
                                it.ineligibleDetails.text = item.`in`
                                    .getIneligible(item, it.ineligibleDetails.context)
                                it.ineligibleLayout.visibility = VISIBLE
                            } else {
                                it.ineligibleLayout.visibility = GONE
                            }

                            /*
                             * Disentitled
                             */
                            if (!item.eligibleTwo) {
                                it.disentitledDetails.text = item.`in`.disentitler
                                it.disentitledLayout.visibility = VISIBLE
                            } else {
                                it.disentitledLayout.visibility = GONE
                            }

                            /*
                             * Primary
                             */
                            if (item.`in`.primary > 0) {
                                it.primaryDetails.text = item.`in`.one
                                it.primaryLayout.visibility = VISIBLE
                            } else {
                                it.primaryLayout.visibility = GONE
                            }

                            /*
                             * Special
                             */
                            if (item.`in`.specialAmount > 0) {
                                it.specialDetails.text = item.`in`.special
                                it.specialLayout.visibility = VISIBLE
                            } else {
                                it.specialLayout.visibility = GONE
                            }

                            /*
                             * Secondary
                             */
                            if (item.`in`.secondary > 0) {
                                it.secondarDetails.text = item.`in`.two
                                it.secondaryLayout.visibility = VISIBLE
                            } else {
                                it.secondaryLayout.visibility = GONE
                            }
                        }

                        /*
                         * Not expanded
                         */
                        else {
                            it.expandButton.setImageResource(baseline_expand_more_white_24)
                            it.detailsLayout.visibility = GONE
                        }
                    })
                }
            }

            /*
             * Popup menu
             */
            it.menu.setOnClickListener { v ->
                val popup = PopupMenu(v.context, v)
                popup.menuInflater
                    .inflate(heir_popup_menu, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    if (menuItem.itemId == edit) {
                        listener.edit(order)

                    } else if (menuItem.itemId == delete) {
                        listener.delete(order)
                    }

                    return@setOnMenuItemClickListener true
                }
                popup.show()
            }
        }
    }
}