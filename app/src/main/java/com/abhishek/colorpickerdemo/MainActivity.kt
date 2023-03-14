package com.abhishek.colorpickerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abhishek.colorpicker.ColorPickerDialog
import com.abhishek.colorpickerdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonOpenColorPickerDialog.setOnClickListener {
            val dialog = ColorPickerDialog()
            dialog.setOnOkCancelListener { isOk, color ->
            }
            dialog.show(supportFragmentManager)
        }
    }
}