package com.example.appartamenty.data

import java.io.Serializable

data class TotalRentForLandlord(
    var totalRentFromTenants : Double = 0.0,
    var totalRentFromUsage : Double = 0.0,
    var numberOfTenants : Int = 0,
    var totalRent : Double = 0.0,
    var timestamp: Long = System.currentTimeMillis(),
    var propertyId: String = ""
) : Serializable
