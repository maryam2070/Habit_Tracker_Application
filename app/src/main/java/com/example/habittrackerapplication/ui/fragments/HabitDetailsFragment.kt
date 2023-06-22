package com.example.habittrackerapplication.ui.fragments

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.common.getIconArray
import com.example.habittrackerapplication.databinding.FragmentHabitDetailsBinding
import com.prolificinteractive.materialcalendarview.CalendarDay

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HabitDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HabitDetailsFragment : Fragment() {

    lateinit var binding:FragmentHabitDetailsBinding
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentHabitDetailsBinding.inflate(inflater,container,false)
        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.calendarView.outlineAmbientShadowColor= Color.WHITE
                binding.calendarView.setDateTextAppearance(Color.WHITE)
                binding.calendarView.setWeekDayTextAppearance(Color.WHITE)
                binding.calendarView.setHeaderTextAppearance(Color.WHITE)
                binding.calendarView.leftArrow.setTint(Color.WHITE)
                binding.calendarView.rightArrow.setTint(Color.WHITE)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.calendarView.outlineAmbientShadowColor=Color.BLACK
                binding.calendarView.setDateTextAppearance(Color.BLACK)
                binding.calendarView.setWeekDayTextAppearance(Color.BLACK)
                binding.calendarView.setHeaderTextAppearance(Color.BLACK)
                binding.calendarView.leftArrow.setTint(Color.BLACK)
                binding.calendarView.rightArrow.setTint(Color.BLACK)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                binding.calendarView.outlineAmbientShadowColor=Color.BLACK
                binding.calendarView.setDateTextAppearance(Color.BLACK)
                binding.calendarView.setWeekDayTextAppearance(Color.BLACK)
                binding.calendarView.setHeaderTextAppearance(Color.BLACK)
                binding.calendarView.leftArrow.setTint(Color.BLACK)
                binding.calendarView.rightArrow.setTint(Color.BLACK)
            }
        }

        val bundle = arguments
        if (bundle == null) {
            Log.d("HabitDetailsFragment", "null argument")

        }else {
            val args = HabitDetailsFragmentArgs.fromBundle(bundle)
            binding.iconIv.setImageResource(getIconArray().get(args.habit!!.iconPosition))
            binding.nameTv.text = args.habit!!.name
            binding.totalDays.text = args.habit!!.days!!.toString()+" total days completed"
            for(i in args.habit!!.completedDays)
            {
                var a= (i.substring(i.lastIndexOf('-')+1))!!.toLong()//year
                var index=i.indexOf('-')
                var b= (i.substring(index+1,  i.indexOf('-',index+1)))!!.toLong() //month
                var c= (i.substring(0,i.indexOf('-')))!!.toLong()//day

                binding.calendarView
                    .setDateSelected(
                        CalendarDay.from(
                        a.toInt(),
                        b.toInt(),
                        c.toInt()),true)
            }
        }
        return binding.root
    }

}