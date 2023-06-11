package com.example.habittrackerapplication.viewmodels

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.example.habittrackerapplication.repositories.FirebaseRealTimeDatabaseRepo
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginFragmentViewModel (private val context:Context,private val authRepo : FirebaseAuthRepo
): ViewModel() {


    private val _user = MutableStateFlow<Resource<FirebaseUser>>(Resource.Loading())
    val user: MutableStateFlow<Resource<FirebaseUser>>
        get() = _user

    fun loginWithGoogle(account: GoogleSignInAccount) = viewModelScope.launch {
        authRepo.loginWithGoogle(account) .collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("LoginFragmentViewModel", "error ${it}")
                is Resource.Loading ->
                    Log.d("LoginFragmentViewModel", "Loading ${it}")
                is Resource.Success ->{
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("email_provider",false).apply()
                    _user.value=it
                    Log.d("LoginFragmentViewModel", "success ${it.data}")
                }
            }
        }
    }

    fun loginWithEmailAndPassword(email:String,pass:String) = viewModelScope.launch {
        authRepo.loginWithEmailAndPassword(email, pass).collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("LoginFragmentViewModel", "error ${it}")
                is Resource.Loading ->
                    Log.d("LoginFragmentViewModel", "Loading ${it}")
                is Resource.Success ->{
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("email_provider",true).apply()
                    _user.value=it
                    Log.d("LoginFragmentViewModel", "success ${it.data}")
                }
            }
        }
    }

    class LoginFragmentViewModelFactory(
        private val context: Context,
        private val authRepo: FirebaseAuthRepo,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginFragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginFragmentViewModel(context,authRepo) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}