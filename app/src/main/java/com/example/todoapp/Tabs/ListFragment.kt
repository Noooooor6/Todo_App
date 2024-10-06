package com.example.todoapp.Tabs

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todoapp.TodosAdapter
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.database.model.Task
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.ui.Edit_ToDoActivity
import com.example.todoapp.utils.Constants
import com.example.todoapp.utils.clearTime
import com.prolificinteractive.materialcalendarview.CalendarDay


class ListFragment : Fragment() {
    lateinit var binding: FragmentListBinding
    lateinit var todosAdapter: TodosAdapter
    var todoList = mutableListOf<Task>()
    private var selectedDay = CalendarDay.today()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRV()
        refreshTodos()
        initCalendar()
    }

    private fun initCalendar() {
        binding.calendarView.selectedDate = CalendarDay.today()
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            selectedDay = date
            refreshTodos()
        }
    }

    fun refreshTodos() {
        val calender = Calendar.getInstance()
        calender.set(Calendar.YEAR, selectedDay.year)
        calender.set(Calendar.MONTH, selectedDay.month - 1)
        calender.set(Calendar.DAY_OF_MONTH, selectedDay.day)
        calender.set(Calendar.YEAR, selectedDay.year)
        calender.clearTime()

        val newtodo = AppDatabase.createDatabase(requireContext()).tasksDao()
            .getTasksByDate(calender.timeInMillis)
        todosAdapter.refrshList(newtodo)
    }

    private fun initRV() {
        todosAdapter = TodosAdapter(todoList)
        binding.todosRV.adapter = todosAdapter
        todosAdapter.listener = object : TodosAdapter.onitemClickListener {
            override fun onDelete(todo: Task) {
                AppDatabase.createDatabase(requireContext()).tasksDao().DeleteTask(todo)
                refreshTodos()
            }

            override fun onDone(todo: Task, position: Int) {
                todo.isDone = !todo.isDone
                AppDatabase.createDatabase(requireContext()).tasksDao().UpdateTask(todo)
                todosAdapter.notifyItemChanged(position)
            }

            override fun onitemViewClick(todo: Task, position: Int) {
                val intent = Intent(requireContext(), Edit_ToDoActivity::class.java)
                intent.putExtra(Constants.TASK_KEY, todo)
                startActivity(intent)
            }

        }


    }

    override fun onResume() {
        super.onResume()

        val updatedTaskList = AppDatabase.createDatabase(requireContext()).tasksDao().getAllTasks()
        todoList.clear()
        todoList.addAll(updatedTaskList)
        refreshTodos()
    }

}
