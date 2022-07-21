package com.example.smartattendance.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendance.R
import com.example.smartattendance.databinding.ActivitySignuploginBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern


class SignupLogin : AppCompatActivity() {

    private lateinit var bind : ActivitySignuploginBinding
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySignuploginBinding.inflate(layoutInflater)
        setContentView(bind.root)

        mAuth = FirebaseAuth.getInstance()

        // set the title for button : Login/ Sign-up
        val buttonTitle = intent.getStringExtra(getString(R.string.signup_or_login))
        bind.signupLoginBtn.text = buttonTitle

        // if user came to sign up - no need for password reset!
        if(buttonTitle == getString(R.string.sign_up)) bind.forgetPassword.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

        bind.signupLoginBtn.setOnClickListener{
            // if user entries are all okay?
            if(validateData()){
                // wants to signup :
                if(bind.signupLoginBtn.text == getString(R.string.sign_up)){
                    signUp(
                        bind.emailId.text.toString().trim(),
                        bind.password.text.toString().trim()
                    )
                }
                // login the user :
                else{
                    Login(
                        bind.emailId.text.toString().trim(),
                        bind.password.text.toString().trim()
                    )
                }
            }
        }

        // Reset password
        bind.forgetPassword.setOnClickListener{
            // clear all entries
            bind.password.text?.clear()
            // validate email field
            if (isValidEmail(bind.emailId.text.toString().trim()))
            {
                bind.emailId.error = "Enter email Please"
                bind.emailId.requestFocus()
            }
            else{
                resetPassword()
            }
        }

    }

    private fun resetPassword() {
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

    // Send a reset link to user email
    private fun sendResetPasswordLink(email : String) {
        Log.d("EmailVerify", "Email id is: $email")
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email sent, please check your mail", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{

                Toast.makeText(this, it.message , Toast.LENGTH_SHORT).show()
            }

    }


    private fun signUp(email: String, password : String) {
        bind.progressBar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"SignUp Successful", Toast.LENGTH_SHORT).show()

                    // save user data for future use
                    val sh = getSharedPreferences(getString(R.string.user_id), MODE_PRIVATE)
                    val edit = sh.edit()
                    val user=mAuth.currentUser
                    val id=user!!.uid
                    edit.putString(getString(R.string.id),id)
                    edit.apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                // progressbar handling
                bind.progressBar.visibility = View.GONE
            }.addOnFailureListener{
                // progressbar gone
                bind.progressBar.visibility = View.GONE
                Toast.makeText(this,"${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun Login(email: String, password : String) {
        // start with showing progressbar
        bind.progressBar.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    val sh = getSharedPreferences(getString(R.string.user_id), MODE_PRIVATE)
                    val edit = sh.edit()
                    val user=mAuth.currentUser
                    val id=user!!.uid
                    edit.putString(getString(R.string.id),id)
                    edit.apply()
                    Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                // progressbar handling
                bind.progressBar.visibility = View.GONE
            }.addOnFailureListener{
                bind.progressBar.visibility = View.GONE
                Toast.makeText(this,"${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Validate the user input
    private fun validateData(): Boolean {

        if(bind.emailId.text.toString().trim().isEmpty()){
            bind.emailId.error = "Empty field"
            bind.emailId.requestFocus()
        }
        else if(!isValidEmail(bind.emailId.text.toString().trim())){
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
   private fun isValidEmail(email: String?): Boolean {
        val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$"
        val pat: Pattern = Pattern.compile(emailRegex)
        return if (email == null) false else pat.matcher(email).matches()
    }

}