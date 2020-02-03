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
    var killedInheritee = false

    /**
     * EligibleOne
     * returns whether this heir is alive, muslim,
     * and didn't participate in the killing of inheritee.
     */
    val eligibleOne: Boolean
        get() = alive && muslim && !killedInheritee

    /**
     * EligibleTwo
     * returns whether this heir is not disentitled by others.
     */
    var eligibleTwo = true

    /**
     * Eligible
     * returns wheter this heir is eligible to get the inheritance.
     */
    val eligible: Boolean
        get() = eligibleOne && eligibleTwo


    init {
        currentId++
    }

    fun resetIn() {
        `in`.primary = 0.0
        `in`.secondary = 0.0
        `in`.one = ""
        `in`.two = ""
        `in`.special = ""
        `in`.disentitler = ""
        eligibleTwo = true
    }

    companion object {
        var currentId = 1
    }
}