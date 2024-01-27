package com.example.androidappsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidappsample.ui.http.HttpFragment

class HttpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_http)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HttpFragment.newInstance())
                .commitNow()
        }
    }
}