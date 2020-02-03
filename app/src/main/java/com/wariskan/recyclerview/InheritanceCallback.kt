package com.wariskan.recyclerview

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import com.wariskan.R
import androidx.recyclerview.selection.SelectionTracker as Tracker

class InheritanceCallback(
    private val context: Context,
    private val tracker: Tracker<String>
)
    : ActionMode.Callback {

    override fun onCreateActionMode(
        mode: ActionMode?,
        menu: Menu?
    ): Boolean {

        val inflater = mode?.menuInflater
        inflater?.inflate(R.menu.inheritance_context_menu, menu)
        return true
    }

    override fun onPrepareActionMode(
        mode: ActionMode?,
        menu: Menu?
    ) = false

    override fun onActionItemClicked(
        mode: ActionMode?,
        item: MenuItem?
    ) = false

    override fun onDestroyActionMode(mode: ActionMode?) {
        tracker.clearSelection()
    }
}