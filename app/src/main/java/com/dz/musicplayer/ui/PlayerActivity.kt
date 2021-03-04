package com.dz.musicplayer.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dz.musicplayer.R
import com.dz.musicplayer.databinding.ActivityPlayerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.lang.Exception

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

            var songName = intent.getStringExtra("song")
            var position = intent.getIntExtra("position", 0)
            val uri = Uri.parse(listOfSongs[position].toString())
            binding.songName.text = songName

            // TODO : Creating MediaPlayer
            mediaPlayer = MediaPlayer.create(applicationContext, uri)

            binding.playBtn.setOnClickListener {
                if(mediaPlayer.isPlaying){
                    mediaPlayer.pause()
                    binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_24)
                } else {
                    mediaPlayer.start()
                    binding.playBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
            }
            binding.skipNext.setOnClickListener {
                mediaPlayer.stop()
                mediaPlayer.release()
                position = ((position + 1) %listOfSongs.size)
                val nextSongUri = Uri.parse(listOfSongs[position].toString())
                mediaPlayer = MediaPlayer.create(applicationContext,nextSongUri)
                binding.songName.text = listOfSongs[position].name
                mediaPlayer.start()
                binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_24)
                animateImageView(binding.imageView)
            }
            binding.skipBack.setOnClickListener {
                mediaPlayer.stop()
                mediaPlayer.release()
                position = if((position - 1) < 0) (listOfSongs.size - 1) else (position - 1)
                val previousSongUri = Uri.parse(listOfSongs[position].toString())
                mediaPlayer = MediaPlayer.create(applicationContext,previousSongUri)
                binding.songName.text = listOfSongs[position].name
                mediaPlayer.start()
                binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_24)
                animateImageView(binding.imageView)
            }
            binding.fastNext.setOnClickListener {}
            binding.fastBack.setOnClickListener {}

            mediaPlayer.setOnCompletionListener {
                binding.playBtn.performClick()
            }

            // TODO : Calculating Music Time
            CoroutineScope(Dispatchers.Main).launch {
                val totalDuration = mediaPlayer.duration
                val currentPosition = 0
                while (currentPosition < totalDuration){
                      try {
                          delay(500)
                          val currentProgress = mediaPlayer.currentPosition
                          binding.seekBar.progress = currentProgress
                      }catch (ex : Exception){
                          Timber.d("Exception ${ex.message}")
                      }
                }
            }
            binding.seekBar.max = mediaPlayer.duration

        }

    }
    private fun animateImageView(view : View){
        val objectAnimator = ObjectAnimator.ofFloat(view,"rotation",360f)
        objectAnimator.duration = 1000
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(objectAnimator)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}