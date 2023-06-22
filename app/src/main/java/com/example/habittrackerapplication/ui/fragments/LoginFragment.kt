package com.example.habittrackerapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.common.CLINT_SERVER_ID
import com.example.habittrackerapplication.common.Resource
import com.example.habittrackerapplication.databinding.FragmentLoginBinding
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.example.habittrackerapplication.viewmodels.LoginFragmentViewModel
import com.example.habittrackerapplication.viewmodels.SignUpFragmentViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
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
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    lateinit var binding:FragmentLoginBinding
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123


    val viewModel: LoginFragmentViewModel by lazy {
        ViewModelProvider(this, LoginFragmentViewModel.LoginFragmentViewModelFactory(
            requireContext(),
            FirebaseAuthRepo(FirebaseAuth.getInstance())
        )).get(
            LoginFragmentViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentLoginBinding.inflate(inflater,container,false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CLINT_SERVER_ID)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        binding.googleBtn.setOnClickListener {
            signInGoogle()
        }

        var flag=true
        binding.loginBtn.setOnClickListener {

            if (binding.emailTil.editText!!.text.isEmpty()) {
                binding.emailTil.setError("Enter Email")
                flag = false
            }
            if (binding.passwordTil.editText!!.text.isEmpty()) {
                binding.passwordTil.setError("Enter Password")
                flag = false
            }

            if (flag) {

                binding.passwordTil.setError(null)
                binding.emailTil.setError(null)

                viewModel.loginWithEmailAndPassword(
                    binding.emailTil.editText!!.text.toString(),
                    binding.passwordTil.editText!!.text.toString()
                )
            }
        }


        CoroutineScope(Dispatchers.IO).launch {
            viewModel.user.collect {
                when (it) {
                    is Resource.Error ->
                        Log.d("LoginFragment", "error ${it}")
                    is Resource.Loading ->
                        Log.d("LoginFragment", "sss ${it}")
                    is Resource.Success -> {
                        launch(Dispatchers.Main) {
                            findNavController().navigate(LoginFragmentDirections.actionLoginToCoreNav())
                            Toast.makeText(requireContext(), "DONE", Toast.LENGTH_SHORT).show()
                        }
                        Log.d("LoginFragment", "success ${it.data}")
                    }
                }
            }
        }
        return binding.root
    }

    private fun signInGoogle() {

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                if (task.getResult(ApiException::class.java) .account!= null) {
                    viewModel.loginWithGoogle(task.getResult(ApiException::class.java))
                }else{
                    Toast.makeText(requireContext(),"The account being chosen not found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException){
                Log.d("LoginFragment", "error ${e.toString()}")
                Toast.makeText(requireContext(),e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}