package com.example.smartattendance.activities

import android.annotation.SuppressLint
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignupActivity : AppCompatActivity() {
    private lateinit var bind : ActivitySignupBinding
    private lateinit var signInRequest : BeginSignInRequest.Builder
    val RC_SIGN_IN = 100
    var firstTime = 1
    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(bind.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        mAuth = Firebase.auth

        // If user id already registered :
        val sh = getSharedPreferences("UserID", MODE_PRIVATE)
        val id = sh.getString("id",null)
        if(id != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        LoadAnimation()

        // lets make sign up process:
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
              .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = mGoogleSignInClient.signInIntent
        bind.GoogleAccount.setOnClickListener{
            Log.e("USERDETAILS","Going in ")
        startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    fun LoadAnimation(){

        bind.emailId.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btu))
        bind.password.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btu))
        bind.signupBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btu))
        bind.LoginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btu))

    }



    override fun onResume() {
        super.onResume()

        bind.signupBtn.setOnClickListener {
            if (firstTime == 1) {
                bind.LoginBtn.visibility = View.GONE
                bind.emailField.visibility = View.VISIBLE
                bind.passwordField.visibility = View.VISIBLE
                ++firstTime
                LoadAnimation()
            } else {
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

            if (firstTime == 1) {
                bind.signupBtn.visibility = View.GONE
                ++firstTime
                bind.emailField.visibility = View.VISIBLE
                bind.passwordField.visibility = View.VISIBLE
                LoadAnimation()
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
    }

    private fun signUp(email: String, password : String) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"SignUp Successful",Toast.LENGTH_SHORT).show()
                    val sh = getSharedPreferences("UserID", MODE_PRIVATE)
                    val edit = sh.edit()
                    edit.putString("id",email)
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
                    edit.putString("id",email)
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

    private fun validateData(): Boolean {

        if(bind.emailId.text.toString().trim().isEmpty()){
            bind.emailId.error = "Empty field"
            bind.emailId.requestFocus()
        }
        else if(!bind.emailId.text.toString().trim().endsWith(".com")){
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.e("USERDETAILS","Matched ")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            val idToken = account.id
            val name = account.displayName
            val photo = account.photoUrl
            val email = account.email

            val sh = getSharedPreferences("UserID", MODE_PRIVATE)
            val edit = sh.edit()

            edit.putString("id",email)
            edit.apply()

            Log.e("USERDETAILS","Name $name\n email: $email photo: $photo And id is $idToken ")

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        } catch (e: ApiException) {

            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()

            Log.e("USERDETAILS"," Exception code ${e.message} + ${e.statusCode}")
        }

    }

}