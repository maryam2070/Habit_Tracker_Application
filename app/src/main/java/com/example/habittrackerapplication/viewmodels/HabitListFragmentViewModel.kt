package com.example.habittrackerapplication.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.models.Habit
import com.example.habittrackerapplication.repositories.FirebaseRealTimeDatabaseRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HabitListFragmentViewModel (private val databaseRepo: FirebaseRealTimeDatabaseRepo
): ViewModel() {


    private val _habits = MutableStateFlow<Resource<ArrayList<Habit>>>(Resource.Loading())
    val habits: MutableStateFlow<Resource<ArrayList<Habit>>>
        get() = _habits

    private val _deleted = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val deleted: MutableStateFlow<Resource<Boolean>>
        get() = _deleted


    fun getAllHabits(userId:String) = viewModelScope.launch {
        databaseRepo.getAllHabits(userId).collect{
            when(it)
            {
                is Resource.Error ->
                    Log.d("HabitListFragmentViewModel", "error ${it}")
                is Resource.Loading ->
                    Log.d("HabitListFragmentViewModel", "Loading ${it}")
                is Resource.Success ->{
                    _habits.value=it
                    Log.d("HabitListFragmentViewModel", "success ${it.data}")

                }
            }
        }
    }

    fun deleteHabit(userId:String,habit: Habit) = viewModelScope.launch {
        databaseRepo.deleteHabit(userId, habit).collect{
            when(it)
            {
                is Resource.Error -> {
                    _deleted.value = it
                    Log.d("HabitListFragmentViewModel", "error ${it}")
                }
                is Resource.Loading ->
                    Log.d("HabitListFragmentViewModel", "Loading ${it}")
                is Resource.Success ->{
                    _deleted.value=it
                    Log.d("HabitListFragmentViewModel", "success ${it.data}")

                }
            }
        }
    }



    class HabitListFragmentViewModelFactory(
        private val databaseRepo: FirebaseRealTimeDatabaseRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HabitListFragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HabitListFragmentViewModel(databaseRepo) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}