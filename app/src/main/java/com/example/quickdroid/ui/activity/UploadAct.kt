package com.example.quickdroid.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quickdroid.api_s.fpApi.ApiServiceBuilder
import com.example.quickdroid.commonCodes.CommonCodeFile
import com.example.quickdroid.databinding.ActivityUploadBinding
import com.example.quickdroid.model.PostMethodSendModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class UploadAct : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var fileName: EditText
    private lateinit var fileType: EditText
    private lateinit var fileLink: EditText
    private lateinit var dateAdded: EditText
    private lateinit var fileSize: EditText
    private var isAllFieldFilled = false
    private lateinit var textInputLayout: TextInputLayout
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        calendar = Calendar.getInstance()
        getAllFields(calendar)
    }

    @SuppressLint("LogNotTimber")
    private fun getAllFields(calendar: Calendar) {
        try {
            fileName = binding.fileName2
            fileType = binding.fileType2
            fileSize = binding.fileSize2
            textInputLayout = binding.dateAdded
            dateAdded = binding.editTextDate
            dateAdded.setOnClickListener {
                CommonCodeFile.showDatePickerDialog(this, calendar, dateAdded)
            }
            fileLink = binding.fileLink2
            binding.submitFile.setOnClickListener {
                isAllFieldFilled = CommonCodeFile.checkAllFields(fileName, fileType, fileLink, fileSize, dateAdded)
                if (isAllFieldFilled) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val file = PostMethodSendModel(
                            fileType = "${fileType.text}", fileName = "${fileName.text}", fileLink = "${fileLink.text}", dateAdded = "${dateAdded.text}", timeAgo = "", fileSize = "${fileSize.text} MB"
                        )
                        val response = ApiServiceBuilder.getResponse(file)
                        val responseBodyString = response.body?.string()
                        if (response.isSuccessful && !responseBodyString.isNullOrEmpty()) {
                            withContext(Dispatchers.Main) {
                                val jsonObject = JSONObject(responseBodyString)
                                val msg = jsonObject.optString("msg")
                                val id = jsonObject.optString("id")
                                Toast.makeText(applicationContext, "message = $msg\nid = $id", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, "Unsuccessful response: ${response.code}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }
            }
        } catch (e: Exception) {
            Toast.makeText(this@UploadAct, "Not Sent | Error | ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }
}