package com.example.smartattendance.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendance.databinding.ActivitySignupBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class SignupActivity : AppCompatActivity() {
    private lateinit var bind : ActivitySignupBinding
    private lateinit var signInRequest : BeginSignInRequest.Builder
    val RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(bind.root)

        // If user id already registered :
        val sh = getSharedPreferences("UserID", MODE_PRIVATE)
        val id = sh.getString("id",null)
        if(id != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

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
        Log.e("USERDETAILS","Handle lol")
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            val idToken = account.id
            val name = account.displayName
            val photo = account.photoUrl
            val email = account.email

            val sh = getSharedPreferences("UserID", MODE_PRIVATE)
            val edit = sh.edit()

            edit.putString("id",idToken)
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