package com.example.androidappsample

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment

private const val ARG_PARAM1 = "title"
private const val ARG_PARAM2 = "message"

class SampleDialogFragment : DialogFragment() {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param title Dialog title.
         * @param message Dialog message.
         * @return A new instance of fragment SampleDialogFragment.
         */
        @JvmStatic
        fun newInstance(title: String, message: String) =
            SampleDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, title)
                    putString(ARG_PARAM2, message)
                }
            }
    }

    interface AlertDialogFragmentListener {
        fun onDialogPositive(dialog: DialogFragment)
        fun onDialogNegative(dialog: DialogFragment)
        fun onDialogTextReceive(dialog: DialogFragment, text: String)
    }

    private var title: String? = null
    private var message: String? = null

    private lateinit var listener: AlertDialogFragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is AlertDialogFragmentListener) {
            context
        } else if (parentFragment is AlertDialogFragmentListener) {
            parentFragment as AlertDialogFragmentListener
        } else {
            throw RuntimeException("$context or $parentFragment must implement OnSampleDialogFragmentListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_PARAM1)
            message = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = activity?.layoutInflater?.inflate(R.layout.fragment_sample_dialog, null)
        builder.setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                listener.onDialogPositive(this)
                dialogView?.findViewById<EditText>(R.id.dialogEditText)?.text?.let {
                    if (it.isNotEmpty()) {
                        listener.onDialogTextReceive(this, it.toString())
                    }
                }
            }
            .setNegativeButton("Cancel") { _, _ ->
                listener.onDialogNegative(this)
            }
        if (!title.isNullOrEmpty()) {
            builder.setTitle(title)
        }
        if (!message.isNullOrEmpty()) {
            dialogView?.let {
                it.findViewById<TextView>(R.id.dialogTextView).text = message
                it.findViewById<EditText>(R.id.dialogEditText).isVisible = false
            }
        }
        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sample_dialog, container, false)
    }
}