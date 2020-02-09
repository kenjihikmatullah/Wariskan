package com.wariskan.model.person

import com.kenji.waris.model.Heir

class Grandpa : Heir() {

    /**
     * Am I
     * parent of the deceased's dad?
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