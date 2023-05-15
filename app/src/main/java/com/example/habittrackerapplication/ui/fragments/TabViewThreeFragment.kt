package com.example.habittrackerapplication.ui.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.databinding.FragmentTabViewThreeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TabViewThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TabViewThreeFragment : Fragment() {
    lateinit var binding: FragmentTabViewThreeBinding

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTabViewThreeBinding.inflate(layoutInflater,container,false)
        binding.daysNp.minValue=1
        binding.daysNp.maxValue=30

        requireArguments().putInt("day",1)

        binding.daysNp.setDividerColor(Color.TRANSPARENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.daysNp.textColor=Color.BLACK
        }
        binding.daysNp.setOnValueChangedListener { numberPicker, i, i2 ->
            requireArguments().putInt("day",binding.daysNp.value)

        }
        return binding.root
    }

    private fun NumberPicker.setDividerColor(color: Int) {
        val dividerField = NumberPicker::class.java.declaredFields.firstOrNull { it.name == "mSelectionDivider" } ?: return
        try {
            dividerField.isAccessible = true
            dividerField.set(this,color)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TabViewThreeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}