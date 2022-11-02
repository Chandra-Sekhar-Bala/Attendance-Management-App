package com.example.smartattendance.ui.emailSignup

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartattendance.R
import com.example.smartattendance.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth


enum class Progress{LOADING, DONE, FINISHED}

class EmailViewModel : ViewModel() {
    private var _progress = MutableLiveData<Progress>()
    val progress : LiveData<Progress> get() = _progress

    // Sign-up the user
    fun signUp(email: String, password : String, auth:FirebaseAuth, context: Context) {
        _progress.value = Progress.LOADING
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {

                    Toast.makeText(context,"SignUp Successful", Toast.LENGTH_SHORT).show()
                    // save user data for future use
                    val sh = context.getSharedPreferences(context.getString(R.string.user_id),
                        AppCompatActivity.MODE_PRIVATE
                    )
                    val edit = sh.edit()
                    val user=auth.currentUser
                    val id=user!!.uid
                    edit.putString(context.getString(R.string.id),id)
                    edit.apply()

                    _progress.value = Progress.FINISHED
                }
                // progressbar handling
                _progress.value = Progress.DONE
            }.addOnFailureListener{
                // progressbar gone
                _progress.value = Progress.DONE
                Toast.makeText(context,"${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // login the user
    fun logIn(email: String, password : String,auth: FirebaseAuth, context: Context) {
        // start with showing progressbar
        _progress.value = Progress.LOADING
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    val sh = context.getSharedPreferences(context.getString(R.string.user_id),
                        AppCompatActivity.MODE_PRIVATE
                    )
                    val edit = sh.edit()
                    val user=auth.currentUser
                    val id=user!!.uid
                    edit.putString(context.getString(R.string.id),id)
                    edit.apply()
                    Toast.makeText(context,"Login Successful", Toast.LENGTH_SHORT).show()

                    _progress.value = Progress.FINISHED
                }
                // progressbar handling
                _progress.value = Progress.DONE

            }.addOnFailureListener{
                _progress.value = Progress.DONE
                Toast.makeText(context,"${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    fun resetPassword(email: String, auth: FirebaseAuth,  context: Context) {
        // get the work done :
        AlertDialog.Builder(context)
            .setTitle("Forger Password?")
            .setMessage("Are you sure you want reset password?")
            .setPositiveButton(
                android.R.string.yes
            ) { _, _ ->
                sendResetPasswordLink(email, auth, context)

            }.setNegativeButton(android.R.string.no)
            { _, _ ->
            }.setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    // Send a reset link to user email
    private fun sendResetPasswordLink(email : String, auth:FirebaseAuth, context: Context) {

        Log.d("EmailVerify", "Email id is: $email")
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Email sent, please check your mail", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{

                Toast.makeText(context, it.message , Toast.LENGTH_SHORT).show()
            }

    }
}