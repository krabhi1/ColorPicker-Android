package com.abhishek.colorpicker

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.abhishek.colorpicker.databinding.FragmentColorPickerDialogBinding
import kotlinx.coroutines.*
import java.io.*


class ColorPickerDialog : DialogFragment() {
    private lateinit var binding: FragmentColorPickerDialogBinding
    private var currentColor = MutableLiveData(Color.argb(100, 255, 0, 0))
    private var okCancelListener: OkCancelFun? = null
    private var colorHolder = MutableLiveData(ColorHolder())
    private lateinit var colorHolderFile: File
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentColorPickerDialogBinding.inflate(layoutInflater)
        initialize()

        initFileIO()
        test()
        return binding.root
    }

    private fun test() {


    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, "ColorPickerDialog")
    }

    fun getColor() = currentColor.value!!
    fun setColor(color: Int) {
        binding.apply {
            //update hue
            val hsv = color.toHSV()
            hueSlider.setHue(hsv[0].toInt())
            //update alpha
            alphaSlider.setAlpha(Color.alpha(color))
            //update color compose
            colorComposer.setColor(color)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        requireDialog().window?.setLayout((6 * width) / 7, height * 80 / 100)
    }
    private fun initialize(){
        binding.apply {
            colorHolder.observe(this@ColorPickerDialog) {
                //update colorBox
                println("colorHolder update $it")
                colorBox.setColorHolder(it)
            }
            setColor(currentColor.value!!)
            alphaSlider.setOnChangeListener {
                val newColor =
                    Utils.colorWithAlpha(alphaSlider.getAlphaValue(), currentColor.value!!)
                currentColor.value = newColor
            }
            hueSlider.setOnChangeListener {
                colorComposer.setComposeColor(hueSlider.getColor())
            }
            colorComposer.setColorChangeListener {
                val alpha = Color.alpha(currentColor.value!!)
                val newColor = Utils.colorWithAlpha(alpha, colorComposer.getColor())
                currentColor.value = newColor
                alphaSlider.setColor(newColor)
            }
            currentColor.observe(viewLifecycleOwner) { color ->
                color?.let {
                    colorView.setColor(it)
                }
            }
            //button
            buttonCancel.setOnClickListener {
                onClickButton(false)
            }
            buttonOk.setOnClickListener {
                onClickButton(true)
            }
            //colorBox
            colorBox.setColorClickListener {
                setColor(it)
            }
        }
    }

    private fun initFileIO() {
        colorHolderFile = File(requireActivity().filesDir, "colorPicker.color")
        lifecycleScope.launch(Dispatchers.IO) {
            val obj = ColorDiskIO(colorHolderFile, lifecycleScope).getColorHolder()
            obj?.let {
                colorHolder.postValue(it)
            }
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun onClickButton(isOk: Boolean) {
        //save color to file
        if (isOk) {
            GlobalScope.launch(Dispatchers.IO) {
                val newColorHolder = colorHolder.value!!.clone()
                newColorHolder.push(currentColor.value!!)
                ColorDiskIO(colorHolderFile, lifecycleScope).setColorHolder(newColorHolder)
            }
        }
        okCancelListener?.invoke(isOk, currentColor.value!!)
        dismiss()
    }

    fun setOnOkCancelListener(callback: OkCancelFun) {
        okCancelListener = callback
    }
}

typealias OkCancelFun = (isOk: Boolean, color: Int) -> Unit

class ColorHolder : Serializable {
    val colors = IntArray(10)
    override fun toString(): String {
        var arr = "["
        colors.forEach {
            arr += "$it "
        }
        arr += "]"
        return arr
    }

    fun clone(): ColorHolder {
        val ch = ColorHolder()
        for (i in colors.indices) {
            ch.colors[i] = colors[i]
        }
        return ch
    }

    fun push(color: Int) {
        //only if unique color
        var isNew = true
        for (i in colors.indices) {
            if (colors[i] == color) {
                isNew = false
                break
            }
        }
        if (isNew) {
            colors.swapFromBack()
            colors[0] = color
        }
    }
}

class ColorDiskIO(private val file: File, private val scope: CoroutineScope) {
    init {
        file.createNewFile()
    }

    suspend fun getColorHolder(): ColorHolder? {
        val colorHolder = withContext(Dispatchers.IO) {
            var obj: ColorHolder? = null
            try {
                val input = ObjectInputStream(FileInputStream(file))
                try {
                    obj = input.readObject() as ColorHolder
                } catch (e: Exception) {
                    println(e)
                } finally {
                    input.close()
                }
            } catch (e: Exception) {
                println(e)
            }
            return@withContext obj
        }
        return colorHolder
    }

    suspend fun setColorHolder(colorHolder: ColorHolder) {
        withContext(Dispatchers.IO) {
            val output = ObjectOutputStream(FileOutputStream(file, false))
            output.writeObject(colorHolder)
            output.close()
        }
    }
}