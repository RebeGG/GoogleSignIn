package com.example.lab6

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val SIGN_IN_CODE = 1
    private val SIGN_OUT_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        sign_in_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, SIGN_IN_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SIGN_IN_CODE -> {
                val signInGoogleTask: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(signInGoogleTask)
            }
            SIGN_OUT_CODE -> signOut()
        }
    }


    override fun onStart() {
        super.onStart()
        val googleAccount = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(googleAccount)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val googleAccount = completedTask.getResult(ApiException::class.java)
            updateUI(googleAccount)
        } catch (e: ApiException) {
            Log.d(getString(R.string.tag), getString(R.string.sign_in_error))
            updateUI(null)
        }
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                StyleableToast.makeText(
                    this,
                    getString(R.string.success_logout),
                    Toast.LENGTH_LONG,
                    R.style.successToast
                ).show()
            }
    }

    private fun updateUI(googleAccount: GoogleSignInAccount?) {
        if (googleAccount != null) {
            val intent = Intent(this, LogOutActivity::class.java)
            intent.putExtra(getString(R.string.user_name_label), googleAccount.displayName)
            intent.putExtra(getString(R.string.user_email_label), googleAccount.email)
            startActivityForResult(intent, SIGN_OUT_CODE)
        }
    }
}