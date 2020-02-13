package com.kenji.waris.model

import androidx.room.Embedded
import com.kenji.waris.model.Position.DAD

/**
 * Person who could get the legacy.
 */
open class Heir : Person() {

    @Embedded(prefix = "in_")
    var `in` = LegacyIn()
    var id = currentId
    var position = DAD
    var killedDeceased = false

    /*
     * Eligibility
     */
    val eligibleOne: Boolean
        get() = alive && muslim && !killedDeceased
    var eligibleTwo = true
    val eligible: Boolean
        get() = eligibleOne && eligibleTwo

    init {
        currentId++
    }


    fun resetIn() {

        /*
         * Amount
         */
        `in`.primary = 0.0
//        `in`.specialAmount = 0.0
        `in`.secondary = 0.0

        /*
         * Details
         */
        `in`.primaryDetail = ""
        `in`.secondaryDetail = ""
//        `in`.special = ""
        `in`.disentitler = ""

        /*
         * Disentitlement
         */
        eligibleTwo = true
    }

    companion object {
        var currentId = 1
    }
}