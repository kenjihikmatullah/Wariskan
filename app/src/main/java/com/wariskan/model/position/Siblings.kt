package com.wariskan.model.position

import com.kenji.waris.model.Gender.FEMALE
import com.kenji.waris.model.Gender.MALE
import com.wariskan.model.person.Sibling
import com.wariskan.model.property.Type.*

class Siblings {

    var siblings = mutableListOf<Sibling>()

    val fullSiblings: List<Sibling>
        get() {
            return siblings.filter {
                it.type == FULL
            }
        }

    val fullBrothers: List<Sibling>
        get() {
            return siblings.filter {
                it.gender == MALE && it.type == FULL
            }
        }

    val fullSisters: List<Sibling>
        get() {
            return siblings.filter {
                it.gender == FEMALE && it.type == FULL
            }
        }

    val paternalBrothers: List<Sibling>
        get() {
            return siblings.filter {
                it.gender == MALE && it.type == PATERNAL
            }
        }

    val maternalSiblings: List<Sibling>
        get() {
            return siblings.filter {
                it.type == MATERNAL
            }
        }

    val paternalSisters: List<Sibling>
        get() {
            return siblings.filter {
                it.gender == FEMALE && it.type == PATERNAL
            }
        }

    val maternalBrothers: List<Sibling>
        get() {
            return siblings.filter {
                it.gender == MALE && it.type == MATERNAL
            }
        }
}
