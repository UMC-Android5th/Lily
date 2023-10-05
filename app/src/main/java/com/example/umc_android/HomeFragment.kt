package com.example.umc_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_android.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homePannelUnderAlbum1Iv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm,AlbumFragment()).commitAllowingStateLoss()
        }

        binding.homePannelUnderAlbum2Iv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm,AlbumFragment()).commitAllowingStateLoss()
        }

        binding.homePannelUnderAlbum3Iv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm,AlbumFragment()).commitAllowingStateLoss()
        }
        return binding.root
    }
}