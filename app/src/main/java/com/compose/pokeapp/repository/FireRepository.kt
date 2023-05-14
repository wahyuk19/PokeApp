package com.compose.pokeapp.repository

import com.compose.pokeapp.data.DataOrException
import com.compose.pokeapp.model.MPoke
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val queryPoke: Query
) {
    suspend fun getListFromDatabase(): DataOrException<List<MPoke>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MPoke>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data =  queryPoke.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MPoke::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false


        }catch (exception: FirebaseFirestoreException){
            dataOrException.e = exception
        }
        return dataOrException

    }
}