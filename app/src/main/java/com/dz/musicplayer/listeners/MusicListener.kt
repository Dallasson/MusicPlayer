package com.dz.musicplayer.listeners

import com.dz.musicplayer.models.SongModel

interface MusicListener  {
      fun onSongSelected(musicModel: SongModel, position : Int)
}