package by.ignot.messupger

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserListAdapter(private var userList: ArrayList<UserObject>) :
    RecyclerView.Adapter<UserListAdapter.UserListViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val layoutView : View = LayoutInflater.from(parent.context).inflate(R.layout.item_user, null, false)
        val layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutView.layoutParams = layoutParams

        return UserListViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.name.text = userList[position].name
        holder.phone.text = userList[position].phone

        holder.layout.setOnClickListener{
            val key = FirebaseDatabase.getInstance().reference.child("chat").push().key

            FirebaseDatabase.getInstance().reference.child("user").child(FirebaseAuth.getInstance().uid!!).child("chat").child(key!!).setValue(true)
            FirebaseDatabase.getInstance().reference.child("user").child(userList[position].uid!!).child("chat").child(key!!).setValue(true)
        }
    }

    class UserListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout : LinearLayout = view.findViewById(R.id.layoutId)
        var name : TextView = view.findViewById(R.id.nameId)
        var phone : TextView = view.findViewById(R.id.phoneId)
    }
}