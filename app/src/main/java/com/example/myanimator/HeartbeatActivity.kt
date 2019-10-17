package com.example.myanimator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_heartbeat.*

/**
 * 心跳动画
 * @author ssq
 */
class HeartbeatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heartbeat)
        AnimatorUtil.startEnlargeHeart(ivHead)
    }
}
