package com.june.youtube.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.june.youtube.databinding.ItemVideoBinding
import com.june.youtube.model.VideoModel

//class VideoRecyclerViewAdapter: RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder>() {
//    var videoList = mutableListOf<VideoModel>()
//
//    inner class ViewHolder(private val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
//
//        init {
//            binding.root.setOnClickListener {
//
//            }
//        }
//
//        fun setContents(video: VideoModel) {
//            binding.titleTextView.text = video.title
//            binding.subTitleTextView.text = video.subtitle
//
//            Glide.with(binding.thumbnailImageView.context)
//                .load(video.thumb)
//                .into(binding.thumbnailImageView)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val video = videoList[position]
//        holder.setContents(video)
//    }
//
//    override fun getItemCount(): Int {
//        return videoList.size
//    }
//}