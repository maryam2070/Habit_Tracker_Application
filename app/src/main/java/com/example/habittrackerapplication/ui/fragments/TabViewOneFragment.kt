package com.example.habittrackerapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.databinding.FragmentTabViewOneBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TabViewOneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TabViewOneFragment : Fragment() ,View.OnClickListener {


    lateinit var binding: FragmentTabViewOneBinding

    private var param1: String? = null
    private var param2: String? = null

    var day:String=""
    var isCheckedV1=false
    var isCheckedV2=false
    var isCheckedV3=false
    var isCheckedV4=false
    var isCheckedV5=false
    var isCheckedV6=false
    var isCheckedV7=false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTabViewOneBinding.inflate(inflater, container, false)



        binding.saturdayTv.setOnClickListener{
            requireArguments().putString("day","Saturday")
            setColorBackgroundV1()
            Log.d("day og weak  ", requireArguments().getString("day").toString())
        }
        binding.sundayTv.setOnClickListener{
            requireArguments().putString("day","Sunday")
            setColorBackgroundV2()
            Log.d("day og weak  ", requireArguments().getString("day").toString())
        }
        binding.mondayTv.setOnClickListener{
            day="saturday"
            requireArguments().putString("day","Monday")
            setColorBackgroundV3()
            Log.d("day og weak  ", requireArguments().getString("day").toString())
        }
        binding.tuesdayTv.setOnClickListener{
            day="saturday"
            requireArguments().putString("day","Tuesday")
            setColorBackgroundV4()
            Log.d("day og weak  ", requireArguments().getString("day").toString())
        }
        binding.wednesdayTv.setOnClickListener{
            day="saturday"
            setColorBackgroundV5()
            requireArguments().putString("day","Wednesday")
            Log.d("day og weak  ", requireArguments().getString("day").toString())
        }
        binding.fridayTv.setOnClickListener{
            day="saturday"
            requireArguments().putString("day","Friday")
            setColorBackgroundV7()
            Log.d("day og weak  ", requireArguments().getString("day").toString())
        }
        binding.thursdayTv.setOnClickListener{
            day="saturday"
            requireArguments().putString("day","Thursday")
            setColorBackgroundV6()
            Log.d("day og weak  ", requireArguments().getString("day").toString())
        }
        return binding.root

    }
    fun setColorBackgroundV1() {
        if (!isCheckedV1) {
            binding.saturdayTv.setBackgroundResource(R.drawable.text_view_background)
            isCheckedV1 = true
        } else {
            binding.saturdayTv.setBackgroundResource(R.color.secondary_purple)
            isCheckedV1=false
        }
    }
    fun setColorBackgroundV2() {
        if (!isCheckedV2) {
            binding.sundayTv.setBackgroundResource(R.drawable.text_view_background)
            isCheckedV2 = true
        } else {
            binding.sundayTv.setBackgroundResource(R.color.secondary_purple)
            isCheckedV2=false
        }
    }
    fun setColorBackgroundV3() {
        if (!isCheckedV3) {
            binding.mondayTv.setBackgroundResource(R.drawable.text_view_background)
            isCheckedV3 = true
        } else {
            binding.mondayTv.setBackgroundResource(R.color.secondary_purple)
            isCheckedV3=false
        }
    }
    fun setColorBackgroundV4() {
        if (!isCheckedV4) {
            binding.tuesdayTv.setBackgroundResource(R.drawable.text_view_background)
            isCheckedV4 = true
        } else {
            binding.tuesdayTv.setBackgroundResource(R.color.secondary_purple)
            isCheckedV4=false
        }
    }
    fun setColorBackgroundV5() {
        if (!isCheckedV5) {
            binding.wednesdayTv.setBackgroundResource(R.drawable.text_view_background)
            isCheckedV5 = true
        } else {
            binding.wednesdayTv.setBackgroundResource(R.color.secondary_purple)
            isCheckedV5=false
        }
    }
    fun setColorBackgroundV6() {
        if (!isCheckedV6) {
            binding.thursdayTv.setBackgroundResource(R.drawable.text_view_background)
            isCheckedV6 = true
        } else {
            binding.thursdayTv.setBackgroundResource(R.color.secondary_purple)
            isCheckedV6=false
        }
    }
    fun setColorBackgroundV7() {
        if (!isCheckedV7) {
            binding.fridayTv.setBackgroundResource(R.drawable.text_view_background)
            isCheckedV7 = true
        } else {
            binding.fridayTv.setBackgroundResource(R.color.secondary_purple)
            isCheckedV7=false
        }
    }

    public fun getSelectedDays(): ArrayList<String> {
        var days=ArrayList<String>()

        days.add("Saturday")
        days.add("Sunday")
        days.add("Monday")
        days.add("Tuesday")
        days.add("Wednesday")
        days.add("Thursday")
        days.add("Friday")

        if(isCheckedV1)days.remove("Saturday")
        if(isCheckedV2)days.remove("Sunday")
        if(isCheckedV3)days.remove("Monday")
        if(isCheckedV4)days.remove("Tuesday")
        if(isCheckedV5)days.remove("Wednesday")
        if(isCheckedV6)days.remove("Thursday")
        if(isCheckedV7)days.remove("Friday")

        println(days)

        return days
    }

    override fun onClick(v: View?) {
        when(v!!){
            binding.saturdayTv->{
                day="saturday"
                Log.d("day og weak  ", requireArguments().getString("day").toString())
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TabViewOneFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            TabViewOneFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
