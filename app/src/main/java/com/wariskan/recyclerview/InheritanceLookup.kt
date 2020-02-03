package com.wariskan.recyclerview

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.wariskan.recyclerview.InheritanceAdapter.Holder

class InheritanceLookup(private val rv: RecyclerView)
    : ItemDetailsLookup<String>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        val view = rv.findChildViewUnder(e.getX(), e.getY())
        if (view != null) {
            val viewHolder = rv.getChildViewHolder(view)
            if (viewHolder is Holder) {
                return viewHolder.itemDetails
            }
        }
        return null
    }
}