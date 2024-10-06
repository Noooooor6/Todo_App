package com.example.todoapp

import android.graphics.Color
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.todoapp.database.model.Task
import com.example.todoapp.databinding.ItemTodoBinding

class TodosAdapter(var todoList: List<Task>) : Adapter<TodosAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun changeTaskStatus(isDone: Boolean) {
            if (isDone) {
                binding.roundedView.setBackgroundResource(R.drawable.rounded_shape_done)
                binding.titleTv.setTextColor(Color.GREEN)
                binding.isDone.setImageResource(R.drawable.ic_done)
            } else {
                val blue = ContextCompat.getColor(itemView.context, R.color.blue)
                binding.titleTv.setTextColor(blue)
                binding.roundedView.setBackgroundResource(R.drawable.rounded_shape)
                binding.isDone.setBackgroundResource(R.drawable.ic_is_done)
            }
        }

        fun bind(todo: Task) {
            binding.titleTv.text = todo.title
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = todo.date
            changeTaskStatus(todo.isDone)
            refrshList(todoList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todoList[position]
        holder.binding.titleTv.text = todo.title
        holder.binding.descriptionTv.text = todo.description
        holder.binding.rightView.setOnClickListener {
            listener?.onDelete(todo)
        }
        holder.binding.isDone.setOnClickListener {
            listener?.onDone(todo, position)
        }
        holder.changeTaskStatus(todo.isDone)
        holder.binding.cardView.setOnClickListener {
            listener?.onitemViewClick(todo, position)
        }
    }

    fun refrshList(newList: List<Task>) {
        todoList = newList
        notifyDataSetChanged()
    }

    var listener: onitemClickListener? = null

    interface onitemClickListener {
        fun onDelete(todo: Task)
        fun onDone(todo: Task, position: Int)
        fun onitemViewClick(todo: Task, position: Int)
    }
}