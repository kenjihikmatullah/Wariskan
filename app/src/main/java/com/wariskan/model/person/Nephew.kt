package com.wariskan.model.person

import com.kenji.waris.model.Heir
import com.wariskan.model.property.Type.FULL
import com.wariskan.model.property.Type.PATERNAL

class Nephew : Heir() {

    var dad = "Mr Dad"

    /**
     * Is Mr Dad
     * the brother of the deceased?
     */
    var boolOne = false

    /**
     * Does Mr Dad
     * have the same dad with the deceased?
     */
    var boolTwo = false

    /**
     * Does Mr Dad
     * have the same mom with the deceased?
     */
    var boolThree = false

    var type = FULL

    val spinnerOne: Int
        get() {
            return when (boolOne) {
                true -> 0
                else -> 1
            }
        }

    val spinnerTwo: Int
        get() {
            return when (type) {
                FULL -> 0
                PATERNAL -> 1
                else -> 2
            }
        }
}