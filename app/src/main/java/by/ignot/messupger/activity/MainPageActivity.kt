package by.ignot.messupger.activity


import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.ignot.messupger.R
import by.ignot.messupger.chat.ChatListAdapter
import by.ignot.messupger.chat.ChatItem
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainPageActivity : AppCompatActivity() {

    private lateinit var chatListRecyclerView : RecyclerView
    private lateinit var chatListAdapter : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>
    private lateinit var chatListLayoutManager: RecyclerView.LayoutManager

    private lateinit var chatList : ArrayList<ChatItem>

    private lateinit var logOutButton : Button
    private lateinit var findUserButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        Fresco.initialize(this);
        logOutButton = findViewById(R.id.logOutButtonId)

        chatList = ArrayList()

        logOutButton.setOnClickListener{FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, LogInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
            return@setOnClickListener
        }

        findUserButton = findViewById(R.id.findUserButtonId)

        findUserButton.setOnClickListener{
            startActivity(Intent(applicationContext, FindUserActivity::class.java))
        }

        getPermissions()
        initializeRecyclerView()
        getUserChatList()
    }

    private fun getUserChatList(){
        val userChatDatabaseReference = FirebaseDatabase.getInstance().reference.child("user").child(FirebaseAuth.getInstance().uid!!).child("chat")
        userChatDatabaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for (childSnapshot in dataSnapshot.children){
                        val chat = ChatItem(childSnapshot.key)
                        var isChatExists = false
                        for (currentChat in chatList){
                            if(currentChat.chatId == chat.chatId){
                                isChatExists = true
                            }
                        }
                        if (isChatExists)
                            continue
                        chatList.add(chat)
                        chatListAdapter.notifyDataSetChanged()
                    }
                }
            }

        })
    }

    private fun initializeRecyclerView(){
        chatListRecyclerView = findViewById(R.id.chatListId)
        chatListRecyclerView.isNestedScrollingEnabled = false
        chatListRecyclerView.setHasFixedSize(false)
        chatListLayoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        chatListRecyclerView.layoutManager = chatListLayoutManager

        chatListAdapter = ChatListAdapter(chatList)
        chatListRecyclerView.adapter = chatListAdapter
    }

    private fun getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(arrayOf(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS), 1)
        }
    }
}
