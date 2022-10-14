package com.example.testworknode.presentation.Foreign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.testworknode.R
import com.example.testworknode.databinding.FragmentForeignBinding
import com.example.testworknode.presentation.EMAIL


class ForeignFragment : Fragment() {
    private lateinit var viewModel: ForeignViewModel

    private var _binding: FragmentForeignBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForeignBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ForeignViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        init()

    }

    private fun init() {
        binding.button.setOnClickListener {
            var email = binding.etEmail.text.toString()
            if (email.isNotEmpty()) {
                EMAIL = email
                viewModel.foreing {
                    Toast.makeText(requireContext(), "Письмо отправленно", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.action_foreignFragment_to_loginFragment)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}