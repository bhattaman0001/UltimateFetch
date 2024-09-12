package com.example.quickdroid.commonCodes

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.widget.EditText
import java.util.Locale

class CommonCodeFile {

    companion object {
        fun showDatePickerDialog(context: Context, calendar: Calendar, view: EditText) {
            val datePickerDialog = DatePickerDialog(
                context, { _, year, month, dayOfMonth ->
                    // Update the calendar with the selected date
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    // Format the selected date and set it in the EditText
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val formattedDate = dateFormat.format(calendar.time)
                    view.setText(formattedDate)
                }, calendar.get(Calendar.YEAR), // Initial year selection
                calendar.get(Calendar.MONTH), // Initial month selection
                calendar.get(Calendar.DAY_OF_MONTH) // Initial day selection
            )
            // Show the date picker dialog
            datePickerDialog.show()
        }


        @SuppressLint("UseCompatLoadingForDrawables")
        fun checkAllFields(fileName: EditText, fileType: EditText, fileLink: EditText, fileSize: EditText, dateAdded: EditText): Boolean {
            if (fileName.length() == 0) {
                fileName.error = "Required Field"
                return false
            }
            if (fileType.length() == 0) {
                fileType.error = "Required Field"
                return false
            }
            if (fileLink.length() == 0) {
                fileLink.error = "Required Field"
                return false
            }
            if (fileSize.length() == 0) {
                fileSize.error = "Required Field"
                return false
            }
            if (dateAdded.length() == 0) {
                dateAdded.error = "Required Field"
                return false
            }
            return true
        }
    }
}