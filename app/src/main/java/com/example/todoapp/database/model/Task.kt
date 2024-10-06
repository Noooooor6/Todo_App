package com.example.todoapp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int = 0,
    @ColumnInfo
    var title: String,
    @ColumnInfo
    var date: Long,
    @ColumnInfo
    var description: String,
    @ColumnInfo
    var isDone: Boolean = false
) : Serializable


