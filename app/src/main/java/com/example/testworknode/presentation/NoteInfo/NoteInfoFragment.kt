package com.example.testworknode.presentation.NoteInfo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.testworknode.R
import com.example.testworknode.databinding.FragmentNoteInfoBinding
import com.example.testworknode.domain.Note
import com.example.testworknode.presentation.*
import com.example.testworknode.presentation.AddItem.AddViewModel
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NoteInfoFragment : Fragment() {

    private val args by navArgs<NoteInfoFragmentArgs>()

    private var _binding: FragmentNoteInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var uploadTask: UploadTask
    private lateinit var viewModel: AddViewModel
    private var imgUri: Uri? = null
    private var downloadUri: Uri? = null
    private var isEmp: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteInfoBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[AddViewModel::class.java]
        return binding.root
    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            binding.imgItem.setImageURI(it)

            try {
                imgUri = it!!
                resumeOldData()
            } catch (e: Exception) {
                Log.d("TAG", "NULL")
                onResume()
                resumeOldData()
//                findNavController().navigate(R.id.action_addFragment_to_mainFragment)
            }
        }
    )

    override fun onStart() {
        super.onStart()
        init()
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!isEdit) {
            when (item.itemId) {
                R.id.edit_btn -> {
                    binding.etitemName.isEnabled = true
                    binding.etitemCount.isEnabled = true
                    binding.etitemPrice.isEnabled = true
                    binding.btnSave.visibility = View.VISIBLE
                    binding.btnCancel.visibility = View.VISIBLE
                    isEdit = true
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun resumeOldData() {
        binding.etitemName.setText(save_Name)
        binding.etitemCount.setText(save_Count)
        binding.etitemPrice.setText(save_Price)
    }


    private fun init() {
        Log.d("TAG", isEdit.toString())
        if (args.note.imgUrl.isEmpty() || args.note.imgUrl == "null") {
            Picasso.get().load(R.drawable.img).into(binding.imgItem)
        } else {
            Picasso.get().load(args.note.imgUrl)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(binding.imgItem)
        }
        with(binding) {
            with(args.note) {
                etitemName.setText(name)
                etitemCount.setText(count.toString())
                etitemPrice.setText(price.toString())
                APP_ACTIVITY.mToolbar.title = "$name"
            }
            btnEdit.setOnClickListener {

                Log.d("TAG", isEdit.toString())
            }
            btnCancel.setOnClickListener {
                with(args.note) {
                    etitemName.setText(name)
                    etitemCount.setText(count.toString())
                    etitemPrice.setText(price.toString())
                }
                isEdit = false
                Log.d("TAG", isEdit.toString())
                etitemName.isEnabled = false
                etitemCount.isEnabled = false
                etitemPrice.isEnabled = false
                btnSave.visibility = View.INVISIBLE
                btnCancel.visibility = View.INVISIBLE
            }

            binding.imgItem.setOnClickListener {
                if (isEdit) {
                    uploadImg()
                }
            }

            btnSave.setOnClickListener {
                editItem()
                if (isEmp) {
                    findNavController().navigate(R.id.action_noteInfoFragment_to_mainFragment)
                }

            }
        }
    }

    private fun editItem() {

        val name = binding.etitemName.text.toString()
        val count = binding.etitemCount.text.toString()
        val price = binding.etitemPrice.text.toString()
        val oldId = args.note.idFirebase
        val oldImg = args.note.imgUrl
        lifecycleScope.launch(Dispatchers.Main) {
            if (name.isNotEmpty() && count.isNotEmpty() && price.isNotEmpty()) {
                if (name.length <= 15 && count.length <= 6 && price.length <= 10) {
                    viewModel.delPost(args.note) {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.progressText.visibility = View.VISIBLE
                        binding.imgItem.isEnabled = false
                        binding.btnCancel.isClickable = false
                        binding.btnSave.isClickable = false
                        binding.etitemName.isEnabled = false
                        binding.etitemCount.isEnabled = false
                        binding.etitemPrice.isEnabled = false
                        isEmp = true
                        Log.d("TAG", "Запись удаленна")
                    }
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
                                    if (oldImg != imgUri.toString()) {
                                        viewModel.addPost(
                                            Note(
                                                name,
                                                Integer.parseInt(count),
                                                Integer.parseInt(price),
                                                downloadUri.toString(),
                                                oldId
                                            )
                                        ) {
                                            findNavController().navigate(
                                                R.id.action_noteInfoFragment_to_mainFragment
                                            )
                                            Toast.makeText(
                                                APP_ACTIVITY,
                                                "Товар изменен",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }
                                    Log.d("TAG", "Ссылка на скачку ${downloadUri.toString()}")
                                } else {
                                    Log.d("TAG", "блое else")
                                }
                            }

                        }
                        isEmp = true
                    } else {
                        viewModel.addPost(
                            Note(
                                name,
                                Integer.parseInt(count),
                                Integer.parseInt(price),
                                oldImg,
                                oldId
                            )
                        )
                        {
                            findNavController().navigate(
                                R.id.action_noteInfoFragment_to_mainFragment
                            )
                            Toast.makeText(
                                APP_ACTIVITY,
                                "Товар изменен",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                        }
                        isEmp = true
                    }
                } else {
                    Toast.makeText(
                        APP_ACTIVITY,
                        "Значения слишком большие, повторите еще раз",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(APP_ACTIVITY, "Заполните все поля", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun saveOld() {
        save_Name = binding.etitemName.text.toString()
        save_Count = binding.etitemCount.text.toString()
        save_Price = binding.etitemPrice.text.toString()
        Log.d("TAG", "$save_Name $save_Count $save_Price")
    }

    private fun uploadImg() {
        saveOld()
        getImage.launch("image/*")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isEdit = false
        _binding = null
    }

    companion object {
        private var isEdit: Boolean = false
    }
}