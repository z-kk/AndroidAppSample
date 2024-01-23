package com.example.androidappsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidappsample.ui.applinks.AppLinksFragment

class AppLinksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_links)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AppLinksFragment.newInstance())
                .commitNow()
        }
    }
}