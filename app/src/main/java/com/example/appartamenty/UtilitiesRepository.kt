package com.example.appartamenty

import com.example.appartamenty.data.Utility
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UtilitiesRepository @Inject constructor(
    private val queryUtilitiesByName: Query,
    propertyId: String
) {
    suspend fun getUtilitiesFromFirestore(propertyId: String): DataOrException<List<Utility>, Exception> {
        val dataOrException = DataOrException<List<Utility>, Exception>()
        try {
            dataOrException.data = queryUtilitiesByName.get().await().map { document ->
                document.toObject(Utility::class.java)
            }
        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e
        }
        return dataOrException
    }
}