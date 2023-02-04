package com.example.smartattendance.ui

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendance.R
import com.example.smartattendance.databinding.ActivitySignupBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignupActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignupBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var sh : SharedPreferences
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // firebase auth initialize
        mAuth = Firebase.auth

        // If user is already registered move to home:
        if(mAuth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
//        sh = getSharedPreferences(getString(R.string.user_id), MODE_PRIVATE)
//        val id = sh.getString(getString(R.string.id),null)
//        if(id != null){
//
//        }

        buildGoogleSigIn()
    }

    private fun buildGoogleSigIn() {

        // goggle SignIn client
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onResume() {
        super.onResume()

        // google sign-in
        binding.signInWithGoogle.setOnClickListener{
            signInWithGoogle()
        }

    }
    // handle google sign-in
    private fun signInWithGoogle() {
        binding.progressBar.visibility = View.VISIBLE
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    // handle intent callback
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            res->
                if(res.resultCode == Activity.RESULT_OK){
                    val task = GoogleSignIn.getSignedInAccountFromIntent(res.data)
                    handleResult(task)
                }else{
                    binding.progressBar.visibility = View.GONE
                }
    }
    // got result handle it
    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if(account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
        binding.progressBar.visibility = View.GONE
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            signInTheUser(account)

        }.addOnFailureListener{
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, it.message.toString() , Toast.LENGTH_SHORT).show()
        }
    }
    // user made it to the end
    private fun signInTheUser(account: GoogleSignInAccount) {
        binding.progressBar.visibility = View.GONE
        val edit = sh.edit()
        edit.putString(getString(R.string.id), account.id)
        edit.apply()

        Toast.makeText(this, "Welcome "+account.displayName , Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
    }
}

