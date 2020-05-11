package by.ignot.messupger

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
    }

    class UserListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name : TextView = view.findViewById(R.id.nameId)
        var phone : TextView = view.findViewById(R.id.phoneId)
    }


}