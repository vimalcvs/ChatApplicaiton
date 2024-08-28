package com.example.chatapplicaiton.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapplicaiton.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ActivitySetting : AppCompatActivity() {
    var auth: FirebaseAuth = Firebase.auth
    private lateinit var userimage: ImageView

    @RequiresApi(Build.VERSION_CODES.P)
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val view = findViewById<View>(android.R.id.content)
                ActivityStorage().uploadimage(it, view, this)
                println("=============${it.path}")
                userimage.setImageURI(it)
                for (i in 0..<MainActivity.list.size) {
                    if (MainActivity.list[i].uid == auth.currentUser!!.uid) {
                        MainActivity.list[i].iamgeurl = it
                        MainActivity.customAdapter.notifyItemChanged(i)
                    }
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val view = findViewById<View>(android.R.id.content)
        userimage = findViewById(R.id.addedimage)
        ActivityStorage().getsettingimage(this, view)
        val logoutbtn = findViewById<Button>(R.id.logoutbtn)
        val addimagebtn = findViewById<Button>(R.id.changeimagebtn)
        logoutbtn.setOnClickListener {
            MainActivity.list.clear()
            auth.signOut()
            val intent = Intent(this, ActivityLogin::class.java).setAction("finish_activity")
            startActivity(intent)
            finishAffinity()
        }
        addimagebtn.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }
}