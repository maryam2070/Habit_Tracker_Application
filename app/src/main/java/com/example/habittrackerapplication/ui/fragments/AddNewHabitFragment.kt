package com.example.habittrackerapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.common.PagerAdapter
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.common.getIconArray
import com.example.habittrackerapplication.databinding.FragmentAddNewHabitBinding
import com.example.habittrackerapplication.models.Habit
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.example.habittrackerapplication.repositories.FirebaseRealTimeDatabaseRepo
import com.example.habittrackerapplication.ui.adapters.IconAdapter
import com.example.habittrackerapplication.viewmodels.AddNewHabitFragmentViewModel
import com.example.habittrackerapplication.viewmodels.HabitListFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddNewHabitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNewHabitFragment : Fragment() {


    lateinit var binding: FragmentAddNewHabitBinding

    var dayOfMonth:Int?=null

    var f1= TabViewOneFragment.newInstance()
    var f2= TabViewTwoFragment.newInstance()
    var f3= TabViewThreeFragment.newInstance()
    lateinit var iconAdapter: IconAdapter
    val repo=FirebaseRealTimeDatabaseRepo(FirebaseDatabase.getInstance())
    val viewModel:AddNewHabitFragmentViewModel by lazy {
        ViewModelProvider(this, AddNewHabitFragmentViewModel.AddNewHabitFragmentViewModelFactory(repo)).get(
            AddNewHabitFragmentViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddNewHabitBinding.inflate(layoutInflater,container,false)

        init()
        binding.saveBtn.setOnClickListener {
            if (binding.nameTil.editText!!.text.isEmpty())
                Toast.makeText(requireContext(), "Please Enter Data", Toast.LENGTH_SHORT).show()
            else {
                handleHabitType()
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.finished.collect {
                when (it) {
                    is Resource.Error ->
                        Log.d("HomeFragment", "error ${it}")
                    is Resource.Loading ->
                        Log.d("HomeFragment", "Loading ${it}")
                    is Resource.Success -> {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(
                                requireContext(),
                                "Habit Saved Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                            Log.d("HomeFragment", "success ${it.data}")
                    }
                }
            }
        }
        return binding.root
    }

   private fun handleHabitType() {
        var list = ArrayList<String>()
       var id=FirebaseAuth.getInstance().currentUser!!.uid
       id="EsH7YeqlzKTAnFjOjYGK6AUlTuG2"
        if (binding.tabLayout.selectedTabPosition.equals(0)) {
            viewModel.insertHabit(id,Habit("",binding.nameTil.editText!!.text.toString(),0,list,iconAdapter.getSelectedItem(),false,"",f1.getSelectedDays(),0,"daily_habits"))
        }else if(binding.tabLayout.selectedTabPosition.equals(1)){
            println("AAAAAAAA   "+f2.requireArguments().getString("day"))
            viewModel.insertHabit(id,Habit("",binding.nameTil.editText!!.text.toString(),0,list,iconAdapter.getSelectedItem(),false,f2.requireArguments().getString("day"),list,0,"weekly_habits"))
        }else{
            viewModel.insertHabit(id,Habit("",binding.nameTil.editText!!.text.toString(),0,list,iconAdapter.getSelectedItem(),false,"",list,f3.requireArguments().getInt("day"),"monthly_habits"))
        }
    }
    fun init()
    {
        val list= getIconArray()
        iconAdapter=IconAdapter(requireContext(), list, binding.iconRv)
        binding.iconRv.adapter= iconAdapter
        binding.iconRv.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.viewPagerFragment.adapter = PagerAdapter(requireActivity().supportFragmentManager,f1,f2,f3)

       binding.tabLayout.setupWithViewPager(binding.viewPagerFragment)
    }

}