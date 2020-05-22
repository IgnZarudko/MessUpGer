package by.ignot.messupger.media

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.ignot.messupger.R

class MediaAdapter : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>{

    private lateinit var mediaList: ArrayList<String>
    private lateinit var context: Context
    constructor(context: Context, mediaList: ArrayList<String>){
        this.context = context
        this.mediaList = mediaList
    }

    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val layoutView : View = LayoutInflater.from(parent.context).inflate(R.layout.item_media, null, false)
        return MediaViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}