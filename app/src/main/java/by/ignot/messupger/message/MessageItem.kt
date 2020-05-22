package by.ignot.messupger.message

class MessageItem(val messageId : String, val senderId : String, val messageText : String, val senderName: String = "Unknown", val mediaUrlList: ArrayList<String>)