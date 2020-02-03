package com.wariskan.model.person

import com.kenji.waris.model.Gender.FEMALE
import com.kenji.waris.model.Gender.MALE
import com.kenji.waris.model.Heir

class Children {

    var children = mutableListOf<Heir>()

    val sons: List<Heir>
        get() {
            return children.filter {
                it.gender == MALE
            }
        }

    val daughters: List<Heir>
        get() {
            return children.filter {
                it.gender == FEMALE
            }
        }
}