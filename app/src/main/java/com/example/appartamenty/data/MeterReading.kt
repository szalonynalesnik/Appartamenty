package com.example.appartamenty.data

data class MeterReading(
    var date: String? = null,
    var utilityName: String? = null,
    var value: Number = 0.0,
    var utilityId: String? = null,
)
