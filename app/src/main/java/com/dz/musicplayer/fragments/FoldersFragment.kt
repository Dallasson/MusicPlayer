package com.dz.musicplayer.fragments

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dz.musicplayer.adapters.FoldersAdapter
import com.dz.musicplayer.databinding.FoldersLayoutBinding
import com.dz.musicplayer.databinding.SongsLayoutBinding
import com.dz.musicplayer.models.FolderModel
import java.io.File
import java.io.LineNumberReader
import kotlin.math.sign
import kotlin.math.sin

class FoldersFragment : Fragment() {

    private lateinit var foldersAdapter: FoldersAdapter
    private var _binding : FoldersLayoutBinding? = null
    private val binding  get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding  = FoldersLayoutBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.foldersRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.foldersRecycler.setHasFixedSize(true)
        getAudioFolders(Environment.getExternalStorageDirectory())
    }

    private fun getAudioFolders(file : File){
        val list = arrayListOf<FolderModel>()
        val allFolders = file.listFiles()
        for(singleFolder in allFolders){
            if(singleFolder.isDirectory &&  !singleFolder.isHidden){
               list.add(FolderModel(singleFolder.name,singleFolder.name))
                foldersAdapter = FoldersAdapter(list)
                binding.foldersRecycler.adapter = foldersAdapter
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}