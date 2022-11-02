package com.example.smartattendance.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smartattendance.R
import androidx.fragment.app.Fragment
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.example.smartattendance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationBar : BottomNavigationBar
    var fragmentManager = supportFragmentManager
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize views
        bottomNavigationBar = findViewById(R.id.bottom_navigation)

        // setting up bottomNavigation design
        bottomNavigationBar
            .setMode(BottomNavigationBar.MODE_SHIFTING)
            .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
            .setBarBackgroundColor("#154EDF")

        // intent receiving
        var stream = intent.getStringExtra("Stream")
        var sem = intent.getStringExtra("Sem")

        // set the fragment as per request
        if(sem != null){
            setTab(0)
            fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, AttendanceFragment(stream,sem))
                .commit()
            //  once attendance is taken same details shouldn't be shown
            stream = null
            sem = null
        }else{
            setTab(1)
            fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, AddDBFragment())
                .commit()
        }

        // navigate using bottomNavigationBar
        bottomNavigationBar.setTabSelectedListener(object :
            BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {

                var fragment: Fragment? = null
                when (position) {
                    0 -> fragment = AttendanceFragment(stream,sem)
                    1 -> fragment = AddDBFragment()
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

    private fun setTab(pos: Int) {
        bottomNavigationBar
            .addItem(
                BottomNavigationItem(
                    R.drawable.attendance_icon,
                    "Attendance"
                ).setActiveColor(R.color.navigation_attendance_active_color)
            )
            .addItem(
                BottomNavigationItem(
                    R.drawable.database_icon,
                    "Database"
                ).setActiveColor(R.color.navigation_database_active_color)
            ).setFirstSelectedPosition(pos)
            .initialise()
    }
}