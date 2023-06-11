package com.example.habittrackerapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.databinding.FragmentEditEmailBinding
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.example.habittrackerapplication.viewmodels.AddNewHabitFragmentViewModel
import com.example.habittrackerapplication.viewmodels.EditEmailFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditEmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditEmailFragment : Fragment() {

    lateinit var binding: FragmentEditEmailBinding

    val viewModel: EditEmailFragmentViewModel by lazy {
        ViewModelProvider(this, EditEmailFragmentViewModel.EditEmailFragmentViewModelFactory(
            FirebaseAuthRepo(FirebaseAuth.getInstance())
        )).get(
            EditEmailFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentEditEmailBinding.inflate(inflater,container,false)


        binding.saveBtn.setOnClickListener {
            var flag=true
            if(binding.emailTil.editText!!.text.isEmpty()){
                binding.emailTil.isErrorEnabled = true
                binding.emailTil.error = "required"
                flag=false
            }
            if(binding.passwordTil.editText!!.text.isEmpty()){
                binding.passwordTil.isErrorEnabled = true
                binding.passwordTil.error = "required"
                flag=false
            }
            if(flag)
                viewModel.editEmail(binding.emailTil.editText!!.text.toString(),binding.passwordTil.editText!!.text.toString())
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.editEmail.collect {
                when (it) {
                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            it.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("EditEmailFragment", "error ${it}")
                    }
                    is Resource.Loading -> {
                        Log.d("EditEmailFragment", "sss ${it}")
                    }
                    is Resource.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "Email Changed",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("EditEmailFragment", "success ${it.data}")
                    }
                }
            }
        }
        return binding.root
    }


}