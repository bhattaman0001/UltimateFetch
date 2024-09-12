package com.example.quickdroid.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.media.ThumbnailUtils
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quickdroid.ui.adapter.VideoAdapter
import com.example.quickdroid.databinding.ActivityDownloadedMediaBinding
import com.example.quickdroid.model.Video
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DownloadedMediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDownloadedMediaBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadedMediaBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        recyclerView = binding.downloadListRv
        recyclerView.layoutManager = LinearLayoutManager(this)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            PackageManager.PERMISSION_GRANTED
        )

        val videosLiveData: LiveData<List<Video>> = getDownloadedVideos()
        videosLiveData.observe(this) {
            if (it.isNullOrEmpty()) {
                binding.noDownloaded.visibility = VISIBLE
                binding.downloadListRv.visibility = GONE
            } else {
                binding.noDownloaded.visibility = GONE
                binding.downloadListRv.visibility = VISIBLE
                videoAdapter = VideoAdapter(this@DownloadedMediaActivity, videosLiveData)
                recyclerView.adapter = videoAdapter
            }
        }
        binding.progressBar.visibility = GONE
    }

    private fun getDownloadedVideos(): LiveData<List<Video>> {
        val videosLiveData = MutableLiveData<List<Video>>()
        val videos: MutableList<Video> = mutableListOf()
        val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val files: Array<File> = downloadsFolder.listFiles()!!
        files.let {
            for (file in it) {
                if (isFile(file.extension)) {
                    val size = getFileSizeInGB(file)
                    val lastModified = getFileLastModifiedDateTime(file)
                    val bMap = ThumbnailUtils.createVideoThumbnail(file.absolutePath, MediaStore.Video.Thumbnails.MICRO_KIND)
                    val video = Video(file.name, file.path, bMap, lastModified, size)
                    videos.add(video)
                }
            }
        }
        videosLiveData.value = videos
        return videosLiveData
    }

    /*private fun getVideoThumbnailPath(videoPath: String): String? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoPath)
        val thumbnail = retriever.getFrameAtTime(1) // Get the thumbnail at the first second
        val thumbnailPath = thumbnail?.let { saveThumbnail(it) }
        retriever.release()
        return thumbnailPath
    }

    private fun saveThumbnail(thumbnail: Bitmap): String {
        val thumbnailFile = File.createTempFile("thumbnail", ".jpg")
        val outputStream = FileOutputStream(thumbnailFile)
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.close()
        return thumbnailFile.absolutePath
    }*/

    private fun isFile(extension: String): Boolean {
        return when (extension.toLowerCase(Locale.ROOT)) {
            "mp4", "avi", "mkv", "wmv", "mov", "flv", "webm", "3gp", "m4v", "mpeg", "mpg" -> true
            "mp3", "wav", "flac", "aac", "wma", "ogg", "m4a", "opus", "aiff" -> true
            "epub", "pdf", "mobi", "azw", "djvu", "html", "txt" -> true
            "apk", "ipa", "exe", "dmg", "appx", "xap", "jar", "deb" -> true
            "zip", "rar", "7z", "tar.gz", "tar", "gz", "bz2", "xz" -> true
            else -> false
        }
    }

    private fun getFileLastModifiedDateTime(file: File): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val lastModified = Date(file.lastModified())
        return dateFormat.format(lastModified)
    }

    private fun getFileSizeInGB(file: File): String {
        val fileSizeInBytes = file.length()
        val fileSizeInKB = fileSizeInBytes / 1024
        val fileSizeInMB = fileSizeInKB / 1024
        val fileSizeInGB = fileSizeInMB / 1024.0
        try {
            return if (fileSizeInGB > 0.0) "%.2f GB".format(fileSizeInGB) else "%.2f MB".format(
                fileSizeInMB
            )
        }catch (e: Exception){
        }
        return ""
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Intent(this, SearchAct::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
            finishAffinity()
        }
    }

}