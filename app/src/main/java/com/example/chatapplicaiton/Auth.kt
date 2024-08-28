package com.example.chatapplicaiton

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.chatapplicaiton.ui.ActivityLogin
import com.example.chatapplicaiton.ui.MainActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Auth(val email: String?, private val pass: String?, val context: Context) {
    private var database = FirebaseDatabase.getInstance().getReference()
    private val auth = Firebase.auth

    fun logIn(view: View) {
        val errormail: TextInputLayout = view.findViewById(R.id.edit_email_layout)
        val errorpass: TextInputLayout = view.findViewById(R.id.edit_password_layout)
       // val empty: TextView = view.findViewById(R.id.empty)
        errormail.visibility = View.INVISIBLE
        errorpass.visibility = View.INVISIBLE
      //  empty.visibility = View.INVISIBLE

        if (email!!.isEmpty() || pass!!.isEmpty()) {
          //  empty.text = "Please enter the empty field"
           // empty.visibility = View.VISIBLE
        } else {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (auth.currentUser!!.isEmailVerified) {
                        Database().loginupdate()
                        val userpass =
                            database.child("User").child(auth.currentUser!!.uid).child("password")
                                .get().addOnCompleteListener { it ->
                                    if (it.toString() != pass) {
                                        database.child("User").child(auth.currentUser!!.uid)
                                            .child("password").setValue(pass)
                                    }
                                }
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)

                    } else {
                        Toast.makeText(
                            context,
                            "please check inbox and verfy your email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    val error = it.exception!!.message.toString()
                    when (error) {
                        "The email address is badly formatted." -> {
                            errormail.visibility = View.VISIBLE
                        }

                        "There is no user record corresponding to this identifier. The user may have been deleted." -> {

                            errormail.visibility = View.VISIBLE
                        }

                        "The password is invalid or the user does not have a password." -> {
                         //   errorpass.text = "please enter the correct password"
                            errorpass.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    fun signup(view: View, username: String) {
        val errormessage = view.findViewById<TextView>(R.id.errorview)
        val name: TextView = view.findViewById(R.id.edit_name)
        if (name.text.length >= 5) {
            if (email!!.endsWith("@gmail.com")) {
                val sub1 = email.substring(0, email.indexOf("@"))
                if (sub1.length >= 5) {
                    strongpass(pass.toString(), view, username)
                }
            } else {
                errormessage.text = "please enter valid email"
                errormessage.visibility = View.VISIBLE
            }
        } else {
            errormessage.text = "your name must have more than 5 character"
            errormessage.visibility = View.VISIBLE
        }

    }

    private fun strongpass(password: String, view: View, name: String) {
        val errormessage = view.findViewById<TextView>(R.id.errorview)
        val specialchars = arrayOf("!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_")
        var count: Int = 0
        if (password.length >= 10) {
            for (i in password) {
                if (specialchars.contains(i.toString())) {
                    count += 1
                }
            }
            if (count >= 1) {
                auth.createUserWithEmailAndPassword(email!!, pass!!).addOnCompleteListener {
                    auth.currentUser!!.sendEmailVerification()
                    Toast.makeText(
                        context,
                        "please check inbox to verify your email",
                        Toast.LENGTH_SHORT
                    ).show()
                    Database().userdata(name, password, email)

                    val intent = Intent(context, ActivityLogin::class.java)
                    context.startActivity(intent)
                }
            } else {
                // tell the user what's need in a specific way
                errormessage.text = "password must have at least one Special character"
                errormessage.visibility = View.VISIBLE
            }
        } else {
            errormessage.text = "password must have at least9 numbers and one character"
            errormessage.visibility = View.VISIBLE
        }
    }
}