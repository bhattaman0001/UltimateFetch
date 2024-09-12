package com.example.quickdroid.ui.activity

import android.content.Intent
import android.os.Bundle
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
import com.example.quickdroid.model.HistoryModel
import com.example.quickdroid.data.repository.Repository
import com.example.quickdroid.databinding.ActivityHistoryBinding
import com.example.quickdroid.ui.adapter.HistoryAdapter
import com.example.quickdroid.viewmodels.MainViewModel
import com.example.quickdroid.util.Constants
import com.example.quickdroid.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity(), HistoryAdapter.OnDeleteClickListener {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyFileAdapter: HistoryAdapter
    private lateinit var repository: Repository
    lateinit var fapp: FileApplication
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        setUpMainViewModel()
        setUpRecyclerView()
        mainViewModel.getAllHistoryFile.observe(this@HistoryActivity) { history ->
            when (history) {
                is Resource.Success -> {
                    if (history.responseList?.value?.isEmpty() == true) {
                        binding.rvHistorySearch.visibility = GONE
                        binding.noHistorySearch.visibility = VISIBLE
                    } else {
                        binding.rvHistorySearch.visibility = VISIBLE
                        binding.noHistorySearch.visibility = GONE
                        historyFileAdapter = history.responseList?.let { HistoryAdapter(it, this, this) }!!
                        recyclerView.adapter = historyFileAdapter
                    }
                    binding.progressBar.visibility = GONE
                }

                is Resource.Error -> {
                    binding.rvHistorySearch.visibility = GONE
                    binding.noHistorySearch.visibility = VISIBLE
                    binding.progressBar.visibility = GONE
                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = VISIBLE
                }
            }
        }
    }

    private fun setUpMainViewModel() {
        fapp = application as FileApplication
        repository = fapp.hRepo
        mainViewModel = ViewModelProvider(this, MainViewModel.Companion.MainViewModelFactory(application, repository))[MainViewModel::class.java]
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.rvHistorySearch
        recyclerView.layoutManager = LinearLayoutManager(this)
        mainViewModel.getAllHistoryFile()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        val item = menu?.findItem(R.id.nav_home)
        item?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.clear_all -> {
                Constants.deleteAllHistory(this@HistoryActivity)
                Toast.makeText(this, "Delete all saved files successfully", Toast.LENGTH_SHORT).show()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        Intent(this, SearchAct::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

    override fun onDeleteClick(history: HistoryModel) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.historyDelete(history)
        }
    }
}