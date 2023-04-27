package com.example.appartamenty.data

import java.io.Serializable

data class Utility(
    var constant: Boolean = false,
    var name: String? = null,
    var price: Double = 0.0,
    var propertyId: String? = null,
    var utilityId: String? = null,
): Serializable

