package com.example.androidappsample.ui.applinks

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidappsample.R

class AppLinksFragment : Fragment() {
    companion object {
        fun newInstance() = AppLinksFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().intent.data?.let { uri ->
            when (uri.lastPathSegment) {
                "dialog" -> {
                    val builder = AlertDialog.Builder(requireContext())
                    for (key in uri.queryParameterNames) {
                        uri.getQueryParameter(key)?.let {
                            when (key) {
                                "title" -> builder.setTitle(it)
                                "message" -> builder.setMessage(it)
                                else -> {}
                            }
                        }
                    }
                    builder.show()
                }
                else -> {}
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_app_links, container, false)
    }
}