package com.example.appartamenty.data

data class Tenant(
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var rent: Double? = null,
    var propertyId: String? = null
)