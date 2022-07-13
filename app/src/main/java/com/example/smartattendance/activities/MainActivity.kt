package com.example.smartattendance.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.smartattendance.R
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.example.smartattendance.fragment.Attendance
import com.example.smartattendance.fragment.AddDbFragment
import com.example.smartattendance.fragment.Profile

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationBar : BottomNavigationBar
    var fragmentManager = supportFragmentManager
    private lateinit var frameLayout : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initilizie views
        frameLayout = findViewById(R.id.frameLayout)
        bottomNavigationBar = findViewById(R.id.bottom_navigation)
        // setting up bottomNavigationbar design
        bottomNavigationBar
            .setMode(BottomNavigationBar.MODE_SHIFTING)
        bottomNavigationBar
            .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
        bottomNavigationBar
            .setActiveColor("#FFFFFF")
            .setInActiveColor("#353b48")

        // intent receiving
        var stream = intent.getStringExtra("Stream")
        var sem = intent.getStringExtra("Sem")

        // set the fragment as per request
        if(sem != null){
            setTab(0)
            fragmentManager.beginTransaction()
                .replace(frameLayout, Attendance(stream,sem))
                .commit()
            //  once attendance is taken same details shouldn't be shown
            stream = null
            sem = null

        }else{
            setTab(1)
            fragmentManager.beginTransaction()
                .replace(frameLayout, AddDbFragment())
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
                        .replace(frameLayout, fragment).commit()
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
                ).setActiveColor("#040338")
            )
            .addItem(
                BottomNavigationItem(
                    R.drawable.database_icon,
                    "Database"
                ).setActiveColor("#070565")
            ).setFirstSelectedPosition(pos)
            .initialise()

    }
}