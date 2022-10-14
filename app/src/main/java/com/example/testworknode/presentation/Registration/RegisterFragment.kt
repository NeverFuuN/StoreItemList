package com.example.testworknode.presentation.Registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.testworknode.R
import com.example.testworknode.databinding.FragmentRegisterBinding
import com.example.testworknode.presentation.EMAIL
import com.example.testworknode.presentation.PASSWORD

class RegisterFragment : Fragment() {
    private lateinit var viewModel: RegisterViewModel

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        init()

    }

    private fun init() {
        binding.button.setOnClickListener {
            var email = binding.etEmail.text.toString()
            var pass = binding.etPass.text.toString()
            var passRep = binding.etPassRep.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty() && passRep.isNotEmpty()) {
                if (pass == passRep) {
                    EMAIL = email
                    PASSWORD = pass
                    viewModel.registration {
                        Toast.makeText(
                            requireContext(),
                            "Пользователь зарегестрирован",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                } else {
                    Toast.makeText(requireContext(), "Пароли отличаются", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}