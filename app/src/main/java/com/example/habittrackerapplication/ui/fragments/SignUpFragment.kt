package com.example.habittrackerapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.databinding.FragmentSignUpBinding
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.example.habittrackerapplication.repositories.FirebaseRealTimeDatabaseRepo
import com.example.habittrackerapplication.viewmodels.SignUpFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {

    lateinit var binding:FragmentSignUpBinding
    val auhtRepo=FirebaseAuthRepo(FirebaseAuth.getInstance())
    val dbRepo=FirebaseRealTimeDatabaseRepo(FirebaseDatabase.getInstance())
    val viewModel: SignUpFragmentViewModel by lazy {
        ViewModelProvider(this, SignUpFragmentViewModel.SignUpFragmentViewModelFactory(auhtRepo,dbRepo)).get(
            SignUpFragmentViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSignUpBinding.inflate(layoutInflater,container,false)


        binding.signUpBtn.setOnClickListener {
                validateInputData()
            }

        binding.googleBtn.setOnClickListener {

        }

        return binding.root
    }

    private fun validateInputData()
    {
        var flag=true

        if(binding.firstNameTil.editText!!.text.isEmpty()){
            binding.firstNameTil.setError("required")
            flag=false
        }
        if(binding.secondNameTil.editText!!.text.isEmpty()){
            binding.secondNameTil.setError("required")
            flag=false
        }
        if(binding.emailTil.editText!!.text.isEmpty()) {
            binding.emailTil.setError("required")
            flag=false
        }
        if(binding.passwordTil.editText!!.text.isEmpty()) {
            binding.passwordTil.setError("required")
            flag = false
        }
        if(binding.confirmPasswordTil.editText!!.text.isEmpty()) {
            binding.confirmPasswordTil.setError("required")
            flag = false
        }
        if(!binding.confirmPasswordTil.editText!!.text.toString().equals(binding.passwordTil.editText!!.text.toString())) {
            binding.confirmPasswordTil.setError("not matched")
            flag = false
        }
        if(flag) {
            binding.confirmPasswordTil.setError(null)
            binding.passwordTil.setError(null)
            binding.emailTil.setError(null)
            binding.firstNameTil.setError(null)
            binding.secondNameTil.setError(null)

            validateEmail(binding.emailTil.editText!!.text.toString(),binding.passwordTil.editText!!.text.toString())
            //validateEmail()
        }
    }

    private fun validateEmail(email: String, pass: String) {
        viewModel.getAllEmails()
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.emails.collect {
                when(it) {
                    is Resource.Error ->
                        Log.d("SignUpFragment", "error ${it}")
                    is Resource.Loading ->
                        Log.d("SignUpFragment", "sss ${it}")
                    is Resource.Success -> {
                        if(it.data!!.contains(email)) {
                            launch(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "this Email is already registered",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }else
                            registerUser(email,pass)
                        Log.d("SignUpFragment", "success ${it.data}")
                    }
                }
            }
        }
    }

    private suspend fun registerUser(email: String, pass: String) {
        viewModel.registerUser(
            email,
            pass
        )

        viewModel.user.collect {
            when(it) {
                is Resource.Error ->
                    Log.d("SignUpFragment", "error ${it}")
                is Resource.Loading ->
                    Log.d("SignUpFragment", "sss ${it}")
                is Resource.Success -> {
                    /////////
                    //launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "DONE", Toast.LENGTH_SHORT).show()
                    //}
                    Log.d("SignUpFragment", "success ${it.data}")
                }
            }
        }
    }


}