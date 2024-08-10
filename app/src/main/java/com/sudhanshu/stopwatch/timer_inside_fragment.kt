package com.sudhanshu.stopwatch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sudhanshu.stopwatch.databinding.FragmentTimerBinding
import com.sudhanshu.stopwatch.databinding.FragmentTimerInsideFragmentBinding

class timer_inside_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var selectedtextview: TextView //check for which one is selected hour,minute or second
    private lateinit var binding: FragmentTimerInsideFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTimerInsideFragmentBinding.inflate(inflater, container, false)

        selectedtextview = binding.second

        changetextcolor(selectedtextview)


        binding.second.setOnClickListener {
            selectedtextview = binding.second
            updateNumpadState()
            changetextcolor(selectedtextview)

        }

        binding.hour.setOnClickListener {
            selectedtextview = binding.hour
            updateNumpadState()
            changetextcolor(selectedtextview)

        }
        binding.minute.setOnClickListener {
            selectedtextview = binding.minute
            updateNumpadState()
            changetextcolor(selectedtextview)

        }
        binding.clear.setOnClickListener {
            clearValues()
        }
        binding.imageView.setOnClickListener {
            val bundle = Bundle()// passing data to next fragment ,, (use viewmodel to pass data )
            bundle.putString("second",binding.second.text.toString())
            bundle.putString("minute",binding.minute.text.toString())
            bundle.putString("hour",binding.hour.text.toString())

            val fragment = LoadingTimerFragment()
            fragment.arguments = bundle
            val fragmentmanager = parentFragmentManager//to get back to parent fragment i.e timerfragment
            val transaction = fragmentmanager.beginTransaction()
            transaction.replace(R.id.frame2,fragment)
            transaction.addToBackStack("timer_inside_fragment")
            transaction.commit()


        }

        binding.numpad1.setOnClickListener {
            enternumber("1")
        }
        binding.numpad2.setOnClickListener {
            enternumber("2")
        }
        binding.numpad3.setOnClickListener {
            enternumber("3")
        }
        binding.numpad4.setOnClickListener {
            enternumber("4")
        }
        binding.numpad5.setOnClickListener {
            enternumber("5")
        }
        binding.numpad6.setOnClickListener {
            enternumber("6")
        }
        binding.numpad7.setOnClickListener {
            enternumber("7")
        }
        binding.numpad8.setOnClickListener {
            enternumber("8")
        }
        binding.numpad9.setOnClickListener {
            enternumber("9")
        }
        binding.numpad0.setOnClickListener {
            enternumber("0")
        }


        return binding.root
    }

    fun changetextcolor(view: TextView) {
        binding.hour.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        binding.minute.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        binding.second.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        val color = ContextCompat.getColor(requireContext(), R.color.my_color)
        view.setTextColor(color)

    }

    fun enternumber(number: String) {
        val currentText = selectedtextview.text.toString()
        val newText = (currentText + number).takeLast(2) // Keep only the last 2 digits
        selectedtextview.text = newText
        handleOverflow()
        updateNumpadState()
        checkForPlayButton()

    }

    private fun handleOverflow() {
        var secondsText = binding.second.text.toString().toIntOrNull() ?: 0
        var minutesText = binding.minute.text.toString().toIntOrNull() ?: 0
        var hoursText = binding.hour.text.toString().toIntOrNull() ?: 0

        // Handle overflow for seconds
        if (secondsText > 59) {
            val extraMinutes = secondsText / 60
            secondsText = secondsText % 60
            minutesText += extraMinutes
        }

        // Handle overflow for minutes
        if (minutesText > 59) {
            val extraHours = minutesText / 60
            minutesText = minutesText % 60
            hoursText += extraHours
        }

        // Update the TextViews with the new values
        binding.second.text = String.format("%02d", secondsText)
        binding.minute.text = String.format("%02d", minutesText)
        binding.hour.text = String.format("%02d", hoursText)
    }

    private fun clearValues() {
        // Ensure selectedTextView is not null
        selectedtextview?.let { view ->
            // Get the current text and convert it to an integer
            val currentText = view.text.toString()
            if (currentText.isNotEmpty()) {
                // Clear only the ones place
                val newText = if (currentText.length == 1) {
                    "0" // If only one digit is present, set it to "0"
                } else {
                    currentText.dropLast(1) // Drop the last character
                    //originalString is "123".
                    //dropLast(1) removes the last character ('3'), resulting in "12".
                }
                // Update the TextView with the new text
                view.text = String.format("%02d", newText.toIntOrNull() ?: 0)
            }

            // Update the numpad state
            updateNumpadState()
            checkForPlayButton()
        }
    }

    private fun updateNumpadState() {
        val text = selectedtextview.text.toString()
        val tenthsPlace =
            text.take(1).toIntOrNull()  //if text is "12", then text.take(1) will give "1".

        // Disable the numpad buttons if the tenths place is not zero

        if (tenthsPlace != 0) {
            binding.numpad0.isEnabled = false
            binding.numpad1.isEnabled = false
            binding.numpad2.isEnabled = false
            binding.numpad3.isEnabled = false
            binding.numpad4.isEnabled = false
            binding.numpad5.isEnabled = false
            binding.numpad6.isEnabled = false
            binding.numpad7.isEnabled = false
            binding.numpad8.isEnabled = false
            binding.numpad9.isEnabled = false
        } else {
            binding.numpad0.isEnabled = true
            binding.numpad1.isEnabled = true
            binding.numpad2.isEnabled = true
            binding.numpad3.isEnabled = true
            binding.numpad4.isEnabled = true
            binding.numpad5.isEnabled = true
            binding.numpad6.isEnabled = true
            binding.numpad7.isEnabled = true
            binding.numpad8.isEnabled = true
            binding.numpad9.isEnabled = true

        }
    }

    fun checkForPlayButton() {
        // Convert the text from each TextView to an integer, or default to 0 if it's empty or not a number
        val hourValue = binding.hour.text.toString().toIntOrNull() ?: 0
        val minuteValue = binding.minute.text.toString().toIntOrNull() ?: 0
        val secondValue = binding.second.text.toString().toIntOrNull() ?: 0

        // Check if any of the fields contain a non-zero value
        if (hourValue != 0 || minuteValue != 0 || secondValue != 0) {
            binding.imageView.visibility = View.VISIBLE
        } else {
            binding.imageView.visibility = View.INVISIBLE
        }
    }



    companion object {
    }
}
