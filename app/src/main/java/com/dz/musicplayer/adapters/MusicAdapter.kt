package com.dz.musicplayer.adapters

import android.service.autofill.TextValueSanitizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dz.musicplayer.R
import com.dz.musicplayer.models.MusicModel

class MusicAdapter(var musicList : MutableList<MusicModel>) : RecyclerView.Adapter<MusicAdapter.MusicHolder>() {

    class MusicHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val songNameText = itemView.findViewById<TextView>(R.id.songName)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicHolder {
       val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.music_rows_layout,parent,false)
        return MusicHolder(view)
    }

    override fun onBindViewHolder(holder: MusicHolder, position: Int) {
       val data = musicList[position]
        holder.songNameText.text = data.songName
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}