package com.wariskan.model.person

import com.kenji.waris.model.Heir

class Grandchild : Heir() {

    var dad = "Mr Dad"

    /**
     * Is Mr Dad
     * son of the deceased?
     */
    var boolOne = false


    val spinnerOne: Int
        get() {
            return when (boolOne) {
                true -> 0
                else -> 1
            }
        }
}