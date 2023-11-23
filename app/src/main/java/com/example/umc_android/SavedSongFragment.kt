package com.example.umc_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_android.databinding.FragmentSavedSongBinding

class SavedSongFragment : Fragment() {
    //private var albumDatas = ArrayList<Album>()
    lateinit var songDB: SongDatabase
    lateinit var binding : FragmentSavedSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedSongBinding.inflate(inflater,container,false)

        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
//        albumDatas.apply {
//            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
//            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
//            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
//            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
//            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
//            add(Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6))
//            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
//            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
//            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
//            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
//            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
//            add(Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6))
//            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
//            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
//            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
//            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
//            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
//            add(Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6))
//        }
//
//        val lockerAlbumRVAdapter = LockerAlbumRVAdapter(albumDatas)
//        binding.lockerMusicAlbumRv.adapter = lockerAlbumRVAdapter
//        binding.lockerMusicAlbumRv.layoutManager = LinearLayoutManager(requireActivity())
//
//        lockerAlbumRVAdapter.setItemClickListener(object : LockerAlbumRVAdapter.OnItemClickListener {
//            override fun onItemClick(album: Album) {
//                changeAlbumFragment(album)
//            }
//            override fun onRemoveAlbum(position: Int) {
//                lockerAlbumRVAdapter.removeItem(position)
//            }
//        })

//        return binding.root
    }
    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerMusicAlbumRv.layoutManager = LinearLayoutManager(requireActivity())
        val lockerAlbumRVAdapter = LockerAlbumRVAdapter()

        lockerAlbumRVAdapter.setItemClickListener(object : LockerAlbumRVAdapter.OnItemClickListener {

            override fun onRemoveAlbum(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }
        })
        binding.lockerMusicAlbumRv.adapter = lockerAlbumRVAdapter
        lockerAlbumRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
    }

}

//    private fun changeAlbumFragment(album: Album) {
//        (context as MainActivity).supportFragmentManager.beginTransaction()
//            .replace(R.id.main_frm, AlbumFragment().apply {
//                arguments = Bundle().apply {
//                    val gson = Gson()
//                    val albumToJson = gson.toJson(album)
//                    putString("album", albumToJson)
//                }
//            })
//            .commitAllowingStateLoss()
//    }
//}

