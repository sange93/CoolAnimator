package com.example.myanimator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btHeadAnim.setOnClickListener { startActivity(Intent(this, HeadAnimActivity::class.java)) }
        btVibrateAnim.setOnClickListener { startActivity(Intent(this, VibrateAnimActivity::class.java)) }
        btHeartbeatAnim.setOnClickListener { startActivity(Intent(this, HeartbeatActivity::class.java)) }
    }
}
