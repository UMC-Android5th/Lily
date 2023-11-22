package com.example.umc_android

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_android.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    //전역변수
    lateinit var binding : ActivitySongBinding
    lateinit var song : Song
    lateinit var timer : Timer
    private var mediaPlyer: MediaPlayer? = null
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener{
            setPlayerStatus(false)
        }
        binding.songPauseIv.setOnClickListener{
            setPlayerStatus(true)
        }
        binding.songRepeatIv.setOnClickListener{
            setPlayerOption(false)
        }
        binding.songRandom2Iv.setOnClickListener{
            setPlayerOption(true)
        }

        binding.songRandomIv.setOnClickListener{
            setPlayerOption2(false)
        }
        binding.songRepeat2Iv.setOnClickListener{
            setPlayerOption2(true)
        }

        binding.songDownIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val msg = binding.songMusicTitleTv.text.toString()
            intent.putExtra("msg", msg)
            startActivity(intent)
            finish()
        }

        if (intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

    }

    //반복, 셔틀 이미지 변경 코드
    fun setPlayerOption(isPlaying: Boolean) {
        if(isPlaying) {
            binding.songRepeatIv.visibility = View.VISIBLE
            binding.songRandom2Iv.visibility = View.GONE
        }
        else {
            binding.songRepeatIv.visibility = View.GONE
            binding.songRandom2Iv.visibility = View.VISIBLE
        }
    }

    fun setPlayerOption2(isPlaying: Boolean){
        if(isPlaying) {
            binding.songRandomIv.visibility = View.VISIBLE
            binding.songRepeat2Iv.visibility = View.GONE
        }
        else {
            binding.songRandomIv.visibility = View.GONE
            binding.songRepeat2Iv.visibility = View.VISIBLE
        }
    }
    fun setPlayerStatus(isPlaying : Boolean) {
        //스레드 초기화
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            mediaPlyer?.start()
        }
        else {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            if(mediaPlyer?.isPlaying == true) {
                mediaPlyer?.pause()
            }
        }
    }

    //사용자가 포커스를 잃었을 때 음악을 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)

        song.second= ((binding.songProgressSb.progress * song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)

        editor.apply()
    }

    //스레드 실습
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlyer?.release() //미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlyer = null //미디어 플레이어 해제
    }

    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlyer = MediaPlayer.create(this,music)

        setPlayerStatus(song.isPlaying)
    }

    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }


    inner class Timer(private val playTime: Int,var isPlaying: Boolean = true):Thread(){

        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while (true){
                    if (second >= playTime){
                        break
                    }

                    if (isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
                        }

                        if (mills % 1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d",second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }

            }catch (e: InterruptedException){
                Log.d("Song","쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }


}





