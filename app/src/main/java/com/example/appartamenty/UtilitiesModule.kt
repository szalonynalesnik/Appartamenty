package com.example.appartamenty

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideQueryUtilitiesByName(propertyId: String) = FirebaseFirestore.getInstance()
        .collection("utilities")
        .whereEqualTo("propertyId", propertyId)
        .orderBy("name")
}