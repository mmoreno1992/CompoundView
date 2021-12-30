package com.jwhh.notekeeper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.color_selector.view.colorEnabled
import kotlinx.android.synthetic.main.color_selector.view.colorSelectorArrowLeft
import kotlinx.android.synthetic.main.color_selector.view.colorSelectorArrowRight
import kotlinx.android.synthetic.main.color_selector.view.selectedColor

class ColorSelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var listOfColors: MutableList<Int>
    private var selectedColorIndex = 0
    private var colorSelectedListener: ColorSelectedListener? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorSelector)
        listOfColors = typedArray.getTextArray(R.styleable.ColorSelector_colors).map {
            Color.parseColor(it.toString())
        }.toMutableList()
        typedArray.recycle()
        orientation = HORIZONTAL

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.color_selector, this)
        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])

        colorSelectorArrowLeft.setOnClickListener {
            selectPreviousColor()
            broadcastColor()
        }
        colorSelectorArrowRight.setOnClickListener {
            selectNextColor()
            broadcastColor()
        }
    }

    private fun selectNextColor() {
        if (selectedColorIndex == listOfColors.lastIndex) {
            selectedColorIndex = 0
        } else {
            selectedColorIndex++
        }
        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
    }

    private fun selectPreviousColor() {
        if (selectedColorIndex == 0) {
            selectedColorIndex = listOfColors.lastIndex
        } else {
            selectedColorIndex--
        }
        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
    }

    fun setColorListener(colorSelectedListener: ColorSelectedListener) {
        this.colorSelectedListener = colorSelectedListener
    }

    private fun broadcastColor() {
        val color = if (colorEnabled.isChecked) {
            listOfColors[selectedColorIndex]
        } else {
            Color.TRANSPARENT
        }
        this.colorSelectedListener?.onColorSelected(color)
    }

    fun setSelectedColor(color: Int) {
        var index = listOfColors.indexOf(color)
        if (index == -1) {
            colorEnabled.isChecked = false
            index = 0
        } else {
            colorEnabled.isChecked = true
        }
        selectedColorIndex = index
        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
    }

    interface ColorSelectedListener {
        fun onColorSelected(color: Int)
    }
}