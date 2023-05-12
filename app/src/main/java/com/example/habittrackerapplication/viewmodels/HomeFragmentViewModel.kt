package com.example.habittrackerapplication.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.models.Habit
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.example.habittrackerapplication.repositories.FirebaseRealTimeDatabaseRepo
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private val databaseRepo: FirebaseRealTimeDatabaseRepo
): ViewModel() {


    private val _habits = MutableStateFlow<Resource<ArrayList<Habit>>>(Resource.Loading())
    val habits: MutableStateFlow<Resource<ArrayList<Habit>>>
        get() = _habits


    fun getAllHabits(userId:String) = viewModelScope.launch {
        databaseRepo.getAllHabits(userId).collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("HomeFragmentViewModel", "error ${it}")
                is Resource.Loading ->
                    Log.d("HomeFragmentViewModel", "Loading ${it}")
                is Resource.Success ->{
                    _habits.value=it
                    Log.d("HomeFragmentViewModel", "success ${it.data}")

                }
            }
        }
    }



    class HomeFragmentViewModelFactory(
        private val databaseRepo: FirebaseRealTimeDatabaseRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeFragmentViewModel(databaseRepo) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}