package com.example.quickdroid.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.quickdroid.databinding.ItemVideoBinding
import com.example.quickdroid.model.Video
import com.example.quickdroid.ui.activity.VideoPlayerActivity

class VideoAdapter(
    private val context: Context,
    private val videosLiveData: LiveData<List<Video>>
) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    private lateinit var binding: ItemVideoBinding

    inner class ViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(video: Video) {
            /*Glide.with(binding.root).load(video.thumbnailPath).into(binding.actionImage)*/
            binding.root.setOnClickListener {
                Intent(context, VideoPlayerActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    putExtra("path", video.path)
                    context.startActivity(this)
                }
            }
            binding.actionImage.setImageBitmap(video.image)
            binding.title.text = video.title
            binding.size.text = video.size
            binding.lastModified.text = video.lastModified
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        videosLiveData.observeForever { videos ->
            holder.bind(videos[position])
        }
    }

    override fun getItemCount(): Int {
        return videosLiveData.value?.size ?: 0
    }
}