package com.wariskan.model.position

import com.kenji.waris.model.Gender.FEMALE
import com.kenji.waris.model.Gender.MALE
import com.wariskan.model.person.Grandchild

class Grandchildren {

    var grandchildren = mutableListOf<Grandchild>()

    val childrenOfSons: List<Grandchild>
        get() {
            return grandchildren.filter {
                it.boolOne
            }
        }

    val sonsOfSons: List<Grandchild>
        get() {
            return grandchildren.filter {
                it.gender == MALE && it.boolOne
            }
        }

    val daughtersOfSons: List<Grandchild>
        get() {
            return grandchildren.filter {
                it.gender == FEMALE && it.boolOne
            }
        }
}