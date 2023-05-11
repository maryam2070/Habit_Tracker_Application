package com.example.habittrackerapplication.viewmodels

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.habittrackerapplication.common.DataListener
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.models.Email
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.example.habittrackerapplication.repositories.FirebaseRealTimeDatabaseRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignUpFragmentViewModel(private val authRepo :FirebaseAuthRepo,
                              private val databaseRepo: FirebaseRealTimeDatabaseRepo):ViewModel() {


    private val _emails = MutableStateFlow<Resource<ArrayList<String>>>(Resource.Loading())
    val emails:MutableStateFlow<Resource<ArrayList<String>>>
        get() = _emails

    private val _user = MutableStateFlow<Resource<FirebaseUser>>(Resource.Loading())
    val user:MutableStateFlow<Resource<FirebaseUser>>
        get() = _user

    fun registerUser(email: String, pass: String) = viewModelScope.launch {
        authRepo.registerUserByEmailAndPass(email,pass) .collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("SignUpFragment", "error ${it}")
                is Resource.Loading ->
                    Log.d("SignUpFragment", "Loading ${it}")
                is Resource.Success ->{
                    insertUserIntoDatabase(it.data!!)
                    Log.d("SignUpFragment", "success ${it.data}")
                }
            }
        }
    }

    private fun insertUserIntoDatabase(user: FirebaseUser)=viewModelScope.launch {
        databaseRepo.registerUser(user).collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("SignUpFragment", "error ${it}")
                is Resource.Loading ->
                    Log.d("SignUpFragment", "Loading ${it}")
                is Resource.Success ->{
                    _user.value=it
                    Log.d("SignUpFragment", "success ${it.data}")
                }
            }
        }

    }

    fun getAllEmails() = viewModelScope.launch {

        databaseRepo.getAllEmails().collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("SignUpFragment", "error ${it}")
                is Resource.Loading ->
                    Log.d("SignUpFragment", "Loading ${it}")
                is Resource.Success ->{
                    _emails.value=it
                    Log.d("SignUpFragment", "success ${it.data}")
                }
            }
        }
    }


    class SignUpFragmentViewModelFactory(
        private val authRepo: FirebaseAuthRepo,
        private val databaseRepo: FirebaseRealTimeDatabaseRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignUpFragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SignUpFragmentViewModel(authRepo, databaseRepo) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}