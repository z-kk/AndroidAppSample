package com.example.androidappsample.ui.main

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidappsample.R
import java.lang.RuntimeException

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    interface MainFragmentListener {
        fun onMainFragmentNext()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var listener: MainFragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement MainFragmentListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        view.findViewById<Button>(R.id.nextButton)
            .setOnClickListener { nextFragment() }

        return view
    }

    private fun nextFragment() {
        listener.onMainFragmentNext()
    }
}