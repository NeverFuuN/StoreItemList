package com.example.testworknode.presentation.Main

import android.graphics.Canvas
import android.graphics.Color.red
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testworknode.R
import com.example.testworknode.databinding.FragmentMainBinding
import com.example.testworknode.domain.Note
import com.example.testworknode.presentation.APP_ACTIVITY
import com.example.testworknode.presentation.AddItem.AddViewModel
import com.example.testworknode.utils.AppPreference
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainFragment : Fragment() {


    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var viewModelNote: AddViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModelNote = ViewModelProvider(this)[AddViewModel::class.java]
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    System.exit(0);
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
        )
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.exit_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btn_exit) {
            viewModel.singOut()
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            Log.d("TAG", "Вышли")
            AppPreference.setInitUser(false)
            return true
        } else return super.onOptionsItemSelected(item)

    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mToolbar.visibility = View.VISIBLE
        setHasOptionsMenu(true)
        init()
        swipeToDelete()

    }

    private fun swipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.currentList[position]
                viewModelNote.delPost(item) {
                    Log.d("TAG", "Удален")
                }
                Snackbar.make(
                    binding.rvMain, "Товар ${item.name} удален",
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setAction("Отменить") {
                        viewModelNote.addPost(item) {
                        }
                    }
                    show()
                }

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addBackgroundColor(ContextCompat.getColor(APP_ACTIVITY,R.color.red))
                    .addActionIcon(R.drawable.ic_delete)
                    .addSwipeLeftLabel("Удалить")
                    .setSwipeLeftLabelColor(ContextCompat.getColor(APP_ACTIVITY,R.color.white))
                    .create()
                    .decorate()
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }).attachToRecyclerView(binding.rvMain)


    }

    private fun init() {
        adapter = NoteAdapter()
        binding.rvMain.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMain.adapter = adapter
        adapter.onItemsClicListener = object : NoteAdapter.OnItemsClickListener {
            override fun onItemsClick(item: Note) {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToNoteInfoFragment(
                        item
                    )
                )
                Log.d("TAG", "Передаем ${item}")
            }
        }
        lifecycleScope.launch(Dispatchers.Main){
            viewModel.allPost.observe(viewLifecycleOwner) {
                var list = it.asReversed()
                adapter.submitList(list)
                if (list.isNotEmpty()) {
                    binding.rvMain.smoothScrollToPosition(0);
                }
            }
        }
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}