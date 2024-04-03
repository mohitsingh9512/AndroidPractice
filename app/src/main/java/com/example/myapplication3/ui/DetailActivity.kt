package com.example.myapplication3.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication3.R
import com.example.myapplication3.extensions.log

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null){
            supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_frame, SecondFragment.getInstance())
            .commit()
        }
    }

    override fun onRestart() {
        super.onRestart()
        log(object {}.javaClass.enclosingMethod?.name ?: "")
    }
}