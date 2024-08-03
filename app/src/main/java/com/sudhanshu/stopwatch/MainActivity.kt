package com.sudhanshu.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.sudhanshu.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var stateofbutton = 1 // 1 for play, 2 for pause
    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var handler: Handler = Handler()
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.stopwatchPlay.setOnClickListener {
            if (stateofbutton == 1) {
                stateofbutton = 2
                val pausedrawable = getDrawable(R.drawable.pausebutton)
                binding.stopwatchPlay.setImageDrawable(pausedrawable)
                startTime = SystemClock.uptimeMillis() - elapsedTime
                handler.postDelayed(updateTimerThread, 0)
                isRunning = true
            } else if (stateofbutton == 2) {
                val playDrawable = getDrawable(R.drawable.playbutton)
                binding.stopwatchPlay.setImageDrawable(playDrawable)
                elapsedTime = SystemClock.uptimeMillis() - startTime
                handler.removeCallbacks(updateTimerThread)
                stateofbutton = 1
                isRunning = false
            }
        }

        binding.stopwatchReset.setOnClickListener {
            startTime = 0L
            elapsedTime = 0L
            stateofbutton = 1
            binding.stopwatchChronometre.text = "00:00:00"
            val playDrawable = getDrawable(R.drawable.playbutton)
            binding.stopwatchPlay.setImageDrawable(playDrawable)
            handler.removeCallbacks(updateTimerThread)
            isRunning = false
        }
    }

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            elapsedTime = SystemClock.uptimeMillis() - startTime

            val minutes = (elapsedTime / 1000) / 60
            val seconds = (elapsedTime / 1000) % 60
            val milliseconds = (elapsedTime % 1000)/10

            binding.stopwatchChronometre.text = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)

            if (isRunning) {
                handler.postDelayed(this, 0)
            }
        }
    }
}
