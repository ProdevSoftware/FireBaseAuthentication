package com.example.firebaseauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    companion object {
         const val RC_SIGN_IN = 1001
         const val PUT_EXTRA_EMAIL = "PUT_EXTRA_EMAIL"
         const val PUT_EXTRA_NAME = "PUT_EXTRA_NAME"
         const val PUT_EXTRA_PHOTO_URL = "PUT_EXTRA_PHOTO_URL"

    }

    private lateinit var loginWithGoogle: Button
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginWithGoogle = findViewById(R.id.loginWithGoogle)
        firebaseAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@MainActivity, gso)
        loginWithGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    account.idToken?.let { fireBaseAuth(it) }
                } else {
                    Log.d("authentication", "Error in social login")
                }
            } catch (e: ApiException) {
                e.printStackTrace()
                Log.d("authentication", "Google sign in failed")
            }
        } else {
            Log.d("authentication", "something went wrong")
        }
    }

    private fun fireBaseAuth(idToken: String) {

        val credential : AuthCredential = GoogleAuthProvider.getCredential(idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
               if(it.isSuccessful){
                   val user: FirebaseUser? = firebaseAuth.currentUser
                   if (user != null) {
                       val intent = Intent(this@MainActivity, ShowResultActivity::class.java)
                       intent.putExtra(PUT_EXTRA_EMAIL,user.email)
                       intent.putExtra(PUT_EXTRA_NAME,user.displayName)
                       intent.putExtra(PUT_EXTRA_PHOTO_URL,user.photoUrl.toString())
                       startActivity(intent)
                   }
               }
            }

    }
}