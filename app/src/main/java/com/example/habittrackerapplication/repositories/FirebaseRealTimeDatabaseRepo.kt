package com.example.habittrackerapplication.repositories

import android.util.Log
import com.example.habittrackerapplication.common.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import kotlin.collections.ArrayList


class FirebaseRealTimeDatabaseRepo(val db:FirebaseDatabase) {


    fun getAllEmails():Flow<Resource<ArrayList<String>>> = callbackFlow {
        val emails=ArrayList<String>()
        trySend(Resource.Loading())
        val ref=db.reference.child("emails")
        val lis=ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    emails.add(it.value.toString())
                }
                trySend(Resource.Success(emails))

                Log.d("FirebaseRealTimeDatabaseRepo", "success ${emails}")
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }
        })

        awaitClose()
        //{ref.removeEventListener(lis) }
    }

    fun registerUser(user:FirebaseUser) :Flow<Resource<FirebaseUser>> = callbackFlow {
        trySend(Resource.Loading())
        db.reference
            .child("accounts")
            .child(user.uid)
            .child("profile_details")
            .child("time_of_registration")
            .setValue(Calendar.getInstance().timeInMillis).addOnCompleteListener {
                if(it.isSuccessful){
                    db.reference
                        .child("emails")
                        .child(user!!.uid)
                        .setValue(user.email).addOnCompleteListener {
                            if(it.isSuccessful)
                                trySend(Resource.Success(user))
                            else{
                                trySend(Resource.Error(it.exception!!.message!!))
                            }
                        }
                }else{
                    trySend(Resource.Error(it.exception!!.message!!))
                }
            }
    }

}