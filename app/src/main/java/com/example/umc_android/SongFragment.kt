package com.example.umc_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.umc_android.databinding.ActivitySongBinding
import com.example.umc_android.databinding.FragmentDetailBinding
import com.example.umc_android.databinding.FragmentSongBinding

class SongFragment : Fragment() {

    lateinit var binding : FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater,container,false)

        binding.btnToggleOffIv.setOnClickListener{
            myMixStatus(false)
        }
        binding.btnToggleOnIv.setOnClickListener{
            myMixStatus(true)
        }

        binding.songLalacLayout.setOnClickListener{
            Toast.makeText(activity,"LILAC",Toast.LENGTH_SHORT).show()
        }

        binding.songFluLayout.setOnClickListener{
            Toast.makeText(activity,"Flu",Toast.LENGTH_SHORT).show()
        }

        binding.songCoinLayout.setOnClickListener{
            Toast.makeText(activity,"Coin",Toast.LENGTH_SHORT).show()
        }

        binding.songSpringLayout.setOnClickListener{
            Toast.makeText(activity,"봄 안녕 봄",Toast.LENGTH_SHORT).show()
        }

        binding.songNothingLayout.setOnClickListener{
            Toast.makeText(activity,"무제",Toast.LENGTH_SHORT).show()
        }
        return binding.root


    }

    fun myMixStatus(isPlaying: Boolean){
        if(isPlaying) {
            binding.btnToggleOffIv.visibility = View.VISIBLE
            binding.btnToggleOnIv.visibility = View.GONE
        }
        else {
            binding.btnToggleOffIv.visibility = View.GONE
            binding.btnToggleOnIv.visibility = View.VISIBLE
        }
    }
}