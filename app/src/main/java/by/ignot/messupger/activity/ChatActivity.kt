package by.ignot.messupger.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.ignot.messupger.MessageAdapter
import by.ignot.messupger.MessageItem
import by.ignot.messupger.R

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView : RecyclerView
    private lateinit var chatAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
    private lateinit var chatLayoutManager: RecyclerView.LayoutManager

    private lateinit var messageList : ArrayList<MessageItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messageList = ArrayList()

        initializeRecyclerView()
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
