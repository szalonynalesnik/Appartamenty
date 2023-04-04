package com.example.appartamenty.data

data class UtilityPrice (

    var name: String = "",
    var lastReading: Double = 0.0,
    var previousReading: Double = 0.0,
    var pricePerUnit: Double = 0.0,
    var total: Double = 0.0,
    var propertyId: String = ""
)