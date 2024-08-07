package com.sudhanshu.stopwatch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.sudhanshu.stopwatch.databinding.ActivityMainBinding
import nl.joery.animatedbottombar.AnimatedBottomBar

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


        var fmanager = supportFragmentManager
        var tr = fmanager.beginTransaction()
        tr.replace(R.id.frame1,Stopwatch_fragment())
        tr.commit()


        val intent = Intent(this,TimerFragment::class.java)

        binding.bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
               if(lastIndex == 0 && newIndex == 1) {
                   val fragment = TimerFragment()
                   val fragmentManager = supportFragmentManager
                   val transaction = fragmentManager.beginTransaction()
                   transaction.replace(R.id.frame1, fragment)
                   transaction.addToBackStack(null)
                   transaction.commit()
               }
                if(lastIndex == 1 && newIndex == 0) {
                    val fragment = Stopwatch_fragment()
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.frame1, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            }

            // An optional method that will be fired whenever an already selected tab has been selected again.
            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
            }
        })
    }




}

