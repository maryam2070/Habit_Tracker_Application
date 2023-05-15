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

class AddNewHabitFragmentViewModel (private val databaseRepo: FirebaseRealTimeDatabaseRepo
): ViewModel() {


    private val _finished= MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val finished: MutableStateFlow<Resource<Boolean>>
        get() = _finished


    fun insertHabit(userId:String,habit: Habit) = viewModelScope.launch {
        databaseRepo.addHabit(userId,habit).collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("HabitListFragmentViewModel", "error ${it}")
                is Resource.Loading ->
                    Log.d("HabitListFragmentViewModel", "Loading ${it}")
                is Resource.Success ->{
                    _finished.value=it
                    Log.d("HabitListFragmentViewModel", "success ${it.data}")

                }
            }
        }
    }



    class AddNewHabitFragmentViewModelFactory(
        private val databaseRepo: FirebaseRealTimeDatabaseRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddNewHabitFragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddNewHabitFragmentViewModel(databaseRepo) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}