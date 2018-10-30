package com.dertyp7214.themeablecomponents.colorpicker

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.dertyp7214.themeablecomponents.R
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Created by lengw on 20.09.2017.
 */

class ColorPicker(private val c: Context) : Dialog(c, R.style.Theme_MaterialComponents_Light_Dialog_Transparent) {

    private lateinit var redTxt: TextView
    private lateinit var greenTxt: TextView
    private lateinit var blueTxt: TextView
    private lateinit var charp: TextView
    private lateinit var redBar: ColorSeekBar
    private lateinit var greenBar: ColorSeekBar
    private lateinit var blueBar: ColorSeekBar
    private lateinit var colorView: View
    private lateinit var shape: GradientDrawable
    private lateinit var hexCode: EditText
    private var red = Color.red(Color.GRAY).toFloat()
    private var green = Color.green(Color.GRAY).toFloat()
    private var blue = Color.blue(Color.GRAY).toFloat()
    private var listener: Listener? = null
    private var time = 1100
    private var minBrightness = 0f
    private var maxBrightness = 1f
    private var darkMode = false
    private var touchListener: TouchListener? = null
    private val background: Drawable
    private var toast = false
    private val text: BottomSheetText

    val colorInt: Int
        get() = getIntFromColor(red, green, blue)

    init {
        text = BottomSheetText(context)
        background = ColorDrawable(Color.WHITE)
    }

    fun setDarkMode(darkMode: Boolean) {
        this.darkMode = darkMode
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.color_picker)

        background.alpha = 255
        window!!.setBackgroundDrawable(background)

        hexCode = findViewById(R.id.hexTxt)

        charp = findViewById(R.id.charp)

        redTxt = findViewById(R.id.txtRed)
        greenTxt = findViewById(R.id.txtGreen)
        blueTxt = findViewById(R.id.txtBlue)

        redBar = findViewById(R.id.red)
        greenBar = findViewById(R.id.green)
        blueBar = findViewById(R.id.blue)
        colorView = findViewById(R.id.colorView)
        val bgDrawable = colorView.background as LayerDrawable
        shape = bgDrawable.findDrawableByLayerId(R.id.color_plate) as GradientDrawable

        redBar.setColor(Color.RED)
        redBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                red = i.toFloat()
                setAllColors(red, green, blue)
                if (b) setHex(getIntFromColor(red, green, blue))
                if (listener != null) listener!!.update(getIntFromColor(red, green, blue))
                if (toast) toast(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.startTouch()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.stopTouch()
            }
        })

        greenBar.setColor(Color.GREEN)
        greenBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                green = i.toFloat()
                setAllColors(red, green, blue)
                if (b) setHex(getIntFromColor(red, green, blue))
                if (listener != null) listener!!.update(getIntFromColor(red, green, blue))
                if (toast) toast(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.startTouch()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.stopTouch()
            }
        })

        blueBar.setColor(Color.BLUE)
        blueBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                blue = i.toFloat()
                setAllColors(red, green, blue)
                if (b) setHex(getIntFromColor(red, green, blue))
                if (listener != null) listener!!.update(getIntFromColor(red, green, blue))
                if (toast) toast(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.startTouch()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.stopTouch()
            }
        })

        setup()

        val btn_ok = findViewById<Button>(R.id.btn_ok)
        val btn_cancel = findViewById<Button>(R.id.btn_cancel)

        if (darkMode) {
            redTxt.setTextColor(Color.WHITE)
            greenTxt.setTextColor(Color.WHITE)
            blueTxt.setTextColor(Color.WHITE)
            charp.setTextColor(Color.WHITE)
            btn_ok.setTextColor(Color.WHITE)
            btn_cancel.setTextColor(Color.WHITE)
        }

        btn_ok.setOnClickListener {
            listener!!.color(getIntFromColor(red, green, blue))
            dismiss()
        }
        btn_cancel.setOnClickListener {
            listener!!.cancel()
            dismiss()
        }
        hexCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (Math.abs(i1 - i2) == 1 && hexCode.text.length == 6) {
                    val hex = hexCode.text.toString()
                    val color = Color.parseColor("#$hex")
                    val red = Color.red(color)
                    val green = Color.green(color)
                    val blue = Color.blue(color)
                    setAllColors(red, green, blue, false)
                    hexCode.setSelection(hex.length)
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if (hexCode.text.length == 6) {
                    val color = Color.parseColor("#${hexCode.text}")
                    setAllColors(Color.red(color), Color.green(color), Color.blue(color), true)
                }
            }
        })
    }

    fun onTouchListener(touchListener: TouchListener) {
        this.touchListener = touchListener
    }

    fun setMinMaxBrightness(min: Float, max: Float) {
        this.minBrightness = min
        this.maxBrightness = max
    }

    fun setAnimationTime(time: Int) {
        this.time = time
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun setup() {
        setAllColors(this.red.toInt(), this.green.toInt(), this.blue.toInt(), false)
    }

    fun setColor(color: Int) {
        intColor(color)
    }

    fun setColor(color: String) {
        stringColor(color)
    }

    private fun stringColor(color: String) {
        val tmp = Color.parseColor(color)
        setAllColors(Color.red(tmp), Color.green(tmp), Color.blue(tmp), false)
    }

    private fun intColor(color: Int) {
        setAllColors(Color.red(color), Color.green(color), Color.blue(color), false)
    }

    private fun setAllColors(r: Float, g: Float, b: Float) {
        setAllColors(r.toInt(), g.toInt(), b.toInt(), true)
    }

    private fun setAllColors(r: Int, g: Int, b: Int, self: Boolean) {
        val color = getIntFromColor(r.toFloat(), g.toFloat(), b.toFloat())
        val rc = Color.red(color)
        val gc = Color.green(color)
        val bc = Color.blue(color)

        this.red = rc.toFloat()
        this.green = gc.toFloat()
        this.blue = bc.toFloat()

        try {
            val hsv = FloatArray(3)
            Color.colorToHSV(color, hsv)
            val btn = findViewById<Button>(R.id.btn_ok)
            if (hsv[2] < minBrightness || hsv[2] > maxBrightness) {
                btn.isEnabled = false
                btn.setTextColor(Color.LTGRAY)
            } else {
                btn.isEnabled = true
                btn.setTextColor(if (darkMode) Color.WHITE else Color.BLACK)
            }
        } catch (ignored: Exception) {
        }

        try {
            if (!self) {
                animateSeek(redBar, rc, time)
                animateSeek(greenBar, gc, time)
                animateSeek(blueBar, bc, time)
                setHex(color)
            }

            redTxt.text = rc.toString()
            greenTxt.text = gc.toString()
            blueTxt.text = bc.toString()
            shape.setColor(color)
        } catch (ignored: Exception) {
        }
    }

    private fun animateSeek(seekBar: SeekBar, toVal: Int, time: Int) {
        val anim = ValueAnimator.ofInt(seekBar.progress, toVal)
        anim.duration = time.toLong()
        anim.addUpdateListener {
            val animProgress = it.animatedValue as Int
            try {
                seekBar.progress = animProgress
            } catch (ignored: Exception) {
            }
        }
        anim.start()
    }

    private fun setHex(color: Int) {
        val hex = String.format("#%06X", 0xFFFFFF and color)
        hexCode.setText(hex.replace("#", ""))
    }

    private fun getIntFromColor(Red: Float, Green: Float, Blue: Float): Int {
        var r = Math.round(255 * (256 - Red))
        var g = Math.round(255 * (256 - Green))
        var b = Math.round(255 * (256 - Blue))

        r = r shl 16 and 0x00FF0000
        g = g shl 8 and 0x0000FF00
        b = b and 0x000000FF

        return -0x1000000 or r or g or b
    }

    fun getColorString(red: Int, green: Int, blue: Int): String {
        return String.format("#%06X", 0xFFFFFF and getIntFromColor(red.toFloat(), green.toFloat(), blue.toFloat()))
    }

    fun setAlpha(alpha: Float) {
        val view = findViewById<View>(android.R.id.content)
        val valueAnimator = ValueAnimator.ofFloat(view.alpha, alpha)
        valueAnimator.duration = 300
        valueAnimator.addUpdateListener {
            val a = it.animatedValue as Float
            view.alpha = a
            background.alpha = (255 * a).toInt()
            window!!.setBackgroundDrawable(background)
        }
        valueAnimator.start()
    }

    fun toast(toast: Boolean) {
        this.toast = toast
        if (!toast) text.dismiss()
    }

    private fun toast(i: Int) {
        text.setText(i.toString(), getIntFromColor(red, green, blue))
        text.show()
    }

    fun disableInput() {
        hexCode.isEnabled = false
    }

    fun enableInput() {
        hexCode.isEnabled = true
    }

    interface Listener {
        fun color(color: Int)

        fun update(color: Int)

        fun cancel()
    }

    interface TouchListener {
        fun startTouch()

        fun stopTouch()
    }

    @SuppressLint("ValidFragment")
    private inner class BottomSheetText internal constructor(context: Context) : BottomSheetDialog(context) {
        internal lateinit var text: String
        internal var textView: TextView = TextView(context)

        init {
            textView.gravity = Gravity.CENTER_HORIZONTAL
            textView.textSize = 18f
            setContentView(textView)
            setCancelable(false)
            window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        internal fun setText(text: String, @ColorInt color: Int) {
            this.text = text
            textView.text = text
            textView.setBackgroundColor(color)
            textView.setTextColor(
                    if (ColorUtils.calculateLuminance(color) < 0.5) Color.WHITE else Color.BLACK)
        }
    }
}
