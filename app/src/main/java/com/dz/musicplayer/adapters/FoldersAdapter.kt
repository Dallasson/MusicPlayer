package com.dz.musicplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dz.musicplayer.R
import com.dz.musicplayer.databinding.FoldersRowsLayoutBinding
import com.dz.musicplayer.models.FolderModel

class FoldersAdapter(var list : MutableList<FolderModel>) : RecyclerView.Adapter<FoldersAdapter.FolderViewHolder>() {

    class FolderViewHolder(var foldersRowsLayoutBinding: FoldersRowsLayoutBinding) :
            RecyclerView.ViewHolder(foldersRowsLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = DataBindingUtil.inflate<FoldersRowsLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.folders_rows_layout,
            parent,false
        )
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
       val data = list[position]
        holder.foldersRowsLayoutBinding.model = data
    }

    override fun getItemCount(): Int {
       return list.size
    }
}