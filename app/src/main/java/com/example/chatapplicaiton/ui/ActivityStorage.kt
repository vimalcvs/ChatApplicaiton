package com.example.chatapplicaiton.ui

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.chatapplicaiton.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ActivityStorage : AppCompatActivity() {
    private lateinit var storage: StorageReference
    var auth: FirebaseAuth = Firebase.auth
    fun uploadimage(uri: Uri, view: View, context: Context) {
        storage = Firebase.storage.reference
        storage.child("userimage").child(auth.currentUser!!.uid).putFile(uri).addOnSuccessListener {
            Toast.makeText(context, "image added successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            view.findViewById<ImageView>(R.id.addedimage).setImageResource(R.drawable.user)
        }
    }

    fun getsettingimage(context: Context, view: View) {
        storage = Firebase.storage.reference
        storage.child("userimage")
            .child(auth.currentUser!!.uid).downloadUrl.addOnCompleteListener {
                it.addOnSuccessListener {
                    val imageView: ImageView = view.findViewById(R.id.addedimage)
                    Glide.with(context).load(it).into(imageView)
                }
            }
    }

    suspend fun putUserImage(uidList: ArrayList<String>): ArrayList<Map<String, Uri>> {
        val uriList = ArrayList<Map<String, Uri>>()
        val storage = Firebase.storage.reference
        val ref = storage.child("userimage")
        withContext(Dispatchers.IO) {
            for (uid in uidList) {
                val imageRef = ref.child(uid)
                try {
                    val downloadUrl = imageRef.downloadUrl.await()
                    uriList.add(mapOf(uid to downloadUrl))
                } catch (e: Exception) {
                    val defaultUri =
                        "https://firebasestorage.googleapis.com/v0/b/chat-application-8a1d7.appspot.com/o/userimage%2Fuser.png?alt=media&token=1d4272bd-0f9e-4e67-9738-727ce232ad2c".toUri()
                    uriList.add(mapOf(uid to defaultUri))
                }
            }
        }
        return uriList
    }
}