package com.example.habittrackerapplication.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.models.Habit
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.example.habittrackerapplication.repositories.FirebaseRealTimeDatabaseRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EditEmailFragmentViewModel(private val authRepo: FirebaseAuthRepo
): ViewModel() {


    private val _editEmail = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val editEmail: MutableStateFlow<Resource<Boolean>>
        get() = _editEmail



    fun editEmail(email:String, pass:String) = viewModelScope.launch {
        authRepo.editEmail(email, pass).collect{
            when(it)
            {
                is Resource.Error -> {
                    _editEmail.value = it
                    Log.d("EditEmailFragmentViewModel", "error ${it}")
                }
                is Resource.Loading ->
                    Log.d("EditEmailFragmentViewModel", "Loading ${it}")
                is Resource.Success ->{
                    _editEmail.value=it
                    Log.d("EditEmailFragmentViewModel", "success ${it.data}")

                }
            }
        }
    }

    class EditEmailFragmentViewModelFactory(
        private val authRepo: FirebaseAuthRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditEmailFragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EditEmailFragmentViewModel(authRepo) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}