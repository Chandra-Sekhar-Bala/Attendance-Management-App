package com.example.smartattendance.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smartattendance.R
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.example.smartattendance.fragment.Attendance
import com.example.smartattendance.fragment.AddDbFragment
import com.example.smartattendance.fragment.Profile

class MainActivity : AppCompatActivity() {
    var fragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val frameLayout = findViewById<FrameLayout>(R.id.frameLayout)
        val bottomNavigationBar = findViewById<BottomNavigationBar>(R.id.bottom_navigation)
        bottomNavigationBar
            .setMode(BottomNavigationBar.MODE_SHIFTING)
        bottomNavigationBar
            .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
        bottomNavigationBar
            .setActiveColor("#FFFFFF")
            .setInActiveColor("#353b48")
        bottomNavigationBar
            .addItem(
                BottomNavigationItem(
                    R.drawable.attendance_icon,
                    "Attendance"
                ).setActiveColor("#040338")
            )
            .addItem(
                BottomNavigationItem(
                    R.drawable.database_icon,
                    "Database"
                ).setActiveColor("#070565")
            )
            .initialise()
        if (savedInstanceState == null) {

        }

        val stream = intent.getStringExtra("streamName")
        val sem = intent.getStringExtra("semName")

        if(sem != null){
            fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, Attendance(stream,sem))
                .commit()
        }else{
            fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, AddDbFragment())
                .commit()
        }

        bottomNavigationBar.setTabSelectedListener(object :
            BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                var fragment: Fragment? = null
                when (position) {
                    0 -> fragment = Attendance(stream,sem)
                    1 -> fragment = AddDbFragment()
                }
                if (fragment != null) {
                    fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, fragment).commit()
                }
            }

            override fun onTabUnselected(position: Int) {}
            override fun onTabReselected(position: Int) {}
        })
    }
}