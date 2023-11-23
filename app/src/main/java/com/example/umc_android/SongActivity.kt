package com.example.umc_android

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_android.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    //전역변수
    lateinit var binding : ActivitySongBinding
    //lateinit var song : Song
    lateinit var timer : Timer
    private var mediaPlayer: MediaPlayer? = null
    //private var gson: Gson = Gson()
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPlayList()

        initPlayList()
       //initSong()
        initClickListener()
        //setPlayer(songs)

        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener{
            setPlayerStatus(true)
            startStopService()
        }
        binding.songPauseIv.setOnClickListener{
            setPlayerStatus(false)
            startStopService()
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
        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
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
//    fun setPlayerStatus(isPlaying : Boolean) {
//        //스레드 초기화
//        song.isPlaying = isPlaying
//        timer.isPlaying = isPlaying
//
//        if(isPlaying) {
//            binding.songMiniplayerIv.visibility = View.VISIBLE
//            binding.songPauseIv.visibility = View.GONE
//            mediaPlyer?.start()
//        }
//        else {
//            binding.songMiniplayerIv.visibility = View.GONE
//            binding.songPauseIv.visibility = View.VISIBLE
//            if(mediaPlyer?.isPlaying == true) {
//                mediaPlyer?.pause()
//            }
//        }
//    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("message", "뒤로가기 버튼 클릭")

        setResult(RESULT_OK, intent)
        finish()
    }

    //사용자가 포커스를 잃었을 때 음악을 중지
    override fun onPause() {
        super.onPause()
        songs[nowPos].second = (songs[nowPos].playTime * binding.songProgressSb.progress) / 100000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        //song.second= ((binding.songProgressSb.progress * song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        //val songJson = gson.toJson(song)
        editor.putInt("songId", songs[nowPos].id)

        editor.apply()
    }

    //스레드 실습
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() //미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null //미디어 플레이어 해제
    }
    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener(){
        binding.songDownIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("message", songs[nowPos].title + "_" + songs[nowPos].singer)
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }
        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }

    }

    private fun moveSong(direct: Int) { // direct는 +1 또는 -1임
        if (nowPos + direct < 0) {
            Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
        }

        else if (nowPos + direct >= songs.size){
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
        }

        else {
            nowPos += direct
            timer.interrupt()
            startTimer()

            mediaPlayer?.release()
            mediaPlayer = null

            setPlayer(songs[nowPos])
        }
    }



    private fun initSong(){
//        if(intent.hasExtra("title") && intent.hasExtra("singer")){
//            song = Song(
//                intent.getStringExtra("title")!!,
//                intent.getStringExtra("singer")!!,
//                intent.getIntExtra("second", 0),
//                intent.getIntExtra("playTime", 0),
//                intent.getBooleanExtra("isPlaying", false),
//                intent.getStringExtra("music")!!
//            )
//        }
//        startTimer()
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    // songId로 position을 얻는 메서드
    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }
    private fun setLike(isLike: Boolean) {
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if (!isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this,music)

        if(song.isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }
        else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus (isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){ // 재생중
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else { // 일시정지
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true) { // 재생 중이 아닐 때에 pause를 하면 에러가 나기 때문에 이를 방지
                mediaPlayer?.pause()
            }
        }
    }
    private fun startStopService() {
        if (isServiceRunning(SongDatabase::class.java)) {
            Toast.makeText(this, "Foreground Service Stopped", Toast.LENGTH_SHORT).show()
            stopService(Intent(this, SongDatabase::class.java))
        }
        else {
            Toast.makeText(this, "Foreground Service Started", Toast.LENGTH_SHORT).show()
            startService(Intent(this, SongDatabase::class.java))
        }
    }

    private fun isServiceRunning(inputClass : Class<SongDatabase>) : Boolean {
        val manager : ActivityManager = getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager

        for (service : ActivityManager.RunningServiceInfo in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (inputClass.name.equals(service.service.className)) {
                return true
            }

        }
        return false
    }
    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime,songs[nowPos].isPlaying)
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
                            binding.songProgressSb.progress = ((mills/playTime) * 100).toInt()
                            //binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
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





