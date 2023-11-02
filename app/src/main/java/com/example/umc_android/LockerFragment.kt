package com.example.umc_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_android.databinding.FragmentAlbumBinding
import com.example.umc_android.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding

    private  val information = arrayListOf("저장한 곡", "음악파일")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdpter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdpter
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) {  // tab으로 눌렀을때 넘어가게 함.
                tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }
}
