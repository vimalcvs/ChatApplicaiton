package com.example.chatapplicaiton.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplicaiton.R
import com.example.chatapplicaiton.model.ModelUser
import com.example.chatapplicaiton.ui.ActivityChat

class AdapterUser(val context: Context, private val userList: ArrayList<ModelUser>, val view: View) :
    RecyclerView.Adapter<AdapterUser.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.nameview.text = currentUser.name
        holder.emailview.text = currentUser.email
        Glide.with(view).load(currentUser.iamgeurl).into(holder.imageview)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ActivityChat::class.java)
                .putExtra("name", currentUser.name)
                .putExtra("uid", currentUser.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameview = itemView.findViewById<TextView>(R.id.nameview)
        val emailview = itemView.findViewById<TextView>(R.id.emailview)
        val imageview = itemView.findViewById<ImageView>(R.id.user_image)
    }
}