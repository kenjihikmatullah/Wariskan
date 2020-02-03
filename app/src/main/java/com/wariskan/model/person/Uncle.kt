package com.wariskan.model.person

import com.kenji.waris.model.Heir
import com.wariskan.model.property.Type.FULL

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
}