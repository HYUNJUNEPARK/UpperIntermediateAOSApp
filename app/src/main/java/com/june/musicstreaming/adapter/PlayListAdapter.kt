package com.june.musicstreaming.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.june.musicstreaming.MusicListModel
import com.june.musicstreaming.databinding.ItemMusicBinding
import com.june.musicstreaming.fragment.PlayerFragment
import com.june.musicstreaming.fragment.PlayerFragment.Companion.TAG

class PlayListAdapter(val context: Context): ListAdapter<MusicListModel, PlayListAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemMusicBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MusicListModel) {
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
                //callback(item)
//                Log.d(TAG, "bind: ${item.streamUrl}")
                PlayerFragment().play(item.streamUrl, context)
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
        val diffUtil = object: DiffUtil.ItemCallback<MusicListModel>() {
            override fun areItemsTheSame(oldItem: MusicListModel, newItem: MusicListModel): Boolean {
                return oldItem.id == newItem.id
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: MusicListModel, newItem: MusicListModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}