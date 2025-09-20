package com.example.ucbp1.features.dollar.data.datasource

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

    suspend fun getDollarUpdates(): Flow<Dollar> = callbackFlow {
        val callback = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                close(p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
//                val value = p0.getValue(String::class.java)
                val value = p0.getValue(Dollar::class.java)
                if (value != null) {
                    trySend(value)
                }
            }
        }

//         Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("dollar")
        myRef.addValueEventListener(callback)

        awaitClose {
            myRef.removeEventListener(callback)
        }
    }
}