package com.example.chatapplicaiton.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapplicaiton.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ActivityResetPass : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private lateinit var resetbtn: Button
    private lateinit var sendemail: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resetpassword2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        resetbtn = findViewById(R.id.btnreset)
        sendemail = findViewById(R.id.sendemail)
        resetbtn.setOnClickListener {
            auth.sendPasswordResetEmail(sendemail.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "please check inbox to reset password", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            Toast.makeText(this, "please check inbox to reset password", Toast.LENGTH_SHORT).show()
        }


    }

}