package com.example.quickdroid.ui.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.quickdroid.R

class VideoPlayerActivity : AppCompatActivity(), MediaController.MediaPlayerControl {
    private lateinit var videoView: VideoView
    private lateinit var mediaController: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_video_player)

        // Initialize VideoView
        videoView = findViewById(R.id.videoView)

        // Set up MediaController
        mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        // Set video path (replace with your video file path or URI)
        val videoPath = intent.getStringExtra("path")
        val videoUri = Uri.parse(videoPath)
        videoView.setVideoURI(videoUri)

        // Start playing the video
        videoView.start()

        // Set up media controller listener
        mediaController.setMediaPlayer(this)
        mediaController.isEnabled = true
    }

    override fun isPlaying(): Boolean {
        return videoView.isPlaying
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun getAudioSessionId(): Int {
        return audioSessionId
    }

    override fun getDuration(): Int {
        return videoView.duration
    }

    override fun getCurrentPosition(): Int {
        return videoView.currentPosition
    }

    override fun pause() {
        videoView.pause()
    }

    override fun start() {
        videoView.start()
    }

    override fun getBufferPercentage(): Int {
        // Not used in this example
        return 0
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun seekTo(pos: Int) {
        videoView.seekTo(pos)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Intent(this, DownloadedMediaActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(this)
            finishAffinity()
        }
    }
}
