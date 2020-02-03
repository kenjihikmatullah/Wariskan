package com.wariskan.model.person

import com.kenji.waris.model.Heir
import com.wariskan.model.property.Type.FULL

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
}