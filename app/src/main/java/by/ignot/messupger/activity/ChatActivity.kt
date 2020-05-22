package by.ignot.messupger.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.ignot.messupger.message.MessageAdapter
import by.ignot.messupger.message.MessageItem
import by.ignot.messupger.R
import by.ignot.messupger.media.MediaAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView : RecyclerView
    private lateinit var chatAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
    private lateinit var chatLayoutManager: RecyclerView.LayoutManager

    private lateinit var mediaRecyclerView: RecyclerView
    private lateinit var mediaAdapter: RecyclerView.Adapter<MediaAdapter.MediaViewHolder>
    private lateinit var mediaLayoutManager: RecyclerView.LayoutManager

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

        val addMediaButton : Button = findViewById(R.id.addMediaButtonId)
        addMediaButton.setOnClickListener{
            openGallery()
        }


        initializeMessageRecyclerView()
        initializeMediaRecyclerView()
        getChatMessages()
    }

    private fun sendMessage(){
        val message : EditText = findViewById(R.id.inputMessageId)
        if (message.text.toString().isNotEmpty()){
            val messageDatabaseReference = chatDatabaseReference.push()

            val newMessageMap = HashMap<String, String>()
            newMessageMap["messageText"] = message.text.toString().trimStart().trimEnd()
            newMessageMap["senderId"] = FirebaseAuth.getInstance().uid!!

            val userNameDatabaseReference = FirebaseDatabase.getInstance().reference.child("user").child(FirebaseAuth.getInstance().uid!!).child("name")
            userNameDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value != null){
                        newMessageMap["senderName"] = dataSnapshot.value.toString()
                    }
                    messageDatabaseReference.updateChildren(newMessageMap.toMap())
                }
            })

        }
        message.text = null
    }

    private fun initializeMessageRecyclerView(){
        chatRecyclerView = findViewById(R.id.chatId)
        chatRecyclerView.isNestedScrollingEnabled = false
        chatRecyclerView.setHasFixedSize(false)
        chatLayoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        chatRecyclerView.layoutManager = chatLayoutManager

        chatAdapter = MessageAdapter(messageList)
        chatRecyclerView.adapter = chatAdapter
    }


    private var pickImageIntent : Int = 1
    private var mediaUriList = ArrayList<String>()

    private fun initializeMediaRecyclerView(){
        mediaRecyclerView = findViewById(R.id.mediaListId)
        mediaRecyclerView.isNestedScrollingEnabled = false
        mediaRecyclerView.setHasFixedSize(false)
        mediaLayoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        mediaRecyclerView.layoutManager = mediaLayoutManager

        mediaAdapter = MediaAdapter(applicationContext, mediaUriList)
        mediaRecyclerView.adapter = mediaAdapter
    }

    private fun openGallery(){
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Choose Picture(s)"), pickImageIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == pickImageIntent){
                if(data?.clipData == null){
                    mediaUriList.add(data?.data.toString())
                } else {
                    for(i in 0 until data.clipData!!.itemCount){
                        mediaUriList.add(data.clipData!!.getItemAt(i).uri.toString())
                    }
                }

                mediaAdapter.notifyDataSetChanged()
            }
        }
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
                    var senderName = ""

                    if (dataSnapshot.child("messageText").value != null){
                        messageText = dataSnapshot.child("messageText").value.toString()
                    }
                    if (dataSnapshot.child("senderId").value != null){
                        senderId = dataSnapshot.child("senderId").value.toString()
                    }
                    if(dataSnapshot.child("senderName").value != null){
                        senderName = dataSnapshot.child("senderName").value.toString()
                    }

                    val messageItem = MessageItem(dataSnapshot.key!!, senderId, messageText, senderName)
                    messageList.add(messageItem)
                    chatLayoutManager.scrollToPosition(messageList.size - 1)
                    chatAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            }

        })
    }
}
