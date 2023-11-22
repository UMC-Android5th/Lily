package com.example.umc_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.umc_android.databinding.FragmentAlbumBinding
import com.example.umc_android.databinding.FragmentSongBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {
    lateinit var binding : FragmentAlbumBinding
    private var gson: Gson = Gson()

    private  val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater,container,false)

        binding.albumBackIv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,HomeFragment())
                .commitAllowingStateLoss()
        }
        val albumToJson = arguments?.getString("album")
        val album = gson.fromJson(albumToJson, Album::class.java)
        setInit(album)

        val albumAdpter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdpter  // 슬라이드할때 넘어가게 함
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {  // tab으로 눌렀을때 넘어가게 함.
            tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }
    private fun setInit(album : Album) {
        binding.imgAlbum5thLilacIv.setImageResource(album.coverImage!!)
        binding.album5thTitleTv.text = album.title.toString()
        binding.album5thSingerTv.text = album.singer.toString()
    }
}
