package com.example.habittrackerapplication.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() ,HabitAdapter.ItemClickListener{

    lateinit var binding:FragmentHomeBinding


    val auhtRepo= FirebaseAuthRepo(FirebaseAuth.getInstance())
    val dbRepo=FirebaseRealTimeDatabaseRepo(FirebaseDatabase.getInstance())
    val viewModel: HomeFragmentViewModel by lazy {
        ViewModelProvider(this, HomeFragmentViewModel.HomeFragmentViewModelFactory(dbRepo)).get(
            HomeFragmentViewModel::class.java)
    }
    val todayList=ArrayList<Habit>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater,container,false)


        val userId= FirebaseAuthRepo(FirebaseAuth.getInstance()).getCurUser()!!.uid
        viewModel.getAllHabits(userId)

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

            val curDay = getFormattedDate(Calendar.getInstance())
            for (i in 0 until list.size) {
                if (list.get(i).completedDays.contains(curDay)) {
                    list.get(i).isChecked = true
                    completed = completed + 1
                }

                if(list.get(i).dayOfMonth!!.equals(getNumberOfDayInMonth(Calendar.getInstance()))||list.get(i).dayOfWeek.equals(getDayName(Calendar.getInstance()))||list.get(i).listOfDays.contains(
                        getDayName(Calendar.getInstance())
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
            binding.habitsRv.adapter = HabitAdapter(requireContext(), todayList, itemClickListener = this@HomeFragment)
            binding.habitsRv.layoutManager = LinearLayoutManager(context)


            binding.nameTv.text = "Hello " + auhtRepo.getCurUser()!!.displayName
            Glide.with(requireContext())
                .load(auhtRepo.getCurUser()!!.photoUrl)
                .placeholder(R.drawable.user_avatar)
                //.centerCrop()
                .circleCrop()
                .into(binding.profileIv)
        }
    }

    override fun onItemClick(position: Int) {
        //
    }

    override fun onRadioButtonClick(position: Int) {
        viewModel.updateHabitCompletedDays(FirebaseAuthRepo(FirebaseAuth.getInstance()).getCurUser()!!.uid,
        todayList.get(position),
        getFormattedDate(Calendar.getInstance())
        )

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.updated.collect {
                when (it) {
                    is Resource.Error ->
                        Log.d("HomeFragment", "error ${it}")
                    is Resource.Loading ->
                        Log.d("HomeFragment", "Loading ${it}")
                    is Resource.Success -> {
                        var d: Dialog = Dialog(requireContext())
                        d.setContentView(R.layout.check_habit_dialog)
                        d.show()
                        Log.d("HomeFragment", "success ${it.data}")

                    }
                }
            }
        }

    }




}