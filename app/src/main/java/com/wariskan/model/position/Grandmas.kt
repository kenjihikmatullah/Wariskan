package com.wariskan.model.position

import com.wariskan.model.person.Grandma

class Grandmas {

    var grandmas = mutableListOf<Grandma>()

    val momOfDad: List<Grandma>
        get() {
            return grandmas.filter { it.boolOne }
        }

    val momOfMom: List<Grandma>
        get() {
            return grandmas.filter { !it.boolOne }
        }
}