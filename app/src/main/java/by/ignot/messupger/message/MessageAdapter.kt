package by.ignot.messupger.message

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import by.ignot.messupger.R

class MessageAdapter(private var messageList: ArrayList<MessageItem>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutView : View = LayoutInflater.from(parent.context).inflate(R.layout.item_message, null, false)
        val layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutView.layoutParams = layoutParams
        return MessageViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.message.text = messageList[position].messageText
        holder.sender.text = messageList[position].senderName
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message : TextView = view.findViewById(R.id.messageId)
        val sender : TextView = view.findViewById(R.id.senderId)
        var layout : LinearLayout = view.findViewById(R.id.messageLayoutId)
    }
}