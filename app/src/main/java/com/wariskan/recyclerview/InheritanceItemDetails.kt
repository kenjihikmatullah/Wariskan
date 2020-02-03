package com.wariskan.recyclerview

import androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails

class InheritanceItemDetails(
    private val position: Int,
    private val key: String

) : ItemDetails<String>() {

    override fun getSelectionKey(): String? = key
    override fun getPosition() = position
}