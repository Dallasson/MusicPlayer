package com.dz.musicplayer.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dz.musicplayer.adapters.SongsAdapter
import com.dz.musicplayer.databinding.SongsLayoutBinding
import com.dz.musicplayer.listeners.MusicListener
import com.dz.musicplayer.models.SongModel
import com.dz.musicplayer.ui.PlayerActivity
import java.io.File

class SongsFragment : Fragment() {

    private lateinit var songs : MutableList<SongModel>
    private lateinit var musicAdapter: SongsAdapter
    private var _binding : SongsLayoutBinding? = null
    private val binding  get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding  = SongsLayoutBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songs = mutableListOf()

        displaySongs()
        initAdapter()
    }

    private fun initAdapter(){
        binding.songsecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.songsecycler.setHasFixedSize(true)

    }

    private fun displaySongs(){
        val arrayOfSongs = songFile(Environment.getExternalStorageDirectory())

        for(i in arrayOfSongs.indices){
            val item = arrayOfSongs[i].name.replace(".mp3","")
                .replace(".wav","")

            songs.add(SongModel(item))
            musicAdapter = SongsAdapter(songs,object  : MusicListener {
                override fun onSongSelected(musicModel: SongModel, position : Int) {
                    Intent(requireContext(), PlayerActivity::class.java).apply {
                        putExtra("songsList",arrayOfSongs)
                        putExtra("song",musicModel.songName)
                        putExtra("position",position)
                        startActivity(this)
                    }
                }
            })
            binding.songsecycler.adapter = musicAdapter


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