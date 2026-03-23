package com.gsm.taskmaster.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gsm.taskmaster.R
import com.gsm.taskmaster.data.model.TodoItem
import com.gsm.taskmaster.databinding.ActivityMainBinding
import com.gsm.taskmaster.ui.adapter.TodoAdapter
import com.gsm.taskmaster.ui.base.UiState
import com.gsm.taskmaster.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

// https://makeappicon.com/ - To make app icon with all dpi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var todoAdapter: TodoAdapter
    private val todoViewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        binding.apply {
            // Setup RecyclerView
            rvTodos.layoutManager = LinearLayoutManager(this@MainActivity)
            rvTodos.setHasFixedSize(true)
            rvTodos.addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            )
            rvTodos.adapter = todoAdapter

            fabAddTodo.setOnClickListener {
                showDialogFragment(AppConstants.ACTION_ADD)
            }

            etSearch.addTextChangedListener {
                val query = it.toString()
                if (query.isNotEmpty()) {
                    try {
                        todoViewModel.filterTodosByUserId(query.toInt())
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@MainActivity, e.localizedMessage, Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    todoViewModel.getAllTodos()
                }
            }
        }

        todoAdapter.itemClickListener = { action, position, todoItem ->
            if (action == AppConstants.ACTION_EDIT) {
                // Edit operation
                showDialogFragment(AppConstants.ACTION_UPDATE, position, todoItem)
            } else {
                // Delete operation
                todoViewModel.deleteTodoItemById(todoItem.id)
                todoAdapter.notifyItemRemoved(position)
            }
        }
    }

    private fun showDialogFragment(
        action: String,
        position: Int? = null,
        todoItem: TodoItem? = null
    ) {
        val inputDialog = AddUpdateTodoDialogFragment.newInstance(action, position, todoItem)
        inputDialog.show(supportFragmentManager, "AddUpdateTodoDialogTag")

        inputDialog.itemClickListener = { action, position, todoItem ->
            if (action == AppConstants.ACTION_UPDATE) {
                todoViewModel.updateTodoItemById(todoItem)
            } else {
                todoViewModel.addNewTodoItem(todoItem)
                todoAdapter.notifyItemInserted(position)
            }
            inputDialog.dismiss()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                todoViewModel.todos.collect {
                    when (it) {
                        is UiState.Error -> {
                            Toast.makeText(
                                this@MainActivity, it.errorMessage, Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBar.visibility = View.GONE
                        }

                        UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val todos = it.data
                            todoAdapter.submitList(ArrayList(todos))
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                todoViewModel.toastMessage.collect {
                    when (it) {
                        is UiState.Error -> {
                            Toast.makeText(this@MainActivity, it.errorMessage, Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBar.visibility = View.GONE
                        }

                        UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@MainActivity, it.data, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}