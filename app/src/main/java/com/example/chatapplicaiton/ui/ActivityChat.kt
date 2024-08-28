package com.example.chatapplicaiton.ui

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplicaiton.adapter.AdapterMessage
import com.example.chatapplicaiton.model.ModelMessage
import com.example.chatapplicaiton.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActivityChat : AppCompatActivity() {

    private lateinit var messagesRecycler: RecyclerView
    private lateinit var messageAdapter: AdapterMessage
    private lateinit var messageList: ArrayList<ModelMessage>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var senderNode: String
    private lateinit var receiverNode: String
    private lateinit var sendbutton: ImageView
    private lateinit var messagebox: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val rootView: View = findViewById(android.R.id.content)

        messageList = ArrayList()
        messagesRecycler = findViewById(R.id.messages)
        messagesRecycler.layoutManager = LinearLayoutManager(this)
        messageAdapter = AdapterMessage(this, messageList)
        messagesRecycler.adapter = messageAdapter
        // Initialize Firebase Database reference
        mDbRef = FirebaseDatabase.getInstance().reference

        // Get sender and receiver UIDs (customize based on your logic)
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        val receiverUid = intent.getStringExtra("uid")

        // Create unique chat nodes for sender and receiver
        senderNode = "$receiverUid$senderUid"
        receiverNode = "$senderUid$receiverUid"
        // Initialize RecyclerView and adapter
        sendbutton = findViewById(R.id.sendBtn)
        val userbar = findViewById<TextView>(R.id.userbar)
        userbar.text = intent.getStringExtra("name")
        messagebox = findViewById(R.id.messageBox)
        val footter = findViewById<LinearLayout>(R.id.footerlayout)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > 100) { // If keyboard is visible
                val layoutParams = footter.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.bottomMargin = keypadHeight
                footter.layoutParams = layoutParams
                // Optionally scroll to the end of the messages
                messagesRecycler.scrollToPosition(messageList.size - 1)
            } else {
                val layoutParams = footter.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.bottomMargin = 0
                footter.layoutParams = layoutParams
            }
            mDbRef.child("chats").child(senderNode).child("messages")
                .addValueEventListener(object : ValueEventListener {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messageList.clear()
                        for (postSnapshot in snapshot.children) {
                            val message = postSnapshot.getValue(ModelMessage::class.java)
                            messageList.add(message!!)
                        }
                        println("======================we are in Datachange")
                        println(messageList.size)
                        messageAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
            sendbutton.setOnClickListener {
                val message = messagebox.text.toString()
                val messageobject = ModelMessage(message, senderUid)
                mDbRef.child("chats").child(senderNode).child("messages").push()
                    .setValue(messageobject).addOnSuccessListener {
                    if (senderNode != receiverNode) {

                        mDbRef.child("chats").child(receiverNode).child("messages").push()
                            .setValue(messageobject)
                    }

                }
                messagebox.setText("")
            }
            // Listen for new messages in the database
            // Additional logic for sending messages can be added here
            // For example, handling user input and updating the database
        }
    }
}
