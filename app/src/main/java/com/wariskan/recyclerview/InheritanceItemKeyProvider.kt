package com.wariskan.recyclerview

import androidx.recyclerview.selection.ItemKeyProvider
import com.kenji.waris.database.Inheritance

class InheritanceItemKeyProvider(
    scope: Int,
    private val list: List<Inheritance>

) : ItemKeyProvider<String>(scope) {

    override fun getKey(position: Int): String? {
        return list.get(position).id.toString()
    }

    override fun getPosition(key: String): Int {
        return list.indexOfFirst { it.id == key.toInt() }
    }
}