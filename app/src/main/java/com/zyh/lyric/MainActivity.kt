package com.zyh.lyric

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
/**
 *Time:2019/3/28
 *Author:ZYH
 *Description:展示的Activity
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ltv = findViewById<LyricTextView>(R.id.ltv)
        ltv.start(2000)
    }
}
