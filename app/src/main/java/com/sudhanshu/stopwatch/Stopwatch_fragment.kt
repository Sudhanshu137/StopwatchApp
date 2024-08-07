package com.sudhanshu.stopwatch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import com.sudhanshu.stopwatch.R.*
import com.sudhanshu.stopwatch.databinding.FragmentStopwatchFragmentBinding
import nl.joery.animatedbottombar.AnimatedBottomBar

class Stopwatch_fragment : Fragment() {
    private lateinit var binding: FragmentStopwatchFragmentBinding
    private var stateofbutton = 1 // 1 for play, 2 for pause
    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var handler: Handler = Handler()
    private var isRunning = false


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStopwatchFragmentBinding.inflate(inflater, container, false)
        //write code for object in fragment

        binding.stopwatchPlay.setOnClickListener {
            if (stateofbutton == 1) {
                stateofbutton = 2
                val pausedrawable = ContextCompat.getDrawable(requireContext(),R.drawable.pausebutton)
                binding.stopwatchPlay.setImageDrawable(pausedrawable)
                startTime = SystemClock.uptimeMillis() - elapsedTime
                handler.postDelayed(updateTimerThread, 0)
                isRunning = true
            } else if (stateofbutton == 2) {
                val playDrawable = ContextCompat.getDrawable(requireContext(), drawable.playbutton)
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
            val playDrawable = getDrawable(requireContext(),drawable.playbutton)
            binding.stopwatchPlay.setImageDrawable(playDrawable)
            handler.removeCallbacks(updateTimerThread)
            isRunning = false
        }


        return binding.root



    }
    companion object {

    }


    val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            elapsedTime = SystemClock.uptimeMillis() - startTime

            val minutes = (elapsedTime / 1000) / 60
            val seconds = (elapsedTime / 1000) % 60
            val milliseconds = (elapsedTime % 1000) / 10

            binding.stopwatchChronometre.text =
                String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)

            if (isRunning) {
                handler.postDelayed(this, 0)
            }

        }

    }
}
