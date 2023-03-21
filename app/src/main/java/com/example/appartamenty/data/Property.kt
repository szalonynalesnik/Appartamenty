package com.example.appartamenty.data

data class Property(
    var street: String? = null,
    var streetNo: String? = null,
    var houseNo: String? = null,
    var postalCode: String? = null,
    var city: String? = null,
    var landlordId: String
)
