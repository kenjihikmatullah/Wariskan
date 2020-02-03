package com.wariskan.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kenji.waris.database.Inheritance
import com.wariskan.R
import com.wariskan.R.menu.inheritance_popup_menu
import com.wariskan.databinding.InheritanceItemBinding.inflate
import com.wariskan.recyclerview.InheritanceAdapter.Holder
import androidx.recyclerview.widget.ListAdapter as Adapter
import com.wariskan.databinding.InheritanceItemBinding as Binding

/**
 * Adapter to display list of inheritance
 */
class InheritanceAdapter(private val listener: Listener) :
    Adapter<Inheritance, Holder>(DiffCallback()) {

    var tracker: SelectionTracker<String>? = null

    class Listener(
        val viewDetail: (id: Int) -> Unit,
        val deleteUnit: (inheritance: Inheritance) -> Unit
    ) {
        fun onClick(id: Int) = viewDetail(id)
        fun delete(inheritance: Inheritance) = deleteUnit(inheritance)
    }

    inner class Holder(val binding: Binding) : ViewHolder(binding.root) {
        val itemDetails: ItemDetails<String>
            get() {
                val position = adapterPosition
                val id = getItem(position).id
                return InheritanceItemDetails(
                    position,
                    "$id"
                )
            }
    }

    class DiffCallback : ItemCallback<Inheritance>() {
        override fun areItemsTheSame(oldItem: Inheritance, newItem: Inheritance): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Inheritance, newItem: Inheritance): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = inflate(inflater, parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        holder.binding.inheritance = item
        holder.binding.root.setOnClickListener {
            tracker?.takeIf { !it.hasSelection() }?.let {
                listener.onClick(item.id)
            }
        }

        /*
         * Style
         */
        tracker?.let {
            holder.binding.root.isActivated = it.isSelected(item.id.toString())
        }

        holder.binding.menu.setOnClickListener { v ->
            val popup = PopupMenu(v.context, v)
            popup.menuInflater
                .inflate(inheritance_popup_menu, popup.menu)
            popup.setOnMenuItemClickListener {
                if (it.itemId == R.id.delete) {
                    listener.delete(item)
                }
                return@setOnMenuItemClickListener true
            }
            popup.show()
        }


    }
}