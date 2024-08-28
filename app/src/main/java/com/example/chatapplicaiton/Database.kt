package com.example.chatapplicaiton

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class Database {
    private val auth = Firebase.auth

    private var database = FirebaseDatabase.getInstance().getReference()
    fun userdata(name: String, password: String, email: String) {
        val uservalues = hashMapOf(
            "name" to name, "email" to email, "password" to password
        )
        val child = database.child("/User")
        child.child(auth.uid.toString()).setValue(uservalues)


    }

    @Exclude
    fun loginupdate() {
        println(auth.currentUser!!.uid)
        database.child("User").child(auth.currentUser!!.uid)
            .updateChildren(hashMapOf<String, Any>("is email verified" to auth.currentUser!!.isEmailVerified))
    }

    fun getdata(context: Context): Map<String, ArrayList<String>> {

        val listname = ArrayList<String>()
        val listemail = ArrayList<String>()
        val listuid = ArrayList<String>()
        val dataref = database.child("User")
        dataref.addListenerForSingleValueEvent(@SuppressLint("SuspiciousIndentation")
        object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listname.clear()
                listemail.clear()
                val snap = snapshot.value as Map<String, Map<String, *>>
                for (i in snap.keys.toList()) {
                    listuid.add(i)
                }
                val mp = snap.values
                for (i in mp) {
                    listname.add(i["name"].toString())
                    listemail.add(i["email"].toString())
                }
                println(listemail)
                println(listname)
                println(listuid)
            }
        }
        )
        return mapOf(
            "name" to listname,
            "email" to listemail,
            "uid" to listuid
        )
    }
}