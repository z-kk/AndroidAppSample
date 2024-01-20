package com.example.androidappsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidappsample.ui.main.MainFragment
import com.example.androidappsample.ui.main.SubFragment

class MainActivity : AppCompatActivity(), MainFragment.MainFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onMainFragmentNext() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SubFragment.newInstance("test1", "test2"))
            .addToBackStack(null)
            .commit()
    }
}