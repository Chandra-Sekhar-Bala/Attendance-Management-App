package com.example.smartattendance.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.smartattendance.R
import com.example.smartattendance.databinding.ActivitySignupBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignupActivity : AppCompatActivity() {
    private lateinit var bind : ActivitySignupBinding
    private var firstTime = 1
    private lateinit var mAuth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(bind.root)

        // firebase auth initialize
        mAuth = Firebase.auth
        //
        mAuth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)

        // If user is already registered :
        val sh = getSharedPreferences(getString(R.string.user_id), MODE_PRIVATE)
        val id = sh.getString(getString(R.string.id),null)
        if(id != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        /* show some animation,
        * 0 = all should be animate
        * 1 = Except signup button
        * */
        LoadAnimation(0)
    }

    fun LoadAnimation( rq : Int){
        // re  == Some elements should not be animated
        when(rq) {
            0 -> {
                bind.LoginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btu))
                bind.signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btu))
            }
            1 -> {
                bind.LoginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btu))
            }
        }
        bind.emailId.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btu))
        bind.password.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btu))
        bind.forgetPassword.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btu))

    }
    override fun onResume() {
        super.onResume()

        bind.signupBtn.setOnClickListener {
            // If user clicks  signup button first time : Show and hide some elements
            if (firstTime == 1) { // user has clicked for choosing purpose
                bind.LoginBtn.visibility = View.GONE
                bind.forgetPassword.visibility = View.GONE
                bind.emailField.visibility = View.VISIBLE
                bind.passwordField.visibility = View.VISIBLE
                ++firstTime
                LoadAnimation(2)
            } else {
                // this time user has clicked for signup purpose
                if (validateData()) {
                    bind.progressBar.visibility = View.VISIBLE
                    signUp(
                        bind.emailId.text.toString().trim(),
                        bind.password.text.toString().trim()
                    )
                }
            }
        }

        bind.LoginBtn.setOnClickListener {
            // If user clicks login button first time : Show ans hide some elements
            if (firstTime == 1) {
                bind.signupBtn.visibility = View.GONE
                ++firstTime
                bind.emailField.visibility = View.VISIBLE
                bind.passwordField.visibility = View.VISIBLE
                bind.forgetPassword.visibility = View.VISIBLE
                LoadAnimation(1)
            }
            else {
                if (validateData()) {
                    bind.progressBar.visibility = View.VISIBLE
                    Login(
                        bind.emailId.text.toString().trim(),
                        bind.password.text.toString().trim()
                    )
                }
            }
        }

        bind.forgetPassword.setOnClickListener{
            // validation
            if(bind.emailId.text.toString().isEmpty() || !bind.emailId.text.toString().endsWith("@gmail.com")){
                Toast.makeText(this,"Enter your email please",Toast.LENGTH_SHORT).show()
            }else {
                // get the work done :
                AlertDialog.Builder(this)
                    .setTitle("Forger Password?")
                    .setMessage("Are you sure you want reset password?")
                    .setPositiveButton(
                        android.R.string.yes
                    ) { _, _ ->
                        sendResetPasswordLink(bind.emailId.text.toString())

                    }.setNegativeButton(android.R.string.no)
                    { _, _ ->
                    }.setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }
        }


    }

    // Send a reset link to user email
    private fun sendResetPasswordLink(email : String) {
        Log.d("EmailVerify", "Email id is: $email")
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                   Toast.makeText(this, "Email sent, please check your mail",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{

                   Toast.makeText(this, it.message ,Toast.LENGTH_SHORT).show()
            }

    }


    private fun signUp(email: String, password : String) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"SignUp Successful",Toast.LENGTH_SHORT).show()
                    val sh = getSharedPreferences(getString(R.string.user_id), MODE_PRIVATE)
                    val edit = sh.edit()
                    val user=mAuth.currentUser
                    val id=user!!.uid
                    edit.putString(getString(R.string.id),id)
                    edit.apply()


                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                bind.progressBar.visibility = View.GONE
            }.addOnFailureListener{
                Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT).show()
            }
    }
    private fun Login(email: String, password : String) {

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val sh = getSharedPreferences("UserID", MODE_PRIVATE)
                    val edit = sh.edit()
                    val user=mAuth.currentUser
                    val id=user!!.uid
                    edit.putString("id",id)
                    edit.apply()

                    Toast.makeText(this,"Login Successful",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                }
                bind.progressBar.visibility = View.GONE
            }.addOnFailureListener{
                Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT).show()
            }
    }
// Validate the user input
    private fun validateData(): Boolean {

        if(bind.emailId.text.toString().trim().isEmpty()){
            bind.emailId.error = "Empty field"
            bind.emailId.requestFocus()
        }
        else if(!bind.emailId.text.toString().trim().endsWith("@gmail.com")){
            bind.emailId.error = "Not a valid mail"
            bind.emailId.requestFocus()
        }
        else if(bind.password.text.toString().trim().isEmpty()){
            bind.password.error = "Empty field"
            bind.password.requestFocus()
        }else if(bind.password.text.toString().trim().length < 6){
            bind.password.error = "Password length should be greater then 6"
            bind.password.requestFocus()
        }
        else
            return true

        return false
    }

}