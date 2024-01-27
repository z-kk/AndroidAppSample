package com.example.androidappsample.ui.main

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidappsample.R
import com.example.androidappsample.SharedPref
import java.lang.RuntimeException

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    interface MainFragmentListener {
        fun onMainFragmentNext()
        fun onMainFragmentHttp()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var listener: MainFragmentListener
    private lateinit var sharedPref: SharedPref

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement MainFragmentListener")
        }

        sharedPref = SharedPref(context)
        sharedPref.sampleStr =
            when (sharedPref.sampleInt) {
                0 -> "zero"
                1 -> "one"
                2 -> "two"
                else -> ""
            }
        sharedPref.sampleInt++
        if (sharedPref.sampleInt > 2) {
            sharedPref.sampleInt = 0
        }
        sharedPref.save()
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

        view.findViewById<TextView>(R.id.sharedPrefTextView).text = "${sharedPref.sampleInt}, ${sharedPref.sampleStr}"
        view.findViewById<Button>(R.id.nextButton)
            .setOnClickListener { nextFragment() }
        view.findViewById<Button>(R.id.httpButton)
            .setOnClickListener { httpActivity() }

        return view
    }

    private fun nextFragment() {
        listener.onMainFragmentNext()
    }

    private fun httpActivity() {
        listener.onMainFragmentHttp()
    }
}