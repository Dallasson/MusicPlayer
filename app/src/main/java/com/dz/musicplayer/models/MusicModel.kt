package com.dz.musicplayer.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class MusicModel (
    var songName : String
) : Serializable
