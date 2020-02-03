package com.wariskan.ui.heirs

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.kenji.waris.model.Position
import com.kenji.waris.model.Position.*
import com.wariskan.ui.heir.HeirFragment
import com.wariskan.util.POSITION
import androidx.viewpager2.adapter.FragmentStateAdapter as Adapter

/**
 * Pager adapter
 * for each heir fragment
 * in heirs fragment
 */
class HeirsAdapter(private val fragment: Fragment) : Adapter(fragment) {

    class Listener(val editUnit: (position: Position, order: Int) -> Unit) {
        fun edit(position: Position, order: Int) = editUnit(position, order)
    }

    private var male = true

    override fun getItemCount() = 11

    override fun createFragment(order: Int): Fragment {
        if (fragment is HeirsFragment) male = fragment.male

        val position = getPosition(order)
        val fragment = HeirFragment()
        val bundle = Bundle().apply {
            putSerializable(POSITION, position)
        }
        fragment.arguments = bundle
        return fragment
    }

    private fun getPosition(order: Int): Position {
        return when (order) {
            0 -> DAD
            1 -> MOM
            2 -> if (male) {
                WIFE
            } else {
                HUSBAND
            }
            3 -> CHILD
            4 -> SIBLING
            5 -> GRANDPA
            6 -> GRANDMA
            7 -> GRANDCHILD
            8 -> UNCLE
            9 -> MALE_COUSIN
            else -> NEPHEW
        }
    }

    companion object {
        fun getOrder(position: Position): Int {
            return when (position) {
                DAD -> 0
                MOM -> 1
                HUSBAND -> 2
                WIFE -> 2
                CHILD -> 3
                SIBLING -> 4
                GRANDPA -> 5
                GRANDMA -> 6
                GRANDCHILD -> 7
                UNCLE -> 8
                MALE_COUSIN -> 9
                else -> 10
            }
        }
    }
}