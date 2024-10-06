package com.example.todoapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.database.model.Task

@Dao
interface TasksDao {

    @Insert
    fun CreatTask(task: Task)

    @Delete
    fun DeleteTask(task: Task)

    @Update
    fun UpdateTask(task: Task)

    @Query("Select * from Task")
    fun getAllTasks(): List<Task>

    @Query("select * from task where date = :date")
    fun getTasksByDate(date: Long): List<Task>

}