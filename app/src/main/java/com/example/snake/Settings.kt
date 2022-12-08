package com.example.snake

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar

class Settings : AppCompatActivity() {
    var pref: SharedPreferences? = null;
    var speed = 50
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        speed = pref?.getInt("speed", 50)!!
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        seekBar.progress = speed;
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val editor = pref?.edit()
                editor?.putInt("speed", progress)
                editor?.apply()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {    }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {     }
        })
    }

    fun onClickReturn(view: View) {
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }

    fun onReset(view: View) {
        val editor = pref?.edit()
        editor?.putInt("counter", 0)
        editor?.putInt("counter2", 0)
        editor?.apply()
    }
}