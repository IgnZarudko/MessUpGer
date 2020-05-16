package by.ignot.messupger.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import by.ignot.messupger.R

class ChatListAdapter(private var chatList: ArrayList<ChatItem>) :
    RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val layoutView : View = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, null, false)
        val layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutView.layoutParams = layoutParams

        return ChatListViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.title.text = chatList[position].chatId

        holder.layout.setOnClickListener{

        }
    }

    class ChatListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout : LinearLayout = view.findViewById(R.id.layoutId)
        var title : TextView = view.findViewById(R.id.titleId)
    }
}