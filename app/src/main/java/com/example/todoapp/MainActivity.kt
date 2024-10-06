package com.example.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.Tabs.ListFragment
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.ui.AddTodoBottomSheet

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var todoListFragment = ListFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showListFragment()
        binding.FloatingActionButton.setOnClickListener {
            val addTodoBottomSheet = AddTodoBottomSheet {
                todoListFragment.refreshTodos()
            }
            addTodoBottomSheet.show(supportFragmentManager, null)
        }
        binding.BottomNav.setOnItemSelectedListener {
            if (it.itemId == R.id.SettingsTap) {
                return@setOnItemSelectedListener false
            } else {
                showListFragment()
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun showListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, todoListFragment)
            .commit()
    }
}