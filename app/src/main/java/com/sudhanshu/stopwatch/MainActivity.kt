package com.sudhanshu.stopwatch

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
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

        // Make status bar transparent  and not overlap navigation buttons
      window.apply {
            // Clear the previous flags
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // Apply new flags to make the status bar transparent
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            // Set status bar color to transparent
            statusBarColor = android.graphics.Color.TRANSPARENT
                   }


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Applying window insets listener to root view to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom
            )
            WindowInsetsCompat.CONSUMED
        }


        var fmanager = supportFragmentManager
        var tr = fmanager.beginTransaction()
        tr.replace(R.id.frame1,Stopwatch_fragment())
        tr.commit()


        val intent = Intent(this,TimerFragment::class.java)
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.frame1)
            if(fragment is Stopwatch_fragment)
            {
               binding.bottomBar.selectTabAt(0)
            }
            else if(fragment is TimerFragment)
            {
                binding.bottomBar.selectTabAt(1)
            }

        }

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

