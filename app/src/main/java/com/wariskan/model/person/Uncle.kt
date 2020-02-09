package com.wariskan.model.person

import com.kenji.waris.model.Heir
import com.wariskan.model.property.Type.FULL
import com.wariskan.model.property.Type.PATERNAL

class Uncle : Heir() {

    var dadX = "Mr DadX"

    /**
     * Am I
     * brother of Mr DadX?
     */
    var boolOne = false

    /**
     * Do I
     * have the same dad with Mr DadX?
     */
    var boolTwo = false

    /**
     * Do I
     * have the same mom with Mr DadX?
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