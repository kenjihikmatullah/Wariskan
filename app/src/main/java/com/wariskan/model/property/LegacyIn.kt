package com.kenji.waris.model

import android.content.Context
import com.wariskan.R.string.*

/**
 * Inheritance obtained by a heir from inheritee.
 */
class LegacyIn {
    var primary = 0.0
    var specialAmount = 0.0
    var secondary = 0.0
    val total: Double
        get() = primary + specialAmount + secondary
//
//    inner class DetailsOne {
//        var multiplier = 1
//        var divider = 1
//        var size = 1
//        var explanation = ""
//    }
//
//    inner class DetailsTwo {
//        var sizeMale = 1
//        var sizeFemale = 1
//        var explanation = ""
//    }

    fun getIneligible(heir: Heir, context: Context) : String {
        return if (!heir.alive) context.getString(ineligible_status)
        else if (!heir.muslim) context.getString(ineligible_faith)
        else context.getString(ineligible_killing)
    }

    var disentitler = ""
    var one = ""
    var special = ""
    var two = ""
}