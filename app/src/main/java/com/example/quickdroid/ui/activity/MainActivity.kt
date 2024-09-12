package com.example.quickdroid.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.quickdroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        /*val videoView = findViewById<View>(R.id.videoViewBackground) as VideoView
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.video3)
        videoView.setVideoURI(uri)
        videoView.alpha = 1F
        videoView.setOnPreparedListener { it.isLooping = true }
        videoView.start()*/

        val searchButton = binding.searchFiles
        val uploadButton = binding.uploadFiles
        searchButton.setOnClickListener {
            Intent(this, SearchAct::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(this)
            }
        }
        uploadButton.setOnClickListener {
            Intent(this, UploadAct::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(this)
            }
        }
    }
}