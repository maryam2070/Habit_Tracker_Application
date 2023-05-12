package com.example.habittrackerapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.common.getDayName
import com.example.habittrackerapplication.common.getFormattedDate
import com.example.habittrackerapplication.common.getNumberOfDayInMonth
import com.example.habittrackerapplication.databinding.FragmentHomeBinding
import com.example.habittrackerapplication.models.Habit
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.example.habittrackerapplication.repositories.FirebaseRealTimeDatabaseRepo
import com.example.habittrackerapplication.ui.adapters.HabitAdapter
import com.example.habittrackerapplication.viewmodels.HomeFragmentViewModel
import com.example.habittrackerapplication.viewmodels.SignUpFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.time.DayOfWeek

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    lateinit var binding:FragmentHomeBinding


    val auhtRepo= FirebaseAuthRepo(FirebaseAuth.getInstance())
    val dbRepo=FirebaseRealTimeDatabaseRepo(FirebaseDatabase.getInstance())
    val viewModel: HomeFragmentViewModel by lazy {
        ViewModelProvider(this, HomeFragmentViewModel.HomeFragmentViewModelFactory(dbRepo)).get(
            HomeFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater,container,false)


        //viewModel.getAllHabits(auhtRepo.getCurUser()!!.uid)

        viewModel.getAllHabits("EsH7YeqlzKTAnFjOjYGK6AUlTuG2")

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.habits.collect {
                when (it) {
                    is Resource.Error ->
                        Log.d("HomeFragment", "error ${it}")
                    is Resource.Loading ->
                        Log.d("HomeFragment", "Loading ${it}")
                    is Resource.Success -> {
                        cancel()
                        updateUi(it.data!!)
                        Log.d("HomeFragment", "success ${it.data}")

                    }
                }
            }
        }

        return binding.root
    }

    private fun updateUi(list: ArrayList<Habit>) {
        CoroutineScope(Dispatchers.Main).launch {
            var completed = 0
            val todayList=ArrayList<Habit>()
            val curDay = getFormattedDate()
            for (i in 0 until list.size) {
                if (list.get(i).completedDays.contains(curDay)) {
                    list.get(i).isChecked = true
                    completed = completed + 1
                }

                if(list.get(i).dayOfMonth!!.equals(getNumberOfDayInMonth())||list.get(i).dayOfWeek.equals(getDayName())||list.get(i).listOfDays.contains(
                        getDayName()
                    ))
                    todayList.add(list.get(i))
            }
            if (todayList.size == 0) {
                binding.circularPb.progress = 0
                binding.progressTv.text = "0% from your\n today habits done"
            } else {
                binding.circularPb.progress = (completed * 100 / list.size * 100) / 100
                binding.progressTv.text =
                    "${(completed * 100 / list.size * 100) / 100}% from your\n today habits done"
            }
            binding.habitsRv.adapter = HabitAdapter(requireContext(), todayList)
            binding.habitsRv.layoutManager = LinearLayoutManager(context)

            binding.nameTv.text = "Hello " + auhtRepo.getCurUser()!!.displayName
        }
    }

    private fun getCompletedHabitsCount(): Int {
        val cnt=0
        return cnt

    }

}