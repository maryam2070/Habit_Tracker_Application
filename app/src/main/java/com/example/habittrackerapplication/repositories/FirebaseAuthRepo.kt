package com.example.habittrackerapplication.repositories

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.util.Log
import com.example.habittrackerapplication.common.DataListener
import com.example.habittrackerapplication.common.Resource
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseAuthRepo(var auth:FirebaseAuth) {

    fun registerUserByEmailAndPass(email: String,pass:String):Flow<Resource<FirebaseUser>> = callbackFlow{

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(Resource.Success(it.result.user!!))
                channel.close()

                Log.d("FirebaseAuthRepo","Success")
            } else {
                trySend(Resource.Error(it.exception!!.message!!))
                channel.close()

                Log.d("FirebaseAuthRepo","Error")
            }
        }
        awaitClose{cancel()}
    }

}