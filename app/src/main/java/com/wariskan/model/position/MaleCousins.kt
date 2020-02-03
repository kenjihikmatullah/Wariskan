package com.wariskan.model.position

import com.wariskan.model.person.MaleCousin
import com.wariskan.model.property.Type.FULL
import com.wariskan.model.property.Type.PATERNAL

class MaleCousins {

    var maleCousins = mutableListOf<MaleCousin>()

    val sonsOfFullBrothersOfDad: List<MaleCousin>
        get() {
            return maleCousins.filter {
                it.boolOne && it.type == FULL
            }
        }

    val sonsOfPaternalBrothersOfDad: List<MaleCousin>
        get() {
            return maleCousins.filter {
                it.boolOne  && it.type == PATERNAL
            }
        }


}