package com.example.quickdroid.ui.adapter

import android.annotation.*
import android.app.AlertDialog
import android.content.*
import android.view.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.example.quickdroid.databinding.*
import com.example.quickdroid.model.*
import com.example.quickdroid.ui.activity.*
import com.google.android.material.snackbar.*
import com.example.quickdroid.model.HistoryModel


@SuppressLint("NotifyDataSetChanged")
class HistoryAdapter(
    history: LiveData<List<HistoryModel>>,
    private val context: Context,
    private val onDeleteClickListener: OnDeleteClickListener
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var fileHistory: List<HistoryModel> = emptyList()
    private lateinit var binding: HistoryItemBinding

    init {
        history.observeForever { updatedFiles ->
            fileHistory = updatedFiles
            notifyDataSetChanged()
        }
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(history: HistoryModel)
    }

    inner class ViewHolder(private val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnLongClickListener {

        init {
            binding.root.setOnLongClickListener(this)
        }

        private lateinit var history: HistoryModel

        fun bind(history: HistoryModel) {
            this.history = history
            binding.historyTxt.text = history.queryName
            binding.historyType.text = history.queryType
            binding.root.setOnClickListener {
                if (binding.historyTxt.text != "") {
                    Intent(context, FileListActivity::class.java).apply {
                        putExtra("queryName", history.queryName)
                        putExtra("typeOfFileSelected", history.queryType)
                        context.startActivity(this)
                    }
                } else {
                    Snackbar.make(
                        binding.root,
                        "File name must not be empty",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        override fun onLongClick(view: View?): Boolean {
            AlertDialog.Builder(context)
                .setTitle("Delete History")
                .setMessage("Are you sure you want to delete it?")
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ ->
                    onDeleteClickListener.onDeleteClick(history)
                }
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = fileHistory[position]
        holder.bind(history)
    }

    override fun getItemCount(): Int {
        return fileHistory.size
    }
}