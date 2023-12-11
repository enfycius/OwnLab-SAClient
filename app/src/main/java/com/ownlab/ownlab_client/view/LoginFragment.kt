package com.ownlab.ownlab_client.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ownlab.ownlab_client.R
import com.ownlab.ownlab_client.databinding.FragmentLoginBinding
import com.ownlab.ownlab_client.models.Auth
import com.ownlab.ownlab_client.utils.ApiResponse

import com.ownlab.ownlab_client.viewmodels.LoginViewModel
import com.ownlab.ownlab_client.viewmodels.TokenViewModel
import com.ownlab.ownlab_client.viewmodels.`interface`.CoroutinesErrorHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()
    private val tokenViewModel: TokenViewModel by activityViewModels()

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        tokenViewModel.token.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(activity, "Correct", Toast.LENGTH_LONG).show()
                navController.navigate(R.id.login_2_main)
            } else {
                Toast.makeText(activity, "Wrong", Toast.LENGTH_LONG).show()
            }
        }

        loginViewModel.loginResponse.observe(viewLifecycleOwner) {
            when(it) {
                is ApiResponse.Success -> {
                    tokenViewModel.save(it.data.token)
                }
                is ApiResponse.Failure -> {
                    Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.loginBtn.setOnClickListener {
            val id: String = binding.idField.text.toString()
            val password: String = binding.passwordField.text.toString()

            loginViewModel.login(Auth(id, password), object: CoroutinesErrorHandler {
                override fun onError(m : String) {
                    Toast.makeText(activity, "Error $m", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}