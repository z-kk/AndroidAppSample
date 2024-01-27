package com.example.androidappsample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidappsample.ui.main.InitFragment
import com.example.androidappsample.ui.main.MainFragment
import com.example.androidappsample.ui.main.SubFragment

class MainActivity : AppCompatActivity(),
    MainFragment.MainFragmentListener,
    InitFragment.InitFragmentListener,
    SubFragment.SubFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, InitFragment.newInstance())
                .commitNow()
        }
    }

    override fun onMainFragmentNext() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SubFragment.newInstance("test1", "test2"))
            .addToBackStack(null)
            .commit()
    }

    override fun onMainFragmentHttp() {
        startActivity(Intent(this, HttpActivity::class.java))
    }

    override fun onInitFragmentFinish() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment.newInstance())
            .commit()
    }

    override fun onSubFragmentBack() {
        supportFragmentManager.popBackStack()
    }
}