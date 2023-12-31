package com.example.umc_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_android.databinding.ItemAlbumBinding

class AlbumRVAdapter(private var albumList: ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {


    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        this.mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener{
            mItemClickListener.onItemClick(albumList[position])
        }
        holder.binding.itemAlbumPlayImgIv.setOnClickListener {
            mItemClickListener.onItemClick(albumList[position])
        }
    }
    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onPlayAlbum(album: Album)
    }
//    interface OnItemClickListener {
//        fun onItemClick(album : Album)
//        fun onPlayAlbum(album : Album)
//    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(album:Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImage!!)
        }
    }
    interface CommunicationInterface {
        fun sendData(album: Album)
    }

}