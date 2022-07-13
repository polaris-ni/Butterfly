package com.treasure.test

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.treasure.butterfly.R
import com.treasure.butterfly.toast.LottieToast
import com.treasure.butterfly.toast.LottieToast.Companion.TOAST_INFO

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.test).setOnClickListener {
            LottieToast(TOAST_INFO).show("土司测试多行土司测试多行MultiLine土司测试多行", 10000L)
        }

        findViewById<TextView>(R.id.test2).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}