package com.dz.musicplayer.adapters

import android.service.autofill.TextValueSanitizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dz.musicplayer.R
import com.dz.musicplayer.databinding.MusicRowsLayoutBinding
import com.dz.musicplayer.listeners.MusicListener
import com.dz.musicplayer.models.MusicModel

class MusicAdapter(var musicList : MutableList<MusicModel>,
       var listener : MusicListener) : RecyclerView.Adapter<MusicAdapter.MusicHolder>() {

    class MusicHolder(var musicRowsLayoutBinding: MusicRowsLayoutBinding) :
        RecyclerView.ViewHolder(musicRowsLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicHolder {
        val view = DataBindingUtil.inflate<MusicRowsLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.music_rows_layout,
            parent,false
        )
        return MusicHolder(view)
    }

    override fun onBindViewHolder(holder: MusicHolder, position: Int) {
       val data = musicList[position]
        holder.musicRowsLayoutBinding.model = data
        holder.musicRowsLayoutBinding.position = position
        holder.musicRowsLayoutBinding.listener = listener
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}