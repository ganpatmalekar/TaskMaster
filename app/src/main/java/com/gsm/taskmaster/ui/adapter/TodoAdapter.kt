package com.gsm.taskmaster.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gsm.taskmaster.data.model.TodoItem
import com.gsm.taskmaster.databinding.ItemTodoBinding
import com.gsm.taskmaster.ui.base.ItemClickListener
import com.gsm.taskmaster.utils.AppConstants

class TodoAdapter : ListAdapter<TodoItem, TodoAdapter.TodoViewHolder>(DIFF_CALLBACK) {

    lateinit var itemClickListener: ItemClickListener<TodoItem>

    override fun onCreateViewHolder(
        parent: ViewGroup,
        p1: Int
    ): TodoViewHolder {
        return TodoViewHolder(
            ItemTodoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: TodoViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), itemClickListener)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TodoItem>() {
            override fun areItemsTheSame(
                old: TodoItem,
                new: TodoItem
            ): Boolean = old.id == new.id

            override fun areContentsTheSame(
                old: TodoItem,
                new: TodoItem
            ): Boolean = old == new
        }
    }

    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todoItem: TodoItem, itemClickListener: ItemClickListener<TodoItem>) {
            binding.apply {
                tvTaskName.text = todoItem.title
                ivEdit.setOnClickListener {
                    itemClickListener.invoke(
                        AppConstants.ACTION_EDIT,
                        bindingAdapterPosition,
                        todoItem
                    )
                }
                ivDelete.setOnClickListener {
                    itemClickListener.invoke(
                        AppConstants.ACTION_DELETE,
                        bindingAdapterPosition,
                        todoItem
                    )
                }
                if (todoItem.completed)
                    ivCompletedTask.visibility = View.VISIBLE
                else
                    ivCompletedTask.visibility = View.GONE
            }
        }
    }
}