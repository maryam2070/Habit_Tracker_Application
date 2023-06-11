package com.example.habittrackerapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.databinding.FragmentEditPasswordBinding
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.example.habittrackerapplication.viewmodels.EditPasswordFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [EditPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditPasswordFragment : Fragment() {

    lateinit var binding: FragmentEditPasswordBinding

    val viewModel: EditPasswordFragmentViewModel by lazy {
        ViewModelProvider(
            this, EditPasswordFragmentViewModel.EditPasswordFragmentViewModelFactory(
                FirebaseAuthRepo(FirebaseAuth.getInstance())
            )
        ).get(
            EditPasswordFragmentViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentEditPasswordBinding.inflate(inflater, container, false)


        binding.saveBtn.setOnClickListener {

            var flag=true

            if(binding.curPassTil.editText!!.text.isEmpty()) {
                binding.curPassTil.setError("required")
                flag=false
            }
            if(binding.newPassTil.editText!!.text.isEmpty()) {
                binding.newPassTil.setError("required")
                flag = false
            }
            if(binding.confirmPassTil.editText!!.text.isEmpty()) {
                binding.confirmPassTil.setError("required")
                flag = false
            }
            if(!binding.confirmPassTil.editText!!.text.toString().equals(binding.newPassTil.editText!!.text.toString())) {
                binding.confirmPassTil.setError("not matched")

                flag = false
            }
            if(flag)
                viewModel.editPassword(binding.curPassTil.editText!!.text.toString().trim(),binding.newPassTil.editText!!.text.toString().trim())
        }
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.editPass.collect {
                when (it) {
                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            it.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("EditPasswordFragment", "error ${it}")
                    }
                    is Resource.Loading -> {
                        Log.d("EditPasswordFragment", "sss ${it}")
                    }
                    is Resource.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "Password Changed",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("EditPasswordFragment", "success ${it.data}")
                    }
                }
            }
        }

        return binding.root
    }


}