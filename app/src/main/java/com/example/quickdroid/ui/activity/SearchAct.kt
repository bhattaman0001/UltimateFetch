package com.example.quickdroid.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.quickdroid.R
import com.example.quickdroid.databinding.ActivitySearchBinding
import com.example.quickdroid.util.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import java.util.Locale

class SearchAct : AppCompatActivity() {

    var typeOfFileArray: Array<String> = emptyArray()
    var typeOfSingleFileSelected: String = ""
    private var queryName: String = ""
    private lateinit var downloadMediaButton: Button
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        try {
            firebaseAnalytics = Firebase.analytics
            downloadMediaButton = binding.downloadMedia
            downloadMediaButton.setOnClickListener {
                Intent(this, DownloadedMediaActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(this)
                }
            }
            typeOfFileArray = resources.getStringArray(R.array.spinner_options)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, typeOfFileArray)
            binding.typeOfFile.adapter = adapter
            binding.typeOfFile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (position == 0) typeOfSingleFileSelected = ""
                    if (position >= 1) typeOfSingleFileSelected = typeOfFileArray[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // todo: do something when nothing is selected
                }
            }

            binding.findYourFile.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this, Manifest.permission.INTERNET
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                        param(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
                        param(FirebaseAnalytics.Param.ITEM_ID, "Find Your File Button")
                    }
                    queryName = binding.queryName.text.toString()
                    if (queryName != "") {
                        val intent = Intent(this@SearchAct, FileListActivity::class.java)
                        intent.putExtra("queryName", queryName)
                        intent.putExtra("typeOfFileSelected", typeOfSingleFileSelected.lowercase(Locale.getDefault()))
                        startActivity(intent)
                    } else {
                        Snackbar.make(
                            binding.root, "File name must not be empty", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } else Snackbar.make(view, "Open your Internet", Snackbar.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
                param(FirebaseAnalytics.Param.ITEM_ID, "exception_id")
                param(FirebaseAnalytics.Param.ITEM_NAME, "exception_name")
                param(FirebaseAnalytics.Param.CONTENT_TYPE, "exception")
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.drawer_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                closeOptionsMenu()
                return true
            }

            R.id.nav_share_app -> {
                Constants.showComingSoonToast(this@SearchAct)
                return true
            }

            R.id.nav_search_web -> {
                Constants.showComingSoonToast(this@SearchAct)
                return true
            }

            R.id.go_to_saved_file -> {
                Intent(this, SavedFilesActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(this)
                }
                return true
            }

            R.id.go_to_history -> {
                Intent(this, HistoryActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(this)
                }
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}