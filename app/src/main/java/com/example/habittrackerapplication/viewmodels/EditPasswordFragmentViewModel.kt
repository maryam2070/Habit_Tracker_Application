package com.example.habittrackerapplication.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EditPasswordFragmentViewModel(private val authRepo: FirebaseAuthRepo
): ViewModel() {


    private val _editPass = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val editPass: MutableStateFlow<Resource<Boolean>>
        get() = _editPass



    fun editPassword(pass:String, newPass:String) = viewModelScope.launch {
        authRepo.updatePassword(pass, newPass).collect{
            when(it)
            {
                is Resource.Error -> {
                    _editPass.value = it
                    Log.d("EditPasswordFragmentViewModel", "error ${it}")
                }
                is Resource.Loading ->
                    Log.d("EditPasswordFragmentViewModel", "Loading ${it}")
                is Resource.Success ->{
                    _editPass.value=it
                    Log.d("EditPasswordFragmentViewModel", "success ${it.data}")

                }
            }
        }
    }

    class EditPasswordFragmentViewModelFactory(
        private val authRepo: FirebaseAuthRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditPasswordFragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EditPasswordFragmentViewModel(authRepo) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}