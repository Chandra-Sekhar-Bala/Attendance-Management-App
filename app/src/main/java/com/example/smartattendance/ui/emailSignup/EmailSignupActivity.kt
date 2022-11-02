package com.example.smartattendance.ui.emailSignup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.smartattendance.R
import com.example.smartattendance.databinding.ActivityEmailSignupBinding
import com.example.smartattendance.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern


class EmailSignupActivity : AppCompatActivity() {

    private lateinit var bind : ActivityEmailSignupBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var viewModel: EmailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityEmailSignupBinding.inflate(layoutInflater)
        setContentView(bind.root)

        auth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this)[EmailViewModel::class.java]

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
                    viewModel.signUp(
                        bind.emailId.text.toString().trim(),
                        bind.password.text.toString().trim(),
                        auth,
                        this
                    )
                }
                // login the user :
                else{
                    viewModel.logIn(
                        bind.emailId.text.toString().trim(),
                        bind.password.text.toString().trim(),
                        auth,
                        this
                    )
                }
            }
        }

        // Reset password
        bind.forgetPassword.setOnClickListener{
            // clear all entries
            bind.password.text?.clear()
            // validate email field
            if (validateData())
            {
                viewModel.resetPassword(bind.emailId.text.toString().trim(), auth, this)
            }
        }

        viewModel.progress.observe(this){ progeess->
            when(progeess){
                Progress.LOADING -> bind.progressBar.visibility = View.VISIBLE
                Progress.DONE -> bind.progressBar.visibility = View.GONE
                Progress.FINISHED -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
            }
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