package com.example.myanimator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_vibrate_anim.*

/**
 * 抖动动画
 * @author ssq
 */
class VibrateAnimActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vibrate_anim)
        btStart.setOnClickListener { AnimatorUtil.startErrorAnim(etContent) }
    }
}
