package by.ignot.messupger.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.ignot.messupger.message.MessageAdapter
import by.ignot.messupger.message.MessageItem
import by.ignot.messupger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView : RecyclerView
    private lateinit var chatAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
    private lateinit var chatLayoutManager: RecyclerView.LayoutManager

    private var chatId : String? = null

    private lateinit var messageList : ArrayList<MessageItem>

    private lateinit var chatDatabaseReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatId = intent.extras?.getString("chatId")

        chatDatabaseReference = FirebaseDatabase.getInstance().reference.child("chat").child(chatId!!)

        messageList = ArrayList()

        val sendMessageButton : Button = findViewById(R.id.sendMessageButtonId)
        sendMessageButton.setOnClickListener{
            sendMessage()
        }
        initializeRecyclerView()
        getChatMessages()
    }

    private fun getChatMessages(){
        chatDatabaseReference.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                if (dataSnapshot.exists()){
                    var messageText = ""
                    var senderId = ""

                    if (dataSnapshot.child("messageText").value != null){
                        messageText = dataSnapshot.child("messageText").value.toString()
                    }
                    if (dataSnapshot.child("senderId").value != null){
                        senderId = dataSnapshot.child("senderId").value.toString()
                    }

                    val messageItem = MessageItem(dataSnapshot.key!!, senderId, messageText)
                    messageList.add(messageItem)
                    chatLayoutManager.scrollToPosition(messageList.size - 1)
                    chatAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            }

        })
    }

    private fun sendMessage(){
        val message : EditText = findViewById(R.id.inputMessageId)
        if (message.text.toString().isNotEmpty()){
            val messageDatabaseReference = chatDatabaseReference.push()

            val newMessageMap = HashMap<String, String>()
            newMessageMap["messageText"] = message.text.toString()
            newMessageMap["senderId"] = FirebaseAuth.getInstance().uid!!

            messageDatabaseReference.updateChildren(newMessageMap.toMap())
        }
        message.text = null
    }

    private fun initializeRecyclerView(){
        chatRecyclerView = findViewById(R.id.chatId)
        chatRecyclerView.isNestedScrollingEnabled = false
        chatRecyclerView.setHasFixedSize(false)
        chatLayoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        chatRecyclerView.layoutManager = chatLayoutManager

        chatAdapter = MessageAdapter(messageList)
        chatRecyclerView.adapter = chatAdapter
    }
}
