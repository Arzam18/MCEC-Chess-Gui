package com.tournament.chess

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this).apply {
            text = "Chess Tournament App\nEngine Manager Ready!"
            textSize = 22f
            setPadding(60, 100, 60, 60)
        }
        setContentView(textView)
    }
}
