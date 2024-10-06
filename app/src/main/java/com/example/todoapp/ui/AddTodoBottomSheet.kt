package com.example.todoapp.ui

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.database.model.Task
import com.example.todoapp.databinding.FragmentAddTodoBottomSheetBinding
import com.example.todoapp.utils.clearTime
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddTodoBottomSheet(var addOnClick: () -> Unit) : BottomSheetDialogFragment() {
    lateinit var binding: FragmentAddTodoBottomSheetBinding
    var selectDay = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTodoBottomSheetBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatePickerDialog()
        updateDateTextView()
        binding.AddBtn.setOnClickListener {
            isValid()
            initListeners()

        }
    }

    private fun initListeners() {
        binding.AddBtn.setOnClickListener {
            if (!isValid()) return@setOnClickListener
            selectDay.clearTime()
            val title = binding.titleTextInput.editText!!.text.toString()
            val description = binding.descriptionTextInput.editText!!.text.toString()
            val newTask = Task(
                title = title,
                description = description,
                date = selectDay.timeInMillis,
                isDone = false
            )
            AppDatabase.createDatabase(requireContext()).tasksDao().CreatTask(newTask)
            addOnClick()
            dismiss()
        }

    }

    private fun initDatePickerDialog() {
        binding.dateTv.setOnClickListener {
            val dialog: DatePickerDialog = DatePickerDialog(
                requireContext(),
                { p0, year, month, day ->
                    selectDay.set(Calendar.YEAR, year)
                    selectDay.set(Calendar.MONTH, month)
                    selectDay.set(Calendar.DAY_OF_MONTH, day)
                    updateDateTextView()
                },
                selectDay.get(Calendar.YEAR),
                selectDay.get(Calendar.MONTH),
                selectDay.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }
    }

    private fun updateDateTextView() {
        val year = selectDay.get(Calendar.YEAR)
        val month = selectDay.get(Calendar.MONTH)
        val day = selectDay.get(Calendar.DAY_OF_MONTH)
        binding.dateTv.text = "${day} /${month + 1} / ${year}"
    }

    private fun isValid(): Boolean {
        val title = binding.titleTextInput.editText!!.text
        val description = binding.descriptionTextInput.editText!!.text
        var isvalid = true
        if (title.isNullOrEmpty()) {
            binding.titleTextInput.error = "plase enter valid title"
            isvalid = false
        } else {
            binding.titleTextInput.error = null
        }
        if (description.isNullOrEmpty()) {
            binding.descriptionTextInput.error = "plase enter valid description"
            isvalid = false
        } else {
            binding.descriptionTextInput.error = null
        }
        return isvalid
    }

}