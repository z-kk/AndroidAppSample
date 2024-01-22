package com.example.androidappsample.ui.main

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidappsample.R
import com.example.androidappsample.SampleDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubFragment : Fragment(), SampleDialogFragment.AlertDialogFragmentListener {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    interface SubFragmentListener {
        fun onSubFragmentBack()
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: SubViewModel
    private lateinit var listener: SubFragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SubFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement SubFragmentListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewModel = ViewModelProvider(this)[SubViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sub, container, false)

        view.findViewById<Button>(R.id.backButton)
            .setOnClickListener { back() }
        view.findViewById<Button>(R.id.dialogButton)
            .setOnClickListener { showDialog() }
        view.findViewById<Button>(R.id.otherDialogButton)
            .setOnClickListener {
                SampleDialogFragment.newInstance(viewModel.dialogTitle, viewModel.dialogMessage)
                    .show(childFragmentManager, null)
            }

        if (viewModel.showingDialog) {
            showDialog()
        }

        return view
    }

    override fun onDialogPositive(dialog: DialogFragment) {
        viewModel.dialogTitle = ""
        viewModel.dialogMessage = ""
    }

    override fun onDialogNegative(dialog: DialogFragment) {
    }

    override fun onDialogTextReceive(dialog: DialogFragment, text: String) {
        if (text.isNotEmpty()) {
            viewModel.dialogTitle = text + "_title"
            viewModel.dialogMessage = text
        }
    }

    private fun back() {
        listener.onSubFragmentBack()
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(param1)
            .setMessage(param2)
            .setPositiveButton("OK") { _, _ ->
                viewModel.showingDialog = false
            }
            .setOnCancelListener {
                viewModel.showingDialog = false
            }
            .show()
        viewModel.showingDialog = true
    }
}