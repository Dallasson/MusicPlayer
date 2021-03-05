package com.dz.musicplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dz.musicplayer.databinding.LibraryLayoutBinding
import com.dz.musicplayer.databinding.SongsLayoutBinding

class LibraryFragment : Fragment() {

    private var _binding : LibraryLayoutBinding? = null
    private val binding  get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding  = LibraryLayoutBinding.inflate(inflater,container,false)

        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}