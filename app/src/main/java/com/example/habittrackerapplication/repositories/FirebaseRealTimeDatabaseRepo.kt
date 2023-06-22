package com.example.habittrackerapplication.repositories

import android.util.Log
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.models.Habit
import com.example.habittrackerapplication.models.habitDtoToHabit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.cancel
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
                if(snapshot.hasChildren()) {
                    snapshot.children.forEach {
                        emails.add(it.value.toString())
                    }
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
        channel.close()
        db.reference
            .child("accounts")
            .child(user.uid)
            .child("profile_details")
            .child("time_of_registration")
            .setValue(Calendar.getInstance().timeInMillis)
        db.reference
            .child("emails")
            .child(user!!.uid)
            .setValue(user.email.toString())
            .addOnCompleteListener {
                if(it.isSuccessful){
                    trySend(Resource.Success(user))
                }else{
                    trySend(Resource.Error(it.exception!!.message!!))
                    channel.close()
                }
            }
        awaitClose { cancel() }
    }



    fun addHabit(userId:String,habit: Habit):Flow<Resource<Boolean>> = callbackFlow {

        trySend(Resource.Loading())
        val key=db.reference.child("accounts")
            .child(userId)
            .child("Habits")
            .push().getKey().toString()

        habit.id=key
        db.reference.child("accounts")
            .child(userId)
            .child("Habits")
            .child(key)
            .setValue(habit).addOnSuccessListener {

                trySend(Resource.Success(true))
            }
            .addOnFailureListener {
                trySend(Resource.Error(it.message.toString()))
            }

        awaitClose()
    }
    fun getAllHabits(userId:String):Flow<Resource<ArrayList<Habit>>> = callbackFlow {
        val data=ArrayList<Habit>()
        trySend(Resource.Loading())
        db.reference.child("accounts")
            .child(userId)
            .child("Habits").get().addOnCompleteListener {
                if(it.isSuccessful){
                    it.result.children.forEach {
                        Log.d("FirebaseRealTimeDatabaseRepo", "success ${it.value}")

                        var taskObj = it.getValue() as HashMap<*, *>
                        data.add(habitDtoToHabit(taskObj))
                    }
                    trySend(Resource.Success(data))
                }else{
                    trySend(Resource.Error(it.exception!!.message!!))
                }
            }

        awaitClose()
    }

    fun getTimeOfRegistration(userId:String):Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())
        db.reference.child("accounts")
            .child(userId)
            .child("profile_details")
            .child("time_of_registration").get().addOnCompleteListener {
                if(it.isSuccessful){
                        Log.d("FirebaseRealTimeDatabaseRepo", "success ${it.result.value}")
                    trySend(Resource.Success(it.result.value.toString()))
                }else{
                    Log.d("FirebaseRealTimeDatabaseRepo", "Faild")
                    trySend(Resource.Error(it.exception!!.message!!))
                }
            }

        awaitClose()
    }

    fun deleteHabit(userId:String,habit: Habit):Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading())
        db.reference.child("accounts")
            .child(userId)
            .child("Habits")
            .child(habit.id!!).removeValue()
            .addOnSuccessListener {

                trySend(Resource.Success(true))
            }
            .addOnFailureListener {
                trySend(Resource.Error(it.message.toString()))
            }
        awaitClose()
    }
    fun updateHabitCompletedDays(userId:String,habit: Habit,day:String):Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading())

        habit.completedDays.add(day)
        FirebaseDatabase.getInstance().reference.child("accounts")
            .child((FirebaseAuth.getInstance().currentUser)?.uid!!)
            .child("Habits").child(habit.id!!).child("completedDays")
            .setValue(habit.completedDays)

        FirebaseDatabase.getInstance().reference.child("accounts")
            .child((FirebaseAuth.getInstance().currentUser)?.uid!!)
            .child("Habits").child(habit.id!!).child("days").setValue(habit.days!!+1).addOnSuccessListener {

                trySend(Resource.Success(true))
            }.addOnFailureListener {
                trySend(Resource.Error(it.message.toString()))
            }
        awaitClose()
    }

}