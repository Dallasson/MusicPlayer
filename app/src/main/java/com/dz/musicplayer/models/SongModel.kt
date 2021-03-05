package com.dz.musicplayer.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class SongModel (
    var songName : String
) : Serializable
