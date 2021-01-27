package com.cmdv.feature_main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cmdv.feature_main.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}