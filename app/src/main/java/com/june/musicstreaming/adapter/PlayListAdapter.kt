package com.june.musicstreaming.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startForegroundService
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.june.musicstreaming.exoPlayer.ExoPlayer
import com.june.musicstreaming.model.MusicModel
import com.june.musicstreaming.databinding.ItemMusicBinding
import com.june.musicstreaming.model.NowPlayingMusicModel
import com.june.musicstreaming.service.ForegroundService

class PlayListAdapter(val context: Context): ListAdapter<MusicModel, PlayListAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemMusicBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MusicModel) {
            binding.itemTrackTextView.text = item.track
            binding.itemArtistTextView.text = item.artist

            Glide.with(binding.itemCoverImageView.context)
                .load(item.coverUrl)
                .thumbnail(0.1f)
                .into(binding.itemCoverImageView)

            if (item.isPlaying) {
                itemView.setBackgroundColor(Color.GRAY)
            }
            else {
                itemView.setBackgroundColor(Color.TRANSPARENT)
            }

            itemView.setOnClickListener {
                val intent = Intent(context, ForegroundService::class.java)
                startForegroundService(context, intent)

                NowPlayingMusicModel.nowPlayingMusic = item
                ExoPlayer().play(item, context)


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentList[position].also { musicListModel ->
            holder.bind(musicListModel)
        }
    }

    companion object {
        val diffUtil = object: DiffUtil.ItemCallback<MusicModel>() {
            override fun areItemsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
                return oldItem.id == newItem.id
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}