package com.example.smartattendance.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smartattendance.R
import androidx.fragment.app.Fragment
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.example.smartattendance.fragment.Attendance
import com.example.smartattendance.fragment.AddDbFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationBar : BottomNavigationBar
    var fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        bottomNavigationBar = findViewById(R.id.bottom_navigation)

        // setting up bottomNavigation design
        bottomNavigationBar
            .setMode(BottomNavigationBar.MODE_SHIFTING)
        bottomNavigationBar
            .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
        bottomNavigationBar
            .setActiveColor(R.color.white)
            .setInActiveColor(R.color.navigation_inactive_color)

        // intent receiving
        var stream = intent.getStringExtra("Stream")
        var sem = intent.getStringExtra("Sem")

        // set the fragment as per request
        if(sem != null){
            setTab(0)
            fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, Attendance(stream,sem))
                .commit()
            //  once attendance is taken same details shouldn't be shown
            stream = null
            sem = null

        }else{
            setTab(1)
            fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, AddDbFragment())
                .commit()
        }

        // navigate using bottomNavigationBar
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