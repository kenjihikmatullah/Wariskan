package com.kenji.waris.model

import android.content.Context
import com.wariskan.R.string.*

/**
 * Legacy
 * obtained by a heir from the deceased.
 */
class LegacyIn {

    /*
     * Amount
     */
    var primary = 0.0
//    var specialAmount = 0.0
    var secondary = 0.0
    val total: Double
        get() = primary + secondary

    /*
    * Details
    */
    var disentitler = ""
    var primaryDetail = ""
    var secondaryDetail = ""

    /**
     * Get
     * ineligibility details of a heir.
     */
    fun getIneligible(heir: Heir, context: Context) : String {
        return if (!heir.alive) context.getString(ineligible_status)
        else if (!heir.muslim) context.getString(ineligible_faith)
        else context.getString(ineligible_killing)
    }

//    var special = ""
}