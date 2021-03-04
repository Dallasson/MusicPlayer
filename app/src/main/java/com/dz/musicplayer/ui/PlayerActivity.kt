package com.dz.musicplayer.ui

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.dz.musicplayer.R
import com.dz.musicplayer.databinding.ActivityPlayerBinding
import java.io.File

class PlayerActivity : AppCompatActivity() {
    private var _binding : ActivityPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var listOfSongs : ArrayList<File>
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        listOfSongs = arrayListOf()



        val intent = intent
        intent?.let {
            listOfSongs  = (it.getSerializableExtra("songsList") as ArrayList<File>?)!!

            val songName = intent.getStringExtra("song")
            val position = intent.getIntExtra("position", 0)
            val uri = Uri.parse(listOfSongs[position].toString())
            binding.songName.text = songName

            // TODO : Creating MediaPlayer
            mediaPlayer = MediaPlayer.create(applicationContext, uri)

            binding.playBtn.setOnClickListener {
                if(mediaPlayer.isPlaying){
                    mediaPlayer.pause()
                    binding.playBtn.setBackgroundResource(R.drawable.ic_baseline_pause_24)
                } else {
                    mediaPlayer.start()
                    binding.playBtn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}