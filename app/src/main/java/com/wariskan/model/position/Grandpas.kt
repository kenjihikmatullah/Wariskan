package com.wariskan.model.position

import com.wariskan.model.person.Grandpa

class Grandpas {

    var grandpas = mutableListOf<Grandpa>()

    val dadOfDad: List<Grandpa>
        get() {
            return grandpas.filter { it.boolOne }
        }
}