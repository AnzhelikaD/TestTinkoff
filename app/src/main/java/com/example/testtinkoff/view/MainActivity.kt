package com.example.testtinkoff.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.testtinkoff.R
import com.example.testtinkoff.utils.DisplayManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.adapter = PointsFragmentPagerAdapter(supportFragmentManager, this)
        tabLayout.setupWithViewPager(viewPager)

        DisplayManager.appDensityDpi = resources.displayMetrics.densityDpi
    }
}