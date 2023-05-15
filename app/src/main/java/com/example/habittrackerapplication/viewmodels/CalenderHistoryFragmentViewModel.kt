package com.example.habittrackerapplication.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.models.Habit
import com.example.habittrackerapplication.repositories.FirebaseRealTimeDatabaseRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CalenderHistoryFragmentViewModel (private val databaseRepo: FirebaseRealTimeDatabaseRepo
): ViewModel() {


    private val _habits = MutableStateFlow<Resource<ArrayList<Habit>>>(Resource.Loading())
    val habits: MutableStateFlow<Resource<ArrayList<Habit>>>
        get() = _habits


    private val _timeOfRegistration = MutableStateFlow<Resource<String>>(Resource.Loading())
    val timeOfRegistration: MutableStateFlow<Resource<String>>
        get() = _timeOfRegistration


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

    fun getTimeOfRegistration(userId:String) = viewModelScope.launch {
        databaseRepo.getTimeOfRegistration(userId).collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("HomeFragmentViewModel", "error ${it}")
                is Resource.Loading ->
                    Log.d("HomeFragmentViewModel", "Loading ${it}")
                is Resource.Success ->{
                    _timeOfRegistration.value=it
                    Log.d("HomeFragmentViewModel", "success ${it.data}")

                }
            }
        }
    }

    class CalenderHistoryFragmentViewModelFactory(
        private val databaseRepo: FirebaseRealTimeDatabaseRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CalenderHistoryFragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CalenderHistoryFragmentViewModel(databaseRepo) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}