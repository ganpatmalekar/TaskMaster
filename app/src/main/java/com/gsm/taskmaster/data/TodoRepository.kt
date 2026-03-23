package com.gsm.taskmaster.data

import com.gsm.taskmaster.data.model.TodoItem
import com.gsm.taskmaster.network.NetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepository @Inject constructor(private val networkService: NetworkService) {

    fun getAllTodos(): Flow<List<TodoItem>> {
        return flow {
            emit(networkService.getAllTodos())
        }.map {
            it
        }
    }

    fun addNewTodoItem(todoItem: TodoItem): Flow<TodoItem> {
        return flow {
            emit(networkService.addNewTodoItem(todoItem))
        }.map {
            it
        }
    }

    fun updateTodoItemById(todoItem: TodoItem): Flow<String> {
        return flow {
            emit(networkService.updateTodoItemById(todoItem.id, todoItem))
        }.map {
            "Task updated successfully"
        }
    }

    fun deleteTodoItemById(id: Int): Flow<String> {
        return flow {
            emit(networkService.deleteTodoItemById(id))
        }.map {
            "Task deleted successfully"
        }
    }

    fun filterTodosByUserId(userId: Int): Flow<List<TodoItem>> {
        return flow {
            emit(networkService.filterTodoByUserId(userId))
        }.map {
            it
        }
    }
}