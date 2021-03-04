package com.dz.musicplayer.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.ArrayMap
import androidx.recyclerview.widget.LinearLayoutManager
import com.dz.musicplayer.adapters.MusicAdapter
import com.dz.musicplayer.databinding.ActivityMainBinding
import com.dz.musicplayer.databinding.ActivityPlayerBinding
import com.dz.musicplayer.listeners.MusicListener
import com.dz.musicplayer.models.MusicModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var songs : MutableList<MusicModel>
    private lateinit var musicAdapter: MusicAdapter
    private var _binding : ActivityMainBinding? = null
    private val binding  get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songs = mutableListOf()


        Dexter.withContext(this)
            .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    displaySongs()
                    initAdapter()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                }
                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?, p1: PermissionToken?) {
                    p1?.continuePermissionRequest()
                }

            }).check()

        initAdapter()
    }


    private fun initAdapter(){
        binding.musicRecycler.layoutManager = LinearLayoutManager(this)
        binding.musicRecycler.setHasFixedSize(true)

    }

    private fun displaySongs(){
        val arrayOfSongs = songFile(Environment.getExternalStorageDirectory())

        for(i in arrayOfSongs.indices){
            val item = arrayOfSongs[i].name.replace(".mp3","")
                .replace(".wav","")

            songs.add(MusicModel(item))
            musicAdapter = MusicAdapter(songs,object  : MusicListener{
                override fun onSongSelected(musicModel: MusicModel,position : Int) {
                  Intent(this@MainActivity,PlayerActivity::class.java).apply {
                      putExtra("songsList",arrayOfSongs)
                      putExtra("song",musicModel.songName)
                      putExtra("position",position)
                      startActivity(this)
                  }
                }
            })
            binding.musicRecycler.adapter = musicAdapter


        }
    }
    private fun songFile(file : File) : ArrayList<File>{
        val songFiles = arrayListOf<File>()
        val songsList = file.listFiles()

        for(singleFile in songsList){
            if(singleFile.isDirectory && !singleFile.isHidden){
                songFiles.addAll(songFile(singleFile))
            } else {
                if(singleFile.name.endsWith(".mp3") || singleFile.name.endsWith(".wav")){
                    songFiles.add(singleFile)
                }

            }
        }
        return songFiles
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}