package com.gsm.taskmaster.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.gsm.taskmaster.R
import com.gsm.taskmaster.data.model.TodoItem
import com.gsm.taskmaster.databinding.InputDialogBinding
import com.gsm.taskmaster.ui.base.ItemClickListener
import com.gsm.taskmaster.utils.AppConstants

class AddUpdateTodoDialogFragment : DialogFragment() {

    private var _binding: InputDialogBinding? = null
    private val binding get() = _binding!!
    lateinit var itemClickListener: ItemClickListener<TodoItem>

    companion object {
        fun newInstance(
            action: String,
            position: Int? = null,
            todoItem: TodoItem? = null
        ): AddUpdateTodoDialogFragment {
            val args = Bundle().apply {
                putString(AppConstants.ACTION, action)
                position?.let { putInt(AppConstants.ADAPTER_POSITION, it) }
                putParcelable(AppConstants.TODO_ITEM, todoItem)
            }
            val fragment = AddUpdateTodoDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = InputDialogBinding.inflate(layoutInflater)
        setupUI()

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        return builder.create()
    }

    private fun setupUI() {
        val action = arguments?.getString(AppConstants.ACTION)
        val todoItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(AppConstants.TODO_ITEM, TodoItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(AppConstants.TODO_ITEM)
        }
        val position = arguments?.getInt(AppConstants.ADAPTER_POSITION)

        binding.apply {
            tvTitle.text = if (action == AppConstants.ACTION_ADD)
                getString(R.string.add_task)
            else
                getString(R.string.update_task)

            if (action == AppConstants.ACTION_UPDATE) {
                etNewTask.setText(todoItem?.title ?: "")
            } else {
                etNewTask.hint = getString(R.string.add_new_task)
            }

            ivClose.setOnClickListener {
                dismiss()
            }

            btnAddUpdate.text = if (action == AppConstants.ACTION_ADD)
                getString(R.string.add)
            else
                getString(R.string.update)

            btnAddUpdate.setOnClickListener {
                // Handle add/update logic
                if (action == AppConstants.ACTION_ADD) {
                    val todoItem = TodoItem(
                        completed = false,
                        id = 0,
                        title = etNewTask.text.toString(),
                        userId = 0
                    )
                    itemClickListener.invoke(action, 0, todoItem)
                } else {
                    val todoItem = TodoItem(
                        completed = false,
                        id = todoItem?.id ?: 0,
                        title = etNewTask.text.toString(),
                        userId = todoItem?.userId ?: 0
                    )
                    if (action != null) {
                        if (position != null) {
                            itemClickListener.invoke(action, position, todoItem)
                        } else {
                            itemClickListener.invoke(action, 0, todoItem)
                        }
                    } else {
                        itemClickListener.invoke(action ?: "", 0, todoItem)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}