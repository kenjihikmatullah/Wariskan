package com.wariskan.model.person

import com.kenji.waris.model.Heir
import com.wariskan.model.property.Type.*

class Sibling : Heir() {

    /**
     * Do I
     * have the same dad with the deceased?
     */
    var boolOne = false

    /**
     * Do I
     * have the same mom with the deceased?
     */
    var boolTwo = false

    var type = FULL
}