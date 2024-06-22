package com.test.application.core.model

data class Charger(
    val busy: Boolean,
    val name: String,
    val address: String
) {
    val id: String
        get() = "$name-$address"

}
