package com.kenji.waris.model

import com.kenji.waris.model.Gender.*

/**
 * Interface for both the deceased and heir.
 */
open class Person {
    var name = ""
    var alive = false
    var muslim = true
    var gender = MALE
}

