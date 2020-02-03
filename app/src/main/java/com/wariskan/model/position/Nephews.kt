package com.wariskan.model.position

import com.wariskan.model.person.Nephew
import com.wariskan.model.property.Type.FULL
import com.wariskan.model.property.Type.PATERNAL

class Nephews {

    var nephews = mutableListOf<Nephew>()

    val sonsOfFullBrothers: List<Nephew>
        get() {
            return nephews.filter {
                it.boolOne && it.type == FULL
            }
        }

    val sonsOfPaternalBrothers: List<Nephew>
        get() {
            return nephews.filter {
                it.boolOne && it.type == PATERNAL
            }
        }
}