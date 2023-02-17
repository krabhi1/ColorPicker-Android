package com.abhishek.colorpicker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.abhishek.colorpicker.databinding.FragmentColorPickerDialogBinding

class ColorPickerDialog : DialogFragment() {
    private lateinit var binding:FragmentColorPickerDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentColorPickerDialogBinding.inflate(layoutInflater)
        return binding.root
    }
    fun show(fragmentManager: FragmentManager){
        show(fragmentManager,"ColorPickerDialog")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        requireDialog().window?.setLayout((6 * width) / 7, height * 80 / 100)
    }
}