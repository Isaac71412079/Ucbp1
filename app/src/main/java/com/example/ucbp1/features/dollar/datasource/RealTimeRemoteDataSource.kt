package com.example.ucbp1.features.dollar.datasource

import android.util.Log
import com.example.ucbp1.features.dollar.domain.model.Dollar
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealTimeRemoteDataSource {

    fun getDollarUpdates(): Flow<Result<Dollar>> = callbackFlow {
        val database = Firebase.database
        val myRef = database.getReference("dollar")

        val callback = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("RealTimeRemoteDataSource", "Firebase error: ${error.message}")
                trySend(Result.failure(error.toException()))
                close(error.toException()) // cerramos el flow si ocurre error
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Dollar::class.java)
                if (value != null) {
                    trySend(Result.success(value))
                } else {
                    trySend(Result.failure(Exception("No se pudo obtener el valor del dólar")))
                }
            }
        }

        myRef.addValueEventListener(callback)

        awaitClose {
            myRef.removeEventListener(callback)
        }
    }
}
