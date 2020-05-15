package by.ignot.messupger.activity

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.ignot.messupger.util.CountryToPhonePrefix
import by.ignot.messupger.R
import by.ignot.messupger.UserListAdapter
import by.ignot.messupger.UserObject
import com.google.firebase.database.*

class FindUserActivity : AppCompatActivity() {

    private lateinit var userListRecyclerView : RecyclerView
    private lateinit var userListAdapter : RecyclerView.Adapter<UserListAdapter.UserListViewHolder>
    private lateinit var userListLayoutManager: RecyclerView.LayoutManager

    private lateinit var userList : ArrayList<UserObject>
    private lateinit var contactList : ArrayList<UserObject>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_find_user)

        userList = ArrayList()
        contactList = ArrayList()

        initializeRecyclerView()
        getContactList()
    }

    @SuppressLint("Recycle")
    private fun getContactList(){
        val phones : Cursor? = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        if (phones != null) {
            while (phones.moveToNext()){
                val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                val filteredPhone: Pair<String, String> = phone.partition {
                    !" -()".contains(it)
                }
                phone = filteredPhone.first

                if(phone[0].toString() != "+"){
                    phone = getCountryISO() + phone
                }

                val contact = UserObject(name, phone)
                contactList.add(contact)
                getUserDetails(contact)
            }
        }
    }

    private fun getUserDetails(contact: UserObject) {
        val userDatabaseReference = FirebaseDatabase.getInstance().reference.child("user")
        val query : Query = userDatabaseReference.orderByChild("phone").equalTo(contact.phone)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    var phone = ""
                    var name  = ""
                    for (childSnapshot in dataSnapshot.children){
                        if(childSnapshot.child("phone").value != null){
                            phone = childSnapshot.child("phone").value.toString()
                            name = childSnapshot.child("name").value.toString()
                        }

                        val user = UserObject(name, phone)
                        if (name == phone){
                            for(currentContact in contactList){
                                if (currentContact.phone == user.phone){
                                    user.name = currentContact.name
                                }
                            }

                        }

                        userList.add(user)
                        userListAdapter.notifyDataSetChanged()
                        return
                    }
                }
            }

        })
    }

    private fun getCountryISO() : String?{
        var iso : String? = null

        val telephonyManager : TelephonyManager = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (telephonyManager.networkCountryIso != null){
            if (telephonyManager.networkCountryIso.toString().isNotEmpty())
                iso = telephonyManager.networkCountryIso.toString()
        }

        return CountryToPhonePrefix.getPhone(iso)
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
