package com.example.chatapplicaiton.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplicaiton.adapter.AdapterUser
import com.example.chatapplicaiton.model.ModelUser
import com.example.chatapplicaiton.R

class ActivitySearch : AppCompatActivity() {
    private lateinit var customAdapter: AdapterUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        println(MainActivity.list)
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var userslist = ArrayList<ModelUser>()
        val recyclerView: RecyclerView = findViewById(R.id.searchview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val view: View = findViewById(android.R.id.content)
        customAdapter = AdapterUser(this, userslist, view)
        recyclerView.adapter = customAdapter
        val searchbox: EditText = findViewById(R.id.searchbox)
        searchbox.doOnTextChanged { text, _, _, _ ->
            run {
                userslist.clear()
                for (i in MainActivity.list) {

                    if (i.name.startsWith(text!!, ignoreCase = true) && !userslist.contains(i)) {
                        userslist.add(i)
                        customAdapter.notifyDataSetChanged()
                    }
                }
            }
            if (searchbox.text.toString() == "") {
                userslist.clear()
            }
        }
    }
}