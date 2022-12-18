package com.example.animeteka.presentation.activities

import android.os.Bundle
import android.widget.Toast
import com.example.animeteka.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class YoutubeActivity :  YouTubeBaseActivity() {

    private var apiKey: String = "AIzaSyD-TCxwG726Y66KD4D2WOl6Xg_CQh-Bybk"
    private var videoId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        videoId = intent.getStringExtra("youtubeVideoId")!!

        if(videoId == "") videoId = "dQw4w9WgXcQ"

        val YTPlayer = findViewById<YouTubePlayerView>(R.id.YTPlayer)

        YTPlayer.initialize(apiKey, object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                bool: Boolean
            ) {
                player?.loadVideo(videoId)
                player?.play()
            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                result: YouTubeInitializationResult?
            ) {
                Toast.makeText(this@YoutubeActivity, resources.getString(R.string.video_player_failed), Toast.LENGTH_SHORT).show()
            }
        })
    }
}