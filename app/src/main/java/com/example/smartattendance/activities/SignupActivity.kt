package com.example.smartattendance.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendance.R
import com.example.smartattendance.databinding.ActivitySignupBinding
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignupActivity : AppCompatActivity() {

    private lateinit var bind : ActivitySignupBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var sh : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(bind.root)

        // firebase auth initialize
        mAuth = Firebase.auth

        // If user is already registered :
        sh = getSharedPreferences(getString(R.string.user_id), MODE_PRIVATE)
        val id = sh.getString(getString(R.string.id),null)
        if(id != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        loadAnimation()
    }

    private fun loadAnimation(){
        // re  == Some elements should not be animated
        bind.LoginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btu))
        bind.signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btu))
        bind.signInWithGoogle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btu))

    }
    override fun onResume() {
        super.onResume()

        // signup
        bind.signupBtn.setOnClickListener {
            login_or_SignUp(getString(R.string.sign_up))
        }
        // Login
        bind.LoginBtn.setOnClickListener {
            login_or_SignUp(getString(R.string.login))
        }

    }

    // Move to next screen  for signup or login
    private fun login_or_SignUp(passingText: String) {
        val intent = Intent(this@SignupActivity, SignupLogin::class.java)
        intent.putExtra(getString(R.string.signup_or_login), passingText)
        startActivity(intent)
    }

}