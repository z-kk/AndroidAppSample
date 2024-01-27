package com.example.androidappsample.ui.http

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidappsample.HttpUtil
import com.example.androidappsample.R

class HttpFragment : Fragment() {
    companion object {
        fun newInstance() = HttpFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_http, container, false)

        val hostEdit = view.findViewById<EditText>(R.id.hostEditText)
        val pathEdit = view.findViewById<EditText>(R.id.pathEditText)
        val queryEdit = view.findViewById<EditText>(R.id.queryEditText)
        hostEdit.setText("10.0.2.2:8080")
        pathEdit.setText("android")
        view.findViewById<Button>(R.id.getButton)
            .setOnClickListener {
                val httpUtil = HttpUtil(hostEdit.text.toString())
                val res = httpUtil.getRequest(pathEdit.text.toString(), queryEdit.text.toString())
                AlertDialog.Builder(requireContext())
                    .setTitle("Get result")
                    .setMessage(res)
                    .show()
            }
        view.findViewById<Button>(R.id.postButton)
            .setOnClickListener {
                val httpUtil = HttpUtil(hostEdit.text.toString())
                val res = httpUtil.postRequest(pathEdit.text.toString(), queryEdit.text.toString())
                AlertDialog.Builder(requireContext())
                    .setTitle("Post result")
                    .setMessage(res)
                    .show()
            }

        return view
    }
}