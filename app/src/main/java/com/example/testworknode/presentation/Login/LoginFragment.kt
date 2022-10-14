package com.example.testworknode.presentation.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.testworknode.R
import com.example.testworknode.databinding.FragmentLoginBinding
import com.example.testworknode.presentation.APP_ACTIVITY
import com.example.testworknode.presentation.EMAIL
import com.example.testworknode.presentation.MainActivity
import com.example.testworknode.presentation.PASSWORD
import com.example.testworknode.utils.AppPreference


class LoginFragment : Fragment() {


    private lateinit var viewModel: LoginViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        }

    override fun onStart() {
        super.onStart()
        init()
        APP_ACTIVITY.mToolbar.visibility = View.GONE
    }

    private fun init() {

        if (AppPreference.getInitUser()) {
            viewModel.initLogin {
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                Toast.makeText(APP_ACTIVITY, "Вход произведен ", Toast.LENGTH_SHORT).show()
            }

        } else {
            binding.button.setOnClickListener {
                var email = binding.etEmail.text.toString()
                var pass = binding.etPass.text.toString()
                if (email.isNotEmpty() && pass.isNotEmpty()) {
                    EMAIL = email
                    PASSWORD = pass
                    viewModel.initLogin {
                        AppPreference.setInitUser(true)
                        binding.etPass.setText("")
                        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    }
                }
            }
            binding.frgPass.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_foreignFragment)
            }
            binding.registerTex.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}