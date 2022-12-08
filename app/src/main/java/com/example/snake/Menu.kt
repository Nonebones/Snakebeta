package com.example.snake

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class Menu : AppCompatActivity() {
    var pref: SharedPreferences? = null;
    private val buttonOne by lazy {
        findViewById<ImageButton>(R.id.btnGameOne)
    }
    private val buttonSecond by lazy {
        findViewById<ImageButton>(R.id.btnGameSecond)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        val editor = pref?.edit()
        editor?.putInt("mode", 1)
        editor?.apply()
    }

    fun btnPlay(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun OnClickSettings(view: View) {
        val intent = Intent(this, Settings::class.java)
        startActivity(intent)
    }

    fun OnClickSkins(view: View) {
        val intent = Intent(this, Skins::class.java)
        startActivity(intent)
    }

    fun onClickGameSecond(view: View) {
        buttonOne.setImageResource(R.drawable.food)
        buttonSecond.setImageResource(R.drawable.head)
        val editor = pref?.edit()
        editor?.putInt("mode", 2)
        editor?.apply()
    }
    fun onClickGameOne(view: View) {
        buttonOne.setImageResource(R.drawable.head)
        buttonSecond.setImageResource(R.drawable.food)
        val editor = pref?.edit()
        editor?.putInt("mode", 1)
        editor?.apply()
    }


}