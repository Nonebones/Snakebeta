package com.example.snake



object SnakeCore {
    private var delay: Long = 0;
    var isPlay = true
    var nextMove: () -> Unit = {}
    private val thread: Thread
    init {
        thread = Thread(Runnable {
            while (true) {
                Thread.sleep(delay)
                if (isPlay) {
                    nextMove()
                }
            }
        })
        thread.start()
    }
    fun startTheGame(speed: Int) {
        delay = (200 - speed).toLong();
        isPlay = true
    }

    fun increaseSpeed() {
        if (delay > 20) delay -=2;
    }

    fun resetDelay(speed: Int) {
        delay = (200 - speed).toLong();
    }
}