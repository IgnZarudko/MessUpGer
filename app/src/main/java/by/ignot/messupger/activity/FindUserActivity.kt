package by.ignot.messupger.activity

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.ignot.messupger.R
import by.ignot.messupger.UserListAdapter
import by.ignot.messupger.UserObject

class FindUserActivity : AppCompatActivity() {

    private lateinit var userListRecyclerView : RecyclerView
    private lateinit var userListAdapter : RecyclerView.Adapter<UserListAdapter.UserListViewHolder>
    private lateinit var userListLayoutManager: RecyclerView.LayoutManager

    private lateinit var userList : ArrayList<UserObject>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_find_user)

        userList = ArrayList()
        initializeRecyclerView()
        getContactList()
    }

    @SuppressLint("Recycle")
    private fun getContactList(){
        val phones : Cursor? = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        if (phones != null) {
            while (phones.moveToNext()){
                val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                userList.add(UserObject(name, phone))
                userListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initializeRecyclerView(){
        userListRecyclerView = findViewById(R.id.userListId)
        userListRecyclerView.isNestedScrollingEnabled = false
        userListRecyclerView.setHasFixedSize(false)
        userListLayoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        userListRecyclerView.layoutManager = userListLayoutManager

        userListAdapter = UserListAdapter(userList)
        userListRecyclerView.adapter = userListAdapter
    }

}
