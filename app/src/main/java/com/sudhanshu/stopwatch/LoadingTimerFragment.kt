package com.sudhanshu.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.sudhanshu.stopwatch.databinding.FragmentLoadingTimerBinding

class LoadingTimerFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var  binding1  :  FragmentLoadingTimerBinding
    private var handler: Handler = Handler()
    private var endTime: Long = 0
    private var remainingTime: Long = 0
    private var stateOfButton = 2 // 1 for play, 2 for pause
    private var countdownTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding1 = FragmentLoadingTimerBinding.inflate(inflater,container,false)
        val second = arguments?.getString("second")?.toIntOrNull() ?: 0
        val min = arguments?.getString("minute")?.toIntOrNull() ?: 0
        val hour = arguments?.getString("hour")?.toIntOrNull() ?: 0


        countdownTime = (hour * 3600 + min * 60 + second) * 1000L
        endTime = SystemClock.elapsedRealtime() + countdownTime

        startCountdown()

        binding1.loadingpause.setOnClickListener {
            if (stateOfButton == 2) { // Pause state
                stateOfButton = 1
                val playDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.playbutton)
                binding1.loadingpause.setImageDrawable(playDrawable)
                pauseCountdown()
            } else { // Play state
                stateOfButton = 2
                val pauseDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.pausebutton)
                binding1.loadingpause.setImageDrawable(pauseDrawable)
                resumeCountdown()
            }
        }
        binding1.destroy.setOnClickListener{
            val fragment = timer_inside_fragment()
            val fragmentmanager = parentFragmentManager
            val transaction = fragmentmanager.beginTransaction()
            transaction.replace(R.id.frame2,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
       return binding1.root
    }
    private fun pauseCountdown() {
        handler.removeCallbacksAndMessages(null)
        remainingTime = endTime - SystemClock.elapsedRealtime()
    }
    private fun resumeCountdown() {
        endTime = SystemClock.elapsedRealtime() + remainingTime
        startCountdown()
    }


    private fun startCountdown() {
        handler.post(object : Runnable {
            override fun run() {
                val timeLeft = endTime - SystemClock.elapsedRealtime()

                if (timeLeft > 0) {
                    val hours = (timeLeft / 3600000).toInt()
                    val minutes = (timeLeft % 3600000 / 60000).toInt()
                    val seconds = (timeLeft % 60000 / 1000).toInt()
                    binding1.chronometre.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    handler.postDelayed(this, 1000)
                } else {
                    binding1.chronometre.text = "00:00:00"
                }
            }
        })
    }


    companion object {



    }
}