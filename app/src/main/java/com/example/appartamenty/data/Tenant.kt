package com.example.appartamenty.data

import java.io.Serializable

data class Tenant(
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var rent: Double? = null,
    var propertyId: String? = null,
    var tenantId: String? = null,
): Serializable