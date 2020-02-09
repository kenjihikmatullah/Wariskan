package com.wariskan.model.person

import com.kenji.waris.model.Heir
import com.wariskan.model.property.Type
import com.wariskan.model.property.Type.FULL

class MaleCousin : Heir() {

    var dad = "Mr Dad"
    var dadX = "Mr DadX"

    /**
     * Is Mr Dad
     * brother of Mr DadX?
     */
    var boolOne = false

    /**
     * Does Mr Dad
     * have the same dad
     * with Mr DadX?
     */
    val boolTwo = false

    /**
     * Does Mr
     * Dad
     * have the same mom
     * with Mr DadX
     */
    val boolThree = false

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
                Type.PATERNAL -> 1
                else -> 2
            }
        }
}
