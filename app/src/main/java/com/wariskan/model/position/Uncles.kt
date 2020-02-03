package com.wariskan.model.position

import com.wariskan.model.person.Uncle
import com.wariskan.model.property.Type.FULL
import com.wariskan.model.property.Type.PATERNAL

class Uncles {

    var uncles = mutableListOf<Uncle>()

    val fullBrothersOfDad: List<Uncle>
        get() {
            return uncles.filter {
                it.boolOne && it.type == FULL
            }
        }

    val paternalBrothersOfDad: List<Uncle>
        get() {
            return uncles.filter {
                it.boolOne && it.type == PATERNAL
            }
        }
}