package com.rizky.submission.last.moviecatalogue.helper

import android.content.Context
import android.text.InputType.TYPE_CLASS_TEXT
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.rizky.submission.last.moviecatalogue.R
import java.text.SimpleDateFormat
import java.util.*

object FunctionHelper {

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun showAlertDialog(context: Context, title: String, message: String){
        val dialog = AlertDialog.Builder(context)
        dialog
            .setMessage(message)
            .setTitle(title)
            .apply {
                setPositiveButton(
                    R.string.ok
                ) { _, _ ->
                    // User clicked OK button
                }
            }
            .setCancelable(true)
            .create()
            .show()
    }

    fun showSearchDialog(context: Context, title: String){
        val dialog = AlertDialog.Builder(context)
        val input = EditText(context)
        input.inputType = (TYPE_CLASS_TEXT)
        dialog
            .setTitle(title)
            .setView(input)
            .apply {
                setPositiveButton(
                    R.string.ok
                ) { _, _ ->
                    val query = input.text
                    Toast.makeText(context, query, Toast.LENGTH_LONG).show()
                }
                setNegativeButton(
                    R.string.cancel
                ) { _, _ ->

                }
            }
            .setCancelable(false)
            .create()
            .show()


    }

}