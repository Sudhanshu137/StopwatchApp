package com.sudhanshu.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
import com.sudhanshu.stopwatch.databinding.FragmentLoadingTimerBinding

class LoadingTimerFragment : Fragment() {

    private lateinit var binding1: FragmentLoadingTimerBinding
    private val handler = Handler()
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
        binding1 = FragmentLoadingTimerBinding.inflate(inflater, container, false)

        // Retrieve the time values from arguments or set to 0
        val second = arguments?.getString("second")?.toIntOrNull() ?: 0
        val min = arguments?.getString("minute")?.toIntOrNull() ?: 0
        val hour = arguments?.getString("hour")?.toIntOrNull() ?: 0

        // Calculate the total countdown time in milliseconds
        countdownTime = (hour * 3600 + min * 60 + second) * 1000L // converting time from previous fragment to milliseconds
        endTime = SystemClock.elapsedRealtime() + countdownTime //end time is the final time set for eg is time given is 10 sec then end time will be systemclock+10sec

        // Start the countdown
        startCountdown()
        // Pause/Resume button logic
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

        // Destroy button logic
        binding1.destroy.setOnClickListener {
            // Navigate back to the timer_inside_fragment
            parentFragmentManager.popBackStack("timer_inside_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        //requireActivity() returns the Activity associated with the Fragment
        //onBackPressedDispatcher is an OnBackPressedDispatcher that manages back button presses.
        // It allows you to register callbacks that are invoked when the back button is pressed.
        //addCallback() is used to register a callback to be invoked when the back button is pressed.
        //viewLifecycleOwner: This is used to automatically manage the lifecycle of the callback.
        //"timer_inside_fragment": This is the name of the back stack entry you want to pop to.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack("timer_inside_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
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
                val now = SystemClock.elapsedRealtime()
                val timeLeft = endTime - now

                if (timeLeft > 0) {
                    val hours = (timeLeft / 3600000).toInt()
                    val minutes = (timeLeft % 3600000 / 60000).toInt()
                    val seconds = (timeLeft % 60000 / 1000).toInt()
                    binding1.chronometre.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                    // Calculate the current progress
                    val elapsedTime = countdownTime - timeLeft
                    binding1.circularProgress.setProgress(elapsedTime.toDouble(), countdownTime.toDouble()-1000.0)

                    // Post next update with minimal delay
                    handler.postDelayed(this, 16) // Update approximately 60 times per second
                } else {
                    binding1.chronometre.text = "00:00:00"
                    // Ensure progress bar is set to maximum
                    binding1.circularProgress.setProgress(countdownTime.toDouble(), countdownTime.toDouble())
                    handler.removeCallbacksAndMessages(null) // Stop further updates
                }
            }
        })
    }
}
