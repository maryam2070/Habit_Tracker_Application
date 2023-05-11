package com.example.habittrackerapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.databinding.FragmentSplashBinding
import com.example.habittraker.adapters.ViewPagerAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SplashFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashFragment : Fragment() {


    lateinit var binding:FragmentSplashBinding
    var handler= Handler()
    var size=0
    var list= intArrayOf(R.layout.slide_1, R.layout.slide_2, R.layout.slide_1, R.layout.slide_2)
    lateinit var adapter: ViewPagerAdapter
    var delay = 3000


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding=FragmentSplashBinding.inflate(inflater,container,false)


        binding.signUpBtn.setOnClickListener {
           // startActivity(Intent(this, SignUpActivity::class.java))
            findNavController().navigate(R.id.action_splashFragment_to_signUpFragment)
        }


        binding.emailTv.setOnClickListener {
           // startActivity(Intent(this, LoginActivity::class.java))
        }
        adapter= ViewPagerAdapter(list,requireContext())
        binding.welcomePager.adapter=adapter




        binding.welcomePager.setOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                size=position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        return binding.root
    }


    val runnable= object : Runnable {
        override fun run() {
            Log.d("size ","Page Size = $size")
            if(adapter.count==size) {
                size = 1
                list.reverse()
                adapter= ViewPagerAdapter(list,requireContext())
                delay =500
                binding.welcomePager.adapter=adapter

            }else {
                size = size + 1
                delay =1000
            }
            binding.welcomePager.setCurrentItem(size,true)


            handler.postDelayed(this, delay.toLong())
        }
    }
    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, delay.toLong())
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

}