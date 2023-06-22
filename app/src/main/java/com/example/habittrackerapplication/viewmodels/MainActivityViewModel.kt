package com.example.habittrackerapplication.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel (private val repo: FirebaseAuthRepo): ViewModel() {


    private val _loading = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val loading: MutableStateFlow<Resource<Boolean>>
        get() = _loading

    fun uploadImage(uri:Uri) = viewModelScope.launch {
        repo.uploadImage(uri).collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("MainActivityViewModel", "error ${it}")
                is Resource.Loading ->
                    Log.d("MainActivityViewModel", "Loading ${it}")
                is Resource.Success ->{
                    loading.value=it
                    Log.d("MainActivityViewModel", "success ${it.data}")
                }
            }
        }
    }

    fun logout(){
        repo.logout()
    }


    class MainActivityViewModelFactory(
        private val repo: FirebaseAuthRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(repo) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}