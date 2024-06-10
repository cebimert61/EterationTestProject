package com.example.eterationtestproject.view

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import com.example.eterationtestproject.R

class FilterDialogFragment(private val onFilterApplied: (Int) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_filter, null)

            builder.setView(view)
                .setPositiveButton(R.string.apply) { dialog, id ->
                    val radioGroup = view.findViewById<android.widget.RadioGroup>(R.id.filterRadioGroup)
                    val selectedId = radioGroup.checkedRadioButtonId
                    onFilterApplied(selectedId)
                }
                .setNegativeButton(R.string.cancel) { dialog, id ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
