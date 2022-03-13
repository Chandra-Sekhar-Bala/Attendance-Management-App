package com.example.smartattendance.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.smartattendance.R;
import com.example.smartattendance.fragment.AddDbFragment;
import com.example.smartattendance.fragment.Attendance;
import com.example.smartattendance.fragment.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frameLayout = findViewById(R.id.frameLayout);

        BottomNavigationBar bottomNavigationBar = findViewById(R.id.bottom_navigation);
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        bottomNavigationBar
                .setActiveColor("#FFFFFF")
                .setInActiveColor("#353b48");

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.attendance_icon, "Attendance").setActiveColor("#3949ab"))
                .addItem(new BottomNavigationItem(R.drawable.database_icon, "Database").setActiveColor("#009688"))
                .addItem(new BottomNavigationItem(R.drawable.profile_icon, "Profile").setActiveColor("#ffa726"))
                .initialise();

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout,new Attendance())
                    .commit();
        }

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                Fragment fragment = null;
                 switch (position){
                     case 0 :
                         fragment = new Attendance();
                         break;
                     case 1:
                         fragment = new AddDbFragment();
                         break;
                     case 2:
                         fragment = new Profile();
                 }

                 if(fragment != null){
                     fragmentManager.beginTransaction()
                             .replace(R.id.frameLayout,fragment).commit();
                 }
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });


    }
}
