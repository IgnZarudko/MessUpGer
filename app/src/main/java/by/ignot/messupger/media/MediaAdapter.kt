package by.ignot.messupger.media

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import by.ignot.messupger.R
import com.bumptech.glide.Glide

class MediaAdapter : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>{

    private lateinit var mediaList: ArrayList<String>
    private lateinit var context: Context
    constructor(context: Context, mediaList: ArrayList<String>){
        this.context = context
        this.mediaList = mediaList
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val layoutView : View = LayoutInflater.from(parent.context).inflate(R.layout.item_media, null, false)
        return MediaViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        Glide.with(context).load(Uri.parse(mediaList[position])).into(holder.mediaItem)
    }

    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mediaItem : ImageView = itemView.findViewById(R.id.mediaItemId)
    }
}