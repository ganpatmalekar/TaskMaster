package com.gsm.taskmaster.network

import com.gsm.taskmaster.data.model.TodoItem
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {

    @GET("todos")
    suspend fun getAllTodos(): List<TodoItem>

    @POST("todos")
    suspend fun addNewTodoItem(@Body todoItem: TodoItem): TodoItem

    @PUT("todos/{id}")
    suspend fun updateTodoItemById(@Path("id") id: Int, @Body todoItem: TodoItem)

    @DELETE("todos/{id}")
    suspend fun deleteTodoItemById(@Path("id") id: Int?)

    @GET("todos")
    suspend fun filterTodoByUserId(@Query("userId") userId: Int): List<TodoItem>
}