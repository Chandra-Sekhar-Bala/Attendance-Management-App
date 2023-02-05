package com.example.smartattendance.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendance.R
import com.example.smartattendance.databinding.ActivitySignupBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignupActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignupBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var sh : SharedPreferences
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 100
    private val TAG = "SignupActivity"

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
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    // Handle sign-in result
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        Log.e(TAG, "Initiate signIn")
        if (requestCode == RC_SIGN_IN) {
            Log.e(TAG, "Successful bro")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "SuccessFul")
                    // Sign in success, update UI with the signed-in user's information
                    Log.e(TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    Log.e(TAG, "UnSuccessful")
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithCredential:failure", task.exception)
                    Snackbar.make(findViewById(R.id.root_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
//    // handle google sign-in
//    private fun signInWithGoogle() {
//        binding.progressBar.visibility = View.VISIBLE
//        val signInIntent = googleSignInClient.signInIntent
//        launcher.launch(signInIntent)
//    }
//
//    // handle intent callback
//    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//            res->
//                if(res.resultCode == Activity.RESULT_OK){
//                    val task = GoogleSignIn.getSignedInAccountFromIntent(res.data)
//                    handleResult(task)
//                }else{
//                    binding.progressBar.visibility = View.GONE
//                }
//    }
//    // got result handle it
//    private fun handleResult(task: Task<GoogleSignInAccount>) {
//
//        Log.e("TEST", "HandleResult")
//        if(task.isSuccessful){
//            val account : GoogleSignInAccount? = task.result
//            if(account != null){
//                updateUI(account)
//            }
//        }else{
//            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
//        }
//        binding.progressBar.visibility = View.GONE
//    }

        private fun updateUI(user: FirebaseUser?) {
           if(user!= null){
               Log.e(TAG, "User is not null")
//               val edit = sh.edit()
//               edit.putString(getString(R.string.id), user.uid)
//               edit.apply()
               Toast.makeText(this, "Welcome " + user.displayName, Toast.LENGTH_SHORT).show()
               startActivity(Intent(this, MainActivity::class.java))
               finishAffinity()
           }else{
               Log.e(TAG, "User is null")
           }
        }

}

