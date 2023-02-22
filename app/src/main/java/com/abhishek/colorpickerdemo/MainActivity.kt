package com.abhishek.colorpickerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abhishek.colorpicker.ColorPickerDialog
import com.abhishek.colorpicker.colorInfo
import com.abhishek.colorpickerdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val dialog = ColorPickerDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonOpenColorPickerDialog.setOnClickListener {
            println(dialog.getColor().colorInfo())
            dialog.setOnOkCancelListener { isOk, color ->
                println("color ${color.colorInfo()}")
            }
            dialog.show(supportFragmentManager)
        }
    }
}