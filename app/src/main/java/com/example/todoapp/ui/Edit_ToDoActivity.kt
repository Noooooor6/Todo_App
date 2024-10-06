package com.example.todoapp.ui

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.database.model.Task
import com.example.todoapp.databinding.ActivityEditTodoBinding
import com.example.todoapp.utils.Constants
import com.example.todoapp.utils.clearTime

class Edit_ToDoActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditTodoBinding
    lateinit var intentTask: Task
    lateinit var newTask: Task
    private var dateCalendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intentTask =
            IntentCompat.getSerializableExtra(intent, Constants.TASK_KEY, Task::class.java) as Task
        initViews()
        setUpToolbar()
        initNewTask()
        onSelectDateTV()
        onSaveClick()
    }

    private fun onSaveClick() {
        binding.saveBtn.setOnClickListener {
            updateTask()
        }
    }


    private fun isValid(): Boolean {
        var isValid = true
        if (binding.titleTextInput.editText?.text.isNullOrEmpty()) {
            isValid = false
            binding.titleTextInput.error = "please enter a title"
        } else {
            binding.titleTextInput.error = null
        }
        return isValid
    }

    private fun updateTask() {
        if (!isValid()) {
            return
        } else
            newTask.apply {
                title = binding.titleTextInput.editText?.text.toString()
                description = binding.descriptionTextInput.editText?.text.toString()

            }
        AppDatabase.createDatabase(this).tasksDao().UpdateTask(newTask)
        finish()
    }


    private fun onSelectDateTV() {
        binding.dateTv.setOnClickListener {
            val dialog = DatePickerDialog(this)
            dialog.setOnDateSetListener { datePicker, year, month, day ->
                binding.dateTv.text = "${day}/${month + 1}/${year}"
                dateCalendar.clearTime()
                dateCalendar.set(Calendar.YEAR, year)
                dateCalendar.set(Calendar.MONTH, month)
                dateCalendar.set(Calendar.DAY_OF_MONTH, day)
                newTask.date = dateCalendar.timeInMillis
            }
            dialog.show()
        }
    }

    private fun initNewTask() {
        newTask = intentTask.copy()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initViews() {
        binding.titleTextInput.editText?.setText(intentTask.title)
        binding.descriptionTextInput.editText?.setText(intentTask.description)
        val dateCalender = Calendar.getInstance()
        dateCalender.timeInMillis = intentTask.date
        val year = dateCalender.get(Calendar.YEAR)
        val month = dateCalender.get(Calendar.MONTH)
        val day = dateCalender.get(Calendar.DAY_OF_MONTH)
        binding.dateTv.text = "${day}/${month + 1}/${year}"
    }
}