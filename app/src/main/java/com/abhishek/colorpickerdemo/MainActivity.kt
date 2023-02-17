package com.abhishek.colorpickerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abhishek.colorpicker.ColorPickerDialog
import com.abhishek.colorpickerdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonOpenColorPickerDialog.setOnClickListener {
            val dialog=ColorPickerDialog()
            dialog.show(supportFragmentManager)
        }
    }
}