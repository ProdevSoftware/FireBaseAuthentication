package com.example.firebaseauthentication

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.firebaseauthentication.MainActivity.Companion.PUT_EXTRA_EMAIL
import com.example.firebaseauthentication.MainActivity.Companion.PUT_EXTRA_NAME
import com.example.firebaseauthentication.MainActivity.Companion.PUT_EXTRA_PHOTO_URL

class ShowResultActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var name: String
    private lateinit var photoUrl: String
    private lateinit var emailTv: TextView
    private lateinit var nameTv: TextView
    private lateinit var profileView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_result)
        emailTv = findViewById(R.id.tvUserEmail)
        nameTv = findViewById(R.id.tvUserName)
        profileView = findViewById(R.id.ivUserProfileImage)
        intent.let {
            email = intent.getStringExtra(PUT_EXTRA_EMAIL).toString()
            name = intent.getStringExtra(PUT_EXTRA_NAME).toString()
            photoUrl = intent.getStringExtra(PUT_EXTRA_PHOTO_URL).toString()
        }
        Glide.with(this)
            .load(photoUrl)
            .centerCrop()
            .placeholder(resources.getDrawable(R.drawable.ic_launcher_background, null))
            .into(profileView)
        emailTv.text = "Email :- $email"
        nameTv.text = "Name :- $name"
    }
}