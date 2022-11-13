package com.example.smartattendance.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smartattendance.R
import com.example.smartattendance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = this.findNavController(R.id.frameLayout)
        binding.bottomNavigation.setupWithNavController(navController)

        // intent receiving
        val stream = intent.getStringExtra("Stream")
        val sem = intent.getStringExtra("Sem")
        val bundle = Bundle()
        bundle.putString("stream",stream)
        bundle.putString("sem",sem)
        // setting up navigation
        if(stream != null) navController.navigate(R.id.attendanceFragment, bundle)

        binding.bottomNavigation.setOnItemSelectedListener { item ->

            /**
             * Called when an item in the navigation menu is selected.
             *
             * @param item The selected item
             * @return true to display the item as the selected item and false if the item should not be
             * selected. Consider setting non-selectable items as disabled preemptively to make them
             * appear non-interactive.
             */
            navController.navigate(item.itemId, bundle)

            true
        }

    }
}