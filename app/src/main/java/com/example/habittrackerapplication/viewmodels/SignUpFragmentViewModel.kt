package com.example.habittrackerapplication.viewmodels

import android.content.Context
import android.preference.PreferenceManager
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.habittrackerapplication.common.DataListener
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.models.Email
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.example.habittrackerapplication.repositories.FirebaseRealTimeDatabaseRepo
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
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


    fun signUpWithGoogle(account: GoogleSignInAccount) = viewModelScope.launch {
        authRepo.loginWithGoogle(account) .collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("LoginFragmentViewModel", "error ${it}")
                is Resource.Loading ->
                    Log.d("LoginFragmentViewModel", "Loading ${it}")
                is Resource.Success ->{
                    insertUserIntoDatabase(it.data!!)
                    Log.d("LoginFragmentViewModel", "success ${it.data}")
                }
            }
        }
    }
    fun registerUser(email: String, pass: String,name:String) = viewModelScope.launch {
        authRepo.registerUserByEmailAndPass(email,pass,name) .collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("SignUpFragmentViewModel", "error ${it}")
                is Resource.Loading ->
                    Log.d("SignUpFragmentViewModel", "Loading ${it}")
                is Resource.Success ->{

                    Log.d("SignUpFragmentViewModel", "success ${it.data}")
                    insertUserIntoDatabase(it.data!!)
                }
            }
        }
    }

    private fun insertUserIntoDatabase(user: FirebaseUser)=viewModelScope.launch {
        databaseRepo.registerUser(user).collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("SignUpFragmentViewModel", "error ${it}")
                is Resource.Loading ->
                    Log.d("SignUpFragmentViewModel", "Loading ${it}")
                is Resource.Success ->{
                    _user.value=it
                    Log.d("SignUpFragmentViewModel", "success user ${it.data}")
                }
            }
        }

    }

    fun getAllEmails() = viewModelScope.launch {
        databaseRepo.getAllEmails().collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("SignUpFragmentViewModel", "error ${it}")
                is Resource.Loading ->
                    Log.d("SignUpFragmentViewModel", "Loading ${it}")
                is Resource.Success ->{
                    _emails.value=it
                    Log.d("SignUpFragmentViewModel", "success ${it.data}")
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