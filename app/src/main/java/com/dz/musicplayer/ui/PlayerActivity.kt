package com.dz.musicplayer.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bullhead.equalizer.DialogEqualizerFragment
import com.dz.musicplayer.R
import com.dz.musicplayer.databinding.ActivityPlayerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit

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
            var position = intent.getIntExtra("position", 0)
            val uri = Uri.parse(listOfSongs[position].toString())
            binding.songName.isSelected = true
            binding.songName.text = songName

            // TODO : Creating MediaPlayer
            mediaPlayer = MediaPlayer.create(applicationContext, uri)

            binding.playBtn.setOnClickListener {
                if(mediaPlayer.isPlaying){
                    mediaPlayer.pause()
                    binding.playBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                } else {
                    mediaPlayer.start()
                    binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_24)
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
                animateImageViewForward(binding.imageView)

                val audioSessionId = mediaPlayer.audioSessionId
                if(audioSessionId != -1){
                    binding.blast.setAudioSessionId(audioSessionId)
                }
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
                animateImageViewBackWords(binding.imageView)

                val audioSessionId = mediaPlayer.audioSessionId
                if(audioSessionId != -1){
                    binding.blast.setAudioSessionId(audioSessionId)
                }
            }
            binding.fastNext.setOnClickListener {
                // TODO : Allow the user to add 15000
                if(mediaPlayer.isPlaying){
                    mediaPlayer.seekTo(mediaPlayer.currentPosition + 15000)
                }
            }
            binding.fastBack.setOnClickListener {
                // TODO : Allow the user to minus 15000
                if(mediaPlayer.isPlaying){
                    mediaPlayer.seekTo(mediaPlayer.currentPosition - 150000)
                }
            }

            mediaPlayer.setOnCompletionListener {
                binding.playBtn.performClick()
                mediaPlayer.stop()
                mediaPlayer.release()
                // if(!mediaPlayer.isLooping){
                     position = ((position - 1) %listOfSongs.size)
                     val uri = Uri.parse(listOfSongs[position].toString())
                     mediaPlayer = MediaPlayer.create(applicationContext,uri)
                     mediaPlayer.start()
                     binding.songName.text = listOfSongs[position].name
                     binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_24)
                     animateImageViewForward(binding.imageView)

                     val audioSessionId = mediaPlayer.audioSessionId
                     if(audioSessionId != -1){
                         mediaPlayer.audioSessionId = audioSessionId
                  //   }
                 }
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
            binding.seekBar.progressDrawable.setColorFilter(resources.getColor(R.color.av_orange),PorterDuff.Mode.MULTIPLY)
            binding.seekBar.thumb.setColorFilter(resources.getColor(R.color.av_green),PorterDuff.Mode.SRC_IN)


            // TODO : User Updating music to certain position
            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    mediaPlayer.seekTo(seekBar?.progress!!)
                }
            })
            binding.positiveNum.text =   createTime(mediaPlayer.duration)
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                binding.negativeNum.text = createTime(mediaPlayer.currentPosition)
            }


            // TODO : Working on the audio Visualizer

            val audioSessionId = mediaPlayer.audioSessionId
            if(audioSessionId != -1){
                binding.blast.setAudioSessionId(audioSessionId)
            }


            // TODO : Audio Equalizer
            binding.equalizer.setOnClickListener {
                val dialogEqualizer = DialogEqualizerFragment.newBuilder()
                    .setAudioSessionId(audioSessionId)
                    .themeColor(ContextCompat.getColor(this, R.color.black))
                    .textColor(ContextCompat.getColor(this, R.color.white))
                    .accentAlpha(ContextCompat.getColor(this, R.color.av_red))
                    .darkColor(ContextCompat.getColor(this, R.color.av_green))
                    .setAccentColor(ContextCompat.getColor(this, R.color.black))
                    .build()

                if(mediaPlayer.isPlaying){
                    dialogEqualizer.show(supportFragmentManager,"eq")
                }
            }

            // TODO : Shuffle Audio
            binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if(buttonView.isPressed){
                    if(isChecked){
                        mediaPlayer.isLooping = isChecked
                    } else {
                        mediaPlayer.isLooping = isChecked
                    }
                }

            }

        }
    }

    private fun createTime(duration : Int) : String {
        // TODO : Creating Time
        var time = ""
        val min = duration / 1000 / 60
        val sec = duration / 1000 % 60

        time+= "$min:"

        if(sec < 10){
            time+="0"
        }

        time+=sec
        return time
    }

    private fun animateImageViewForward(view : View){
        val objectAnimator = ObjectAnimator.ofFloat(view,"rotationY",0f,360f)
        objectAnimator.duration = 1000
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(objectAnimator)
        animatorSet.start()
    }

    private fun animateImageViewBackWords(view : View){
        val objectAnimator = ObjectAnimator.ofFloat(view,"rotationX",0f,360f)
        objectAnimator.duration = 1000
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(objectAnimator)
        animatorSet.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.blast.release()
        if(mediaPlayer.isPlaying){
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        _binding = null
    }
}