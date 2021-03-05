package com.dz.musicplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dz.musicplayer.R
import com.dz.musicplayer.databinding.SongsRowsLayoutBinding
import com.dz.musicplayer.listeners.MusicListener
import com.dz.musicplayer.models.SongModel

class SongsAdapter(var musicList : MutableList<SongModel>,
                   var listener : MusicListener) : RecyclerView.Adapter<SongsAdapter.MusicHolder>() {

    class MusicHolder(var musicRowsLayoutBinding: SongsRowsLayoutBinding) :
        RecyclerView.ViewHolder(musicRowsLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicHolder {
        val view = DataBindingUtil.inflate<SongsRowsLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.songs_rows_layout,
            parent,false
        )
        return MusicHolder(view)
    }

    override fun onBindViewHolder(holder: MusicHolder, position: Int) {
       val data = musicList[position]
        holder.musicRowsLayoutBinding.model = data
        holder.musicRowsLayoutBinding.songName.isSelected = true
        holder.musicRowsLayoutBinding.position = position
        holder.musicRowsLayoutBinding.listener = listener

    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}