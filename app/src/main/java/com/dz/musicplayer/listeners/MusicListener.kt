package com.dz.musicplayer.listeners

import com.dz.musicplayer.models.MusicModel

interface MusicListener  {
      fun onSongSelected(musicModel: MusicModel,position : Int)
}