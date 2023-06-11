package com.example.habittrackerapplication.repositories

import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.example.habittrackerapplication.common.DataListener
import com.example.habittrackerapplication.common.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.FirebaseDatabase
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
                //return@addOnCompleteListener
                Log.d("FirebaseAuthRepo","Success")
            } else {
                trySend(Resource.Error(it.exception!!.message!!))
                channel.close()

                Log.d("FirebaseAuthRepo","Error")
            }
        }
        awaitClose{cancel()}
    }
    fun getCurUser(): FirebaseUser? {
        return auth.currentUser
    }


    fun editEmail(email: String,pass:String):Flow<Resource<Boolean>> = callbackFlow{

        var credential = EmailAuthProvider.getCredential(FirebaseAuth.getInstance().currentUser!!.email.toString(),pass)

        FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential).addOnCompleteListener {
            if (it.isSuccessful()) {
                FirebaseAuth.getInstance().currentUser!!
                    .updateEmail(email)
                    .addOnSuccessListener {

                        FirebaseDatabase.getInstance()
                            .reference
                            .child("emails")
                            .child(FirebaseAuth.getInstance().currentUser!!!!.uid)
                            .setValue(email)
                        trySend(Resource.Success(true))
                    }
                    .addOnFailureListener {
                        trySend(Resource.Error(it.message!!))
                    }
            } else {
                trySend(Resource.Error(it.exception!!.message!!))
            }
        }
        awaitClose{cancel()}
    }

    fun updatePassword(pass: String,newPass:String):Flow<Resource<Boolean>> = callbackFlow{

        var credential =EmailAuthProvider.getCredential(FirebaseAuth.getInstance().currentUser!!.email.toString(),pass)

        FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential).addOnCompleteListener {
            if(it.isSuccessful()){
                FirebaseAuth.getInstance().currentUser!!
                    .updatePassword(newPass)
                    .addOnSuccessListener{

                        trySend(Resource.Success(true))
                    }
                    .addOnFailureListener {
                        trySend(Resource.Error(it.message!!))
                    }
            }else {
                trySend(Resource.Error(it.exception!!.message!!))
            }
        }
        awaitClose{cancel()}

    }


    fun loginWithEmailAndPassword(email: String,pass:String):Flow<Resource<FirebaseUser>> = callbackFlow {
        auth.signInWithEmailAndPassword(
            email,pass
        )
            .addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(Resource.Success(it.result.user!!))
                 else
                    trySend(Resource.Error(it.exception!!.message!!))

            }
        awaitClose{cancel()}
    }
    fun loginWithGoogle(account: GoogleSignInAccount):Flow<Resource<FirebaseUser>> = callbackFlow{
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                trySend(Resource.Success(task.result.user!!))
            }else{
                trySend(Resource.Error(task.exception!!.message!!))
            }
        }

        awaitClose{cancel()}
    }
}