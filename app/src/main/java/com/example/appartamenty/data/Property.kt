package com.example.appartamenty.data

import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class Property (
    @get: PropertyName("propertyId") @set: PropertyName("propertyId") var propertyId: String? = "",
    @get: PropertyName("street") @set: PropertyName("street") var street: String? = "",
    @get: PropertyName("streetNo") @set: PropertyName("streetNo") var streetNo: String? = "",
    @get: PropertyName("houseNo") @set: PropertyName("houseNo") var houseNo: String? = "",
    @get: PropertyName("postalCode") @set: PropertyName("postalCode") var postalCode: String? = "",
    @get: PropertyName("city") @set: PropertyName("city") var city: String? = "",
    @get: PropertyName("landlordId") @set: PropertyName("landlordId") var landlordId: String? = ""
    ): Serializable


