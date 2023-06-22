package com.example.habittrackerapplication.ui.fragments

import android.app.Dialog
import android.graphics.Canvas
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.databinding.FragmentHabitListBinding
import com.example.habittrackerapplication.models.Habit
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.example.habittrackerapplication.repositories.FirebaseRealTimeDatabaseRepo
import com.example.habittrackerapplication.ui.adapters.HabitAdapter
import com.example.habittrackerapplication.viewmodels.HabitListFragmentViewModel
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kevincodes.recyclerview.ItemDecorator
import kotlinx.coroutines.*
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.get

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HabitListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HabitListFragment : Fragment() ,HabitAdapter.ItemClickListener{

    lateinit var binding:FragmentHabitListBinding

    val dailyHabits=ArrayList<Habit>()
    val weeklyHabits=ArrayList<Habit>()
    val monthlyHabits=ArrayList<Habit>()
    var curHabits=ArrayList<Habit>()
    var userId=""
    val auhtRepo= FirebaseAuthRepo(FirebaseAuth.getInstance())
    val dbRepo= FirebaseRealTimeDatabaseRepo(FirebaseDatabase.getInstance())
    val viewModel: HabitListFragmentViewModel by lazy {
        ViewModelProvider(this, HabitListFragmentViewModel.HabitListFragmentViewModelFactory(dbRepo)).get(
            HabitListFragmentViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHabitListBinding.inflate(layoutInflater,container,false)

        userId= FirebaseAuthRepo(FirebaseAuth.getInstance()).getCurUser()!!.uid
        viewModel.getAllHabits(userId)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.habits.collect {
                when (it) {
                    is Resource.Error ->
                        Log.d("HabitListFragment", "error ${it}")
                    is Resource.Loading ->
                        Log.d("HabitListFragment", "Loading ${it}")
                    is Resource.Success -> {
                        setLists(it.data!!)
                        setRecycler(dailyHabits)
                        curHabits=dailyHabits
                        Log.d("HabitListFragment", "success ${it.data}")

                    }
                }
            }
        }
        binding.addHabitFb.setOnClickListener {
         findNavController().navigate(HabitListFragmentDirections.actionHabitListFragmentToAddNewHabitFragment())
        }


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
                        binding.titleTv.text = "Daily habits"
                        setRecycler(dailyHabits)
                        curHabits=dailyHabits

                    }
                    1 -> {
                        binding.titleTv.text = "Weekly habits"
                        setRecycler(weeklyHabits)
                        curHabits=weeklyHabits

                    }
                    2 -> {
                        binding.titleTv.text = "monthly_habits"
                        setRecycler(monthlyHabits)
                        curHabits=monthlyHabits

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        return binding.root
    }

    private fun setLists(data: ArrayList<Habit>) {
        dailyHabits.clear()
        weeklyHabits.clear()
        monthlyHabits.clear()
        for (i in 0 until data.size) {
            val type=data.get(i).type
            when (type) {
                "daily_habits" ->
                    dailyHabits.add(data.get(i))
                "weekly_habits" ->
                    weeklyHabits.add(data.get(i))
                "monthly_habits" ->
                    monthlyHabits.add(data.get(i))

            }
        }
    }

    private fun setRecycler(list: ArrayList<Habit>) {

        CoroutineScope(Dispatchers.Main).launch {
            binding.habitsRv.adapter = HabitAdapter(requireContext(), list,false,this@HabitListFragment)
            binding.habitsRv.layoutManager = LinearLayoutManager(context)
            val simpleCallback = object :
                ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT
                ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )

                    val colorAlert = ContextCompat.getColor(requireContext(), R.color.orange)
                    val teal200 = ContextCompat.getColor(requireContext(), R.color.teal_200)
                    val defaultWhiteColor = ContextCompat.getColor(requireContext(), R.color.white)

                    ItemDecorator.Builder(c, recyclerView, viewHolder, dX, actionState).set(
                        backgroundColorFromStartToEnd = colorAlert,
                        backgroundColorFromEndToStart = teal200,
                        textFromStartToEnd = "getString(R.string.action_delete)",
                        textFromEndToStart = "Delete",
                        textColorFromStartToEnd = defaultWhiteColor,
                        textColorFromEndToStart = defaultWhiteColor,
                        iconTintColorFromStartToEnd = defaultWhiteColor,
                        iconTintColorFromEndToStart = defaultWhiteColor,
                        textSizeFromStartToEnd = 16f,
                        textSizeFromEndToStart = 16f,
                        typeFaceFromStartToEnd = Typeface.DEFAULT_BOLD,
                        typeFaceFromEndToStart = Typeface.SANS_SERIF,
                        iconResIdFromStartToEnd = R.drawable.icon11,
                        iconResIdFromEndToStart = R.drawable.ic_baseline_delete_24
                    )
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            showConfirmDialog(list.get(position))
                        }
                    }
                }
            }
            ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.habitsRv)

        }
    }

    private fun showConfirmDialog(habit: Habit) {
        val dialog = Dialog(requireContext())

        dialog.setContentView(R.layout.confirm_dialog)

        dialog.show()
        dialog.findViewById<Button>(R.id.yes_btn).setOnClickListener {
            dialog.dismiss()

            viewModel.deleteHabit(userId,habit)

            CoroutineScope(Dispatchers.IO).launch {
                viewModel.deleted.collect {
                    when (it) {
                        is Resource.Error -> {
                            MainScope().launch {
                                Toast.makeText(requireContext(),"error occurred please try again",Toast.LENGTH_SHORT).show()
                            }
                            Log.d("CalenderHistoryFragment", "error ${it}")
                        }
                        is Resource.Loading ->
                            Log.d("CalenderHistoryFragment", "Loading ${it}")
                        is Resource.Success -> {
                            MainScope().launch {
                                Toast.makeText(requireContext(),"deleted",Toast.LENGTH_SHORT).show()
                            }
                            Log.d("CalenderHistoryFragment", "success ${it.data}")

                        }
                    }
                }
            }
        }

        dialog.findViewById<Button>(R.id.no_btn).setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onItemClick(position: Int) {
        findNavController().navigate( HabitListFragmentDirections.actionHabitListFragmentToHabitDetailsFragment(curHabits.get(position)))
    }

    override fun onRadioButtonClick(position: Int) {

    }

}