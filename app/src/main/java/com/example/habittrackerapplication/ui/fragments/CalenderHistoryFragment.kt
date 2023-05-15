package com.example.habittrackerapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.common.getDayName
import com.example.habittrackerapplication.common.getFormattedDate
import com.example.habittrackerapplication.common.getNumberOfDayInMonth
import com.example.habittrackerapplication.databinding.FragmentCalenderHistoryBinding
import com.example.habittrackerapplication.models.Habit
import com.example.habittrackerapplication.repositories.FirebaseRealTimeDatabaseRepo
import com.example.habittrackerapplication.ui.adapters.HabitAdapter
import com.example.habittrackerapplication.viewmodels.CalenderHistoryFragmentViewModel
import com.example.habittrackerapplication.viewmodels.HomeFragmentViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.time.Month
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalenderHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalenderHistoryFragment : Fragment() {


    lateinit var binding: FragmentCalenderHistoryBinding

    val dbRepo=FirebaseRealTimeDatabaseRepo(FirebaseDatabase.getInstance())
    val viewModel: CalenderHistoryFragmentViewModel by lazy {
        ViewModelProvider(this, CalenderHistoryFragmentViewModel.CalenderHistoryFragmentViewModelFactory(dbRepo)).get(
            CalenderHistoryFragmentViewModel::class.java)
    }

    var habits=kotlin.collections.ArrayList<Habit>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCalenderHistoryBinding.inflate(layoutInflater,container,false)

        viewModel.getAllHabits("EsH7YeqlzKTAnFjOjYGK6AUlTuG2")
        viewModel.getTimeOfRegistration("EsH7YeqlzKTAnFjOjYGK6AUlTuG2")


        CoroutineScope(Dispatchers.IO).launch {
            viewModel.habits.collect {
                when (it) {
                    is Resource.Error ->
                        Log.d("HomeFragment", "error ${it}")
                    is Resource.Loading ->
                        Log.d("HomeFragment", "Loading ${it}")
                    is Resource.Success -> {
                        cancel()
                        habits=it.data!!
                        val calendar=Calendar.getInstance()
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.titleTv.text=getDateString(calendar[Calendar.YEAR],calendar[Calendar.MONTH],calendar[Calendar.DAY_OF_MONTH])
                        }
                        updateUi(it.data!!, getFormattedDate(calendar),getNumberOfDayInMonth(calendar),getDayName(calendar),calendar.timeInMillis)
                        Log.d("HomeFragment", "success ${it.data}")
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.timeOfRegistration.collect {
                when (it) {
                    is Resource.Error ->
                        Log.d("HomeFragment", "error ${it}")
                    is Resource.Loading ->
                        Log.d("HomeFragment", "Loading ${it}")
                    is Resource.Success -> {
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.calendarCv.minDate=it.data.toString().toLong()
                        }
                        Log.d("HomeFragment", "success ${it.data}")
                    }
                }
            }
        }
        binding.calendarCv.setOnDateChangeListener(
            CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->

                val calendar= Calendar.getInstance()
                calendar.set(Calendar.YEAR,year)
                calendar.set(Calendar.MONTH,month)
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)


                updateUi(habits, getFormattedDate(calendar),dayOfMonth,getDayName(calendar),calendar.timeInMillis)
                binding.titleTv.setText("All habits in ${getDateString(year,month+1,dayOfMonth)}")

            })

        return binding.root
    }

    private fun updateUi(list: ArrayList<Habit>,selectedDay:String,dayOfMonth: Int,dayName:String,timeInMillis:Long) {
        CoroutineScope(Dispatchers.Main).launch {
            var completed = 0
            val todayList=ArrayList<Habit>()
            for (i in 0 until list.size) {
                if (list.get(i).completedDays.contains(selectedDay)) {
                    list.get(i).isChecked = true
                    completed = completed + 1
                }

                if((list.get(i).dayOfMonth!!.equals(dayOfMonth)||list.get(i).dayOfWeek.equals(
                        dayName
                    )||list.get(i).listOfDays.contains(
                        dayName
                    )) && list.get(i).timeOfAdding<=timeInMillis)
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

        }
    }

    private fun getDateString(year: Int, month: Int, dayOfMonth: Int): String {
        var m=""
        when(month){
            1->m="january"
            2->m="february"
            3->m="march"
            4->m="april"
            5->m="May"
            6->m="June"
            7->m="July"
            8->m="August"
            9->m="September"
            10->m="October"
            11->m="November"
            12->m="December"
        }
        return "$dayOfMonth, $m $year"
    }
}