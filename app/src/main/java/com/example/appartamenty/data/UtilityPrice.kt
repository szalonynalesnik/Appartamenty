package com.example.appartamenty.data

import java.sql.Timestamp

data class UtilityPrice(

    var name: String = "",
    var lastReading: Double = 0.0,
    var previousReading: Double = 0.0,
    var pricePerUnit: Double = 0.0,
    var total: Double = 0.0,
    var timestamp: Long = System.currentTimeMillis(),
    var propertyId: String = ""
)