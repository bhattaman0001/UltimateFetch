package com.example.quickdroid.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quickdroid.FileApplication
import com.example.quickdroid.R
import com.example.quickdroid.model.FileDetailedModel
import com.example.quickdroid.data.repository.Repository
import com.example.quickdroid.databinding.ActivitySavedFilesBinding
import com.example.quickdroid.ui.adapter.SavedFileAdapter
import com.example.quickdroid.viewmodels.MainViewModel
import com.example.quickdroid.util.Constants
import com.example.quickdroid.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("LogNotTimber")
class SavedFilesActivity : AppCompatActivity(), SavedFileAdapter.onClickListener {
    private lateinit var binding: ActivitySavedFilesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var savedFileAdapter: SavedFileAdapter
    private lateinit var repository: Repository
    private lateinit var mainViewModel: MainViewModel
    private lateinit var fApp: FileApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedFilesBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        setUpMainViewModel()
        setUpRecyclerView()
        mainViewModel.getAllSavedFile.observe(this@SavedFilesActivity) { savedFile ->
            when (savedFile) {
                is Resource.Success -> {
                    if (savedFile.responseList?.value?.isEmpty() == true) {
                        binding.rvSavedFile.visibility = GONE
                        binding.noSavedFile.visibility = VISIBLE
                    } else {
                        binding.rvSavedFile.visibility = VISIBLE
                        binding.noSavedFile.visibility = GONE
                        savedFileAdapter = savedFile.responseList?.let { SavedFileAdapter(it, this, this) }!!
                        recyclerView.adapter = savedFileAdapter
                    }
                    binding.progressBar.visibility = GONE
                }

                is Resource.Error -> {
                    binding.rvSavedFile.visibility = GONE
                    binding.noSavedFile.visibility = VISIBLE
                    binding.progressBar.visibility = GONE
                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = VISIBLE
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.rvSavedFile
        recyclerView.layoutManager = LinearLayoutManager(this)
        mainViewModel.getAllSavedFile()
    }

    private fun setUpMainViewModel() {
        fApp = application as FileApplication
        repository = fApp.sRepo
        mainViewModel = ViewModelProvider(this, MainViewModel.Companion.MainViewModelFactory(application, repository))[MainViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                Intent(this, SearchAct::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    startActivity(this)
                }
                finishAffinity()
                return true
            }

            R.id.clear_all -> {
                Constants.deleteAllSavedFile(this@SavedFilesActivity)
                Toast.makeText(this, "Delete all saved files successfully", Toast.LENGTH_SHORT).show()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDeleteClick(file: FileDetailedModel) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteSavedFile(file)
        }
    }

    override fun isVisible(id: Int?) {
        val sharedPreferences = getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        Log.d("aman", "4: $id")
        editor.putString("  idOfItem", "$id")
        editor.apply()
    }
}