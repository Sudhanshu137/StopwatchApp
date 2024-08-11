package com.sudhanshu.stopwatch

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sudhanshu.stopwatch.databinding.FragmentStopwatchFragmentBinding
import com.sudhanshu.stopwatch.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentTimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        val innerFragment = timer_inside_fragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.frame2, innerFragment)
            .addToBackStack(null)
            .commit()
        return binding.root
    }

    companion object {

            }
    }
