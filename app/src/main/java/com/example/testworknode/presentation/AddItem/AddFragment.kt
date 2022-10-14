package com.example.testworknode.presentation.AddItem

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.testworknode.R
import com.example.testworknode.databinding.FragmentAddBinding
import com.example.testworknode.domain.Note
import com.example.testworknode.presentation.APP_ACTIVITY
import com.example.testworknode.presentation.STOR_DATABASE
import com.google.firebase.storage.UploadTask


class AddFragment : Fragment() {


    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AddViewModel
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var uploadTask: UploadTask
    private var imgUri: Uri? = null
    private var downloadUri: Uri? = null
    private var isEmp: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[AddViewModel::class.java]
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        init()
    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            binding.etimgItem.visibility = View.INVISIBLE
            binding.etImgView.setImageURI(it)
            try {
                imgUri = it!!
            } catch (e: Exception) {
                Log.d("TAG", "NULL")
                onStart()
//                findNavController().navigate(R.id.action_addFragment_to_mainFragment)
                binding.etImgView.visibility = View.INVISIBLE
                binding.etimgItem.visibility = View.VISIBLE
            }
            binding.etImgView.visibility = View.VISIBLE
        }
    )

    private fun init() {
        binding.etimgItem.setOnClickListener {
            uploadImg()
        }
        binding.etImgView.setOnClickListener {
            uploadImg()
        }
        binding.btnAdd.setOnClickListener {
            addToFB()
            if (isEmp) findNavController().navigate(R.id.action_addFragment_to_mainFragment)

        }
    }

    private fun addToFB(){
        val name = binding.etitemName.text.toString()
        val count = binding.etitemCount.text.toString()
        val price = binding.etitemPrice.text.toString()
        if (name.isNotEmpty() && count.isNotEmpty() && price.isNotEmpty()) {
            if (name.length <= 15 && count.length <= 6 && price.length <= 10) {
                if (imgUri != null) {
                    val imgRef = STOR_DATABASE.child("images/${imgUri?.lastPathSegment}")
                    Log.d("TAG", imgUri?.lastPathSegment.toString())
                    uploadTask = imgRef.putFile(imgUri!!)
                    uploadTask.addOnSuccessListener {
                        val urlTask = uploadTask.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            imgRef.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                downloadUri = task.result
                                addToFB(name, count, price)
                                Log.d("TAG", "Ссылка на скачку ${downloadUri.toString()}")
                            } else {
                                Log.d("TAG", task.exception.toString())
                            }
                        }
                    }
                    isEmp = true
                } else if (name.isNotEmpty()){
                    addToFB(name, count, price)
                    isEmp = true
                }
            }else {
                Toast.makeText(
                    APP_ACTIVITY,
                    "Значения слишком большие, повторите еще раз",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }else {
            Toast.makeText(APP_ACTIVITY, "Заполните все поля", Toast.LENGTH_SHORT)
                .show()
        }


    }

    private fun addToFB(name: String, count: String, price: String) {
//        if (name.isNotEmpty() && count.isNotEmpty() && price.isNotEmpty()) {
//            if (name.length <= 15 && count.length <= 6 && price.length <= 10) {
                viewModel.addPost(
                    Note(
                        name,
                        Integer.parseInt(count),
                        Integer.parseInt(price),
                        downloadUri.toString()
                    )
                )
                {
                    Toast.makeText(
                        APP_ACTIVITY,
                        "Товар добавлен",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }

//            } else {
//                Toast.makeText(
//                    APP_ACTIVITY,
//                    "Значения слишком большие, повторите еще раз",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        } else {
//            Toast.makeText(APP_ACTIVITY, "Заполните все поля", Toast.LENGTH_SHORT)
//                .show()
//        }
    }


    private fun uploadImg() = getImage.launch("image/*")


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
