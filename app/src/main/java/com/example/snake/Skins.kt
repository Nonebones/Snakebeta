package com.example.snake

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class Skins : AppCompatActivity() {
    var pref: SharedPreferences? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skins)
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        val skin = pref?.getInt("skin", 1)!!
        setChosenSkin(skin)
    }

    fun onClickReturn(view: View) {
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }

    fun onSetSkin1(view: View) {
        val editor = pref?.edit()
        editor?.putInt("skin", 1)
        editor?.apply()
        setChosenSkin(1)
    }
    fun onSetSkin2(view: View) {
        val editor = pref?.edit()
        editor?.putInt("skin", 2)
        editor?.apply()
        setChosenSkin(2)
    }

    private fun setChosenSkin(skin: Int) {
        val currentSkin = findViewById<ImageView>(R.id.currentSkin);

        when(skin) {
            1 -> {
                currentSkin.setImageResource(R.drawable.head1)
            }
            2 -> {
                currentSkin.setImageResource(R.drawable.head)
            }
        }
    }
}