package com.example.snake

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.snake.SnakeCore.isPlay
import com.example.snake.SnakeCore.startTheGame


const val HEAD_SIZE = 50
const val CELLS_ON_FIELD = 18


class MainActivity : AppCompatActivity() {
    private lateinit var layout: LinearLayout
    private val allTail = mutableListOf<PartOfTail>()
    private val allBlocks = mutableListOf<Block>()
    private val food by lazy {
        ImageView(this)
    }
    private val container by lazy {
        findViewById<View>(R.id.container) as ViewGroup
    }
    private val score by lazy {
        findViewById<View>(R.id.score) as TextView
    }
    private val head by lazy {
        ImageView(this)
    }
    private val record by lazy {
        findViewById<View>(R.id.tvRecord) as TextView
    }
    var counter = 0
    var pref: SharedPreferences? = null
    var mode = 1
    var speed = 50
    var skin = 1;
    var headImage = 0
    var bodyImage = 0
    var counterName = "counter"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = findViewById(R.id.linearLayout)
        layout.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                SnakeCore.nextMove = { move(Directions.LEFT) }
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                SnakeCore.nextMove = { move(Directions.RIGHT) }
            }

            override fun onSwipeUp() {
                super.onSwipeUp()
                SnakeCore.nextMove = { move(Directions.UP) }
            }

            override fun onSwipeDown() {
                super.onSwipeDown()
                SnakeCore.nextMove = { move(Directions.DOWN) }
            }
        })
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        mode = pref?.getInt("mode", 1)!!
        speed = pref?.getInt("speed", 50)!!
        counterName = if (mode == 1) "counter" else "counter2";
        counter = pref?.getInt(counterName, 0)!!
        skin = pref?.getInt("skin", 1)!!
        setSkin()
        record.text = counter.toString()

        head.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
        head.setImageResource(headImage)
        container.layoutParams =
            LinearLayout.LayoutParams(HEAD_SIZE * CELLS_ON_FIELD, HEAD_SIZE * 32)
        container.translationX = 90.0F;
        container.translationY = 10.0F;
        startTheGame(speed)
        generateNewFood(container)
        SnakeCore.nextMove = { move(Directions.DOWN) }


    }

    private fun generateNewFood(container: ViewGroup) {

        food.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
        food.setImageResource(R.drawable.food)
        (food.layoutParams as FrameLayout.LayoutParams).topMargin =
            (0 until CELLS_ON_FIELD).random() * HEAD_SIZE
        (food.layoutParams as FrameLayout.LayoutParams).leftMargin =
            (0 until CELLS_ON_FIELD).random() * HEAD_SIZE
        food.scaleX = 0.8F
        food.scaleY = 0.8F
        container.removeView(food)
        container.addView(food)
    }

    private fun generateBlock(container: ViewGroup) {
        val block = ImageView(this)
        val top = (0 until CELLS_ON_FIELD).random() * HEAD_SIZE;
        val left = (0 until CELLS_ON_FIELD).random() * HEAD_SIZE;
        block.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
        block.setImageResource(R.drawable.head)
        (block.layoutParams as FrameLayout.LayoutParams).topMargin = top
        (block.layoutParams as FrameLayout.LayoutParams).leftMargin = left
        block.scaleX = 0.8F
        block.scaleY = 0.8F
        container.removeView(block)
        container.addView(block)
        allBlocks.add(Block(top, left, block));
    }

    private fun checkIfSnakeEatsPerson() {
        if (this.head.left == food.left && this.head.top == food.top) {
            generateNewFood(container)
            addPartOfTail(this.head.top, this.head.left)
            SnakeCore.increaseSpeed();
            score.text = allTail.size.toString();

            if (allTail.size % 5 <= 0 && mode == 2) {
                generateBlock(container)
            }
        }
    }

    private fun addPartOfTail(top: Int, left: Int) {
        val tailPart = drawPartOfTail(top, left)
        allTail.add(PartOfTail(top, left, tailPart))
    }

    private fun drawPartOfTail(top: Int, left: Int): ImageView {
        val tailImage = ImageView(this)
        tailImage.setImageResource(bodyImage)
        tailImage.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
        (tailImage.layoutParams as FrameLayout.LayoutParams).topMargin = top
        (tailImage.layoutParams as FrameLayout.LayoutParams).leftMargin = left
        container.addView(tailImage)
        return tailImage
    }

    fun move(directions: Directions) {
        when (directions) {
            Directions.UP -> {
                (head.layoutParams as FrameLayout.LayoutParams).topMargin -= HEAD_SIZE
                head.rotation = 180.0F
            }
            Directions.LEFT -> {
                (head.layoutParams as FrameLayout.LayoutParams).leftMargin -= HEAD_SIZE
                head.rotation = 90.0F
            }
            Directions.RIGHT -> {
                (head.layoutParams as FrameLayout.LayoutParams).leftMargin += HEAD_SIZE
                head.rotation = 270.0F
            }
            Directions.DOWN -> {
                (head.layoutParams as FrameLayout.LayoutParams).topMargin += HEAD_SIZE
                head.rotation = 360.0F
            }
        }
        runOnUiThread {
            if (checkIfSnakeSmash(head)) {
                isPlay = false
                showScore()
                return@runOnUiThread
            }
            makeTailMove()
            checkIfSnakeEatsPerson()
            container.removeView(head)
            container.addView(head)
        }
    }

    private fun showScore() {
        AlertDialog.Builder(this)
            .setTitle("Your score:  ${allTail.size} foods")
            .setNegativeButton("Replay") { _, _ ->
                this.recreate()
            }
            .setPositiveButton("Exit") { _, _ ->
                val intent = Intent(this, Menu::class.java)
                startActivity(intent)
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun checkIfSnakeSmash(head: ImageView): Boolean {
        for (tailPart in allTail) {
            if (tailPart.left == this.head.left && tailPart.top == this.head.top) {
                return resetGame();
            }
        }
        if (this.head.top < 0
            || this.head.left < 0
            || this.head.top >= HEAD_SIZE * 32
            || this.head.left >= HEAD_SIZE * CELLS_ON_FIELD
        ) {
            return resetGame();
        }

        for (block in allBlocks) {
            if (this.head.left == block.left && this.head.top == block.top) {
                return resetGame();
            }
        }

        return false
    }

    private fun makeTailMove() {
        var tempTailPart: PartOfTail? = null
        for (index in 0 until allTail.size) {
            val tailPart = allTail[index]
            container.removeView(tailPart.ImageView)
            if (index == 0) {
                tempTailPart = tailPart
                allTail[index] =
                    PartOfTail(head.top, head.left, drawPartOfTail(head.top, head.left))
            } else {
                var anotherTempPartOfTail = allTail[index]
                tempTailPart?.let {
                    allTail[index] = PartOfTail(it.top, it.left, drawPartOfTail(it.top, it.left))
                }
                tempTailPart = anotherTempPartOfTail

            }
        }
    }


    private fun resetGame(): Boolean {
        score.text = "0"
        SnakeCore.resetDelay(speed);
        if (counter < allTail.size) saveData(allTail.size)
        return true;
    }

    private fun saveData(res: Int) {
        val editor = pref?.edit()
        editor?.putInt(counterName, res)
        editor?.apply()
    }

    private fun setSkin() {
        when (skin) {
            1 -> {
                headImage = R.drawable.head1
                bodyImage = R.drawable.body
            }
            2 -> {
                headImage = R.drawable.head
                bodyImage = R.drawable.tail
            }
        }
    }


}

enum class Directions {
    UP,
    RIGHT,
    DOWN,
    LEFT
}