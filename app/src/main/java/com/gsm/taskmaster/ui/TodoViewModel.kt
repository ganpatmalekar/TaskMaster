package com.gsm.taskmaster.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gsm.taskmaster.data.TodoRepository
import com.gsm.taskmaster.data.model.TodoItem
import com.gsm.taskmaster.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {

    private var allTodos: ArrayList<TodoItem> = arrayListOf()
    private val _todos: MutableStateFlow<UiState<List<TodoItem>>> =
        MutableStateFlow(UiState.Loading)
    val todos: StateFlow<UiState<List<TodoItem>>> = _todos

    private val _toastMessage = MutableSharedFlow<UiState<String>>()
    val toastMessage: SharedFlow<UiState<String>> = _toastMessage.asSharedFlow()
    private val _searchQuery = MutableStateFlow(0)

    init {
        getAllTodos()
        handleSearch()
    }

    fun getAllTodos() {
        viewModelScope.launch {
            repository.getAllTodos()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _todos.value = UiState.Error(e.localizedMessage ?: "Unknown Error")
                }
                .collect {
                    allTodos = ArrayList(it)
                    _todos.value = UiState.Success(allTodos)
                }
        }
    }

    fun addNewTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            _toastMessage.emit(UiState.Loading)
            repository.addNewTodoItem(todoItem)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _todos.value = UiState.Error(e.localizedMessage ?: "Unknown Error")
                }
                .collect {
                    allTodos.add(0, it)
                    _todos.value = UiState.Success(allTodos)
                    _toastMessage.emit(UiState.Success("Task added successfully"))
                }
        }
    }

    fun updateTodoItemById(todoItem: TodoItem) {
        viewModelScope.launch {
            _toastMessage.emit(UiState.Loading)
            repository.updateTodoItemById(todoItem)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _toastMessage.emit(UiState.Error(e.localizedMessage ?: "Unknown Error"))
                }
                .collect {
                    // 1. Create a NEW list instance based on the current one
                    val updatedList = ArrayList(allTodos)
                    val index = updatedList.indexOfFirst { updatedTodo ->
                        updatedTodo.id == todoItem.id
                    }

                    if (index != -1) {
                        // 2. Modify the new list
                        updatedList[index] = todoItem
                        allTodos = updatedList

                        // 3. This will now trigger StateFlow because the list content
                        // is different from the previous state
                        _todos.value = UiState.Success(allTodos)
                    }
                    _toastMessage.emit(UiState.Success(it))
                }
        }
    }

    fun deleteTodoItemById(id: Int) {
        viewModelScope.launch {
            _toastMessage.emit(UiState.Loading)
            repository.deleteTodoItemById(id)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _toastMessage.emit(UiState.Error(e.localizedMessage ?: "Unknown Error"))
                }
                .collect {
                    _toastMessage.emit(UiState.Success(it))
                }
        }
    }

    fun filterTodosByUserId(userId: Int) {
        _searchQuery.value = userId
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun handleSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(400)
                .filter { it > 0 }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    _todos.value = UiState.Loading
                    repository.filterTodosByUserId(query)
                        .catch { e ->
                            _todos.value = UiState.Error(e.localizedMessage ?: "Unknown Error")
                        }
                }.flowOn(Dispatchers.IO)
                .collect { todos ->
                    _todos.value = UiState.Success(todos)
                }
        }
    }
}