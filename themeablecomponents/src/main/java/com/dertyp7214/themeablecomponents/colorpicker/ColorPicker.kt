package com.dertyp7214.themeablecomponents.colorpicker

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import com.dertyp7214.themeablecomponents.R

/**
 * Created by lengw on 20.09.2017.
 */

class ColorPicker(c: Context) : Dialog(c, R.style.Theme_MaterialComponents_Light_Dialog_Transparent) {

    private lateinit var alphaTxt: TextView
    private lateinit var redTxt: TextView
    private lateinit var greenTxt: TextView
    private lateinit var blueTxt: TextView
    private lateinit var alphaBar: ColorSeekBar
    private lateinit var redBar: ColorSeekBar
    private lateinit var greenBar: ColorSeekBar
    private lateinit var blueBar: ColorSeekBar
    private lateinit var colorView: View
    private lateinit var shape: GradientDrawable
    private lateinit var hexCode: EditText
    private lateinit var btnOk: Button
    private lateinit var btnCancel: Button
    private var alpha = Color.alpha(Color.GRAY).toFloat()
    private var red = Color.red(Color.GRAY).toFloat()
    private var green = Color.green(Color.GRAY).toFloat()
    private var blue = Color.blue(Color.GRAY).toFloat()
    private var listener: Listener? = null
    private var time = 1100
    private var minBrightness = 0f
    private var maxBrightness = 1f
    private var darkMode = false
    private var touchListener: TouchListener? = null
    private var background: Drawable
    private var toast = false

    val colorInt: Int
        get() = getIntFromColor(alpha, red, green, blue)

    var hexCharp: Boolean = true
        @SuppressLint("SetTextI18n")
        private set(value) {
            field = value
            try {
                if (!hexCode.text.contains('#'))
                    hexCode.setText("#${hexCode.text}")
            } catch (e: Exception) {
            }
        }

    var colorMode: ColorMode = ColorMode.RGB
        set(value) {
            field = value
            field.text = ColorMode.BottomSheetText(context)
        }

    init {
        colorMode.text = ColorMode.BottomSheetText(context)
        background = ColorDrawable(Color.WHITE)
    }

    fun setDarkMode(darkMode: Boolean) {
        this.darkMode = darkMode
    }

    private val alphaBarColor = if (darkMode) Color.WHITE else Color.BLACK
    private val redBarColor = Color.RED
    private val greenBarColor = Color.GREEN
    private val blueBarColor = Color.BLUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.color_picker)

        background.alpha = 255
        window!!.setBackgroundDrawable(background)

        hexCode = findViewById(R.id.hexTxt)

        alphaTxt = findViewById(R.id.txtAlpha)
        redTxt = findViewById(R.id.txtRed)
        greenTxt = findViewById(R.id.txtGreen)
        blueTxt = findViewById(R.id.txtBlue)

        alphaBar = findViewById(R.id.alpha)
        redBar = findViewById(R.id.red)
        greenBar = findViewById(R.id.green)
        blueBar = findViewById(R.id.blue)
        colorView = findViewById(R.id.colorView)
        val bgDrawable = colorView.background as LayerDrawable
        shape = bgDrawable.findDrawableByLayerId(R.id.color_plate) as GradientDrawable

        if (colorMode == ColorMode.ARGB) {
            alphaBar.visibility = View.VISIBLE
            alphaTxt.visibility = View.VISIBLE
        } else {
            alphaBar.visibility = View.INVISIBLE
            alphaTxt.visibility = View.INVISIBLE
            alphaBar.layoutParams.height = 0
            alphaTxt.layoutParams.height = 0
            setMargins(alphaBar, 0, 0, 0, 0)
            setMargins(alphaTxt, 0, 0, 0, 0)
        }

        hexCode.filters = arrayOf(InputFilter.LengthFilter(colorMode.length), InputFilter.AllCaps(),
                InputFilter { source, _, _, dest, dstart, _ ->
                    val filtered = source.filterIndexed { index, c ->
                        val idx = dstart + index
                        (c == '#' && idx == 0 && !dest.contains('#')) || c in "0123456789ABCDEF"
                    }
                    if (dstart == 0 && filtered.getOrNull(0) != '#' && !dest.contains('#'))
                        filtered.padStart(1, '#')
                    else filtered
                })

        alphaBar.setColor(alphaBarColor)
        alphaBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val color = colorMode.calcColor(arrayListOf(alphaBar.progress, redBar.progress, greenBar.progress, blueBar.progress))
                alpha = Color.alpha(color).toFloat()
                red = Color.red(color).toFloat()
                blue = Color.green(color).toFloat()
                red = Color.blue(color).toFloat()
                setAllColors(alpha, red, green, blue)
                if (b) setHex(colorInt)
                if (listener != null) listener!!.update(colorInt)
                if (toast) toast(i, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.startTouch()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.stopTouch()
            }
        })

        redBar.setColor(redBarColor)
        redBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val color = colorMode.calcColor(arrayListOf(alphaBar.progress, redBar.progress, greenBar.progress, blueBar.progress))
                alpha = Color.alpha(color).toFloat()
                red = Color.red(color).toFloat()
                green = Color.green(color).toFloat()
                blue = Color.blue(color).toFloat()
                setAllColors(alpha, red, green, blue)
                if (b) setHex(colorInt)
                if (listener != null) listener!!.update(colorInt)
                if (toast) toast(i, 1)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.startTouch()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.stopTouch()
            }
        })

        greenBar.setColor(greenBarColor)
        greenBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val color = colorMode.calcColor(arrayListOf(alphaBar.progress, redBar.progress, greenBar.progress, blueBar.progress))
                alpha = Color.alpha(color).toFloat()
                red = Color.red(color).toFloat()
                green = Color.green(color).toFloat()
                blue = Color.blue(color).toFloat()
                setAllColors(alpha, red, green, blue)
                if (b) setHex(colorInt)
                if (listener != null) listener!!.update(colorInt)
                if (toast) toast(i, 2)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.startTouch()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.stopTouch()
            }
        })

        blueBar.setColor(blueBarColor)
        blueBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val color = colorMode.calcColor(arrayListOf(alphaBar.progress, redBar.progress, greenBar.progress, blueBar.progress))
                alpha = Color.alpha(color).toFloat()
                red = Color.red(color).toFloat()
                green = Color.green(color).toFloat()
                blue = Color.blue(color).toFloat()
                setAllColors(alpha, red, green, blue)
                if (b) setHex(colorInt)
                if (listener != null) listener!!.update(colorInt)
                if (toast) toast(i, 3)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.startTouch()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (touchListener != null) touchListener!!.stopTouch()
            }
        })

        setup()

        btnOk = findViewById(R.id.btn_ok)
        btnCancel = findViewById(R.id.btn_cancel)

        btnOk.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        btnCancel.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)

        if (darkMode) {
            alphaBar.setColor(Color.WHITE)
            alphaTxt.setTextColor(Color.WHITE)
            redTxt.setTextColor(Color.WHITE)
            greenTxt.setTextColor(Color.WHITE)
            blueTxt.setTextColor(Color.WHITE)
            btnOk.setTextColor(Color.WHITE)
            btnCancel.setTextColor(Color.WHITE)
            hexCode.setTextColor(Color.WHITE)
            background = ColorDrawable(Color.parseColor("#303641"))
            window!!.setBackgroundDrawable(background)
        }

        btnOk.setOnClickListener {
            listener!!.color(colorInt)
            dismiss()
        }

        btnCancel.setOnClickListener {
            listener!!.cancel()
            dismiss()
        }

        hexCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (Math.abs(i1 - i2) == 1 && ((hexCode.text.length == colorMode.length - 1 && hexCode.text[0] != '#')
                                || (hexCode.text.length == colorMode.length && hexCode.text[0] == '#'))) {
                    val hex = hexCode.text.toString().replace("#", "")
                    val color = Color.parseColor("#$hex")
                    val alpha = Color.alpha(color)
                    val red = Color.red(color)
                    val green = Color.green(color)
                    val blue = Color.blue(color)
                    setAllColors(alpha, red, green, blue, false)
                    hexCode.setSelection(hexCode.text.length)
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if ((hexCode.text.length == colorMode.length - 1 && hexCode.text[0] != '#') || (hexCode.text.length == colorMode.length && hexCode.text[0] == '#')) {
                    val color = Color.parseColor("#${hexCode.text.toString().replace("#", "")}")
                    setAllColors(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color), true)
                }
            }
        })
    }

    private fun colorBar(bar: ColorSeekBar, current: Float, max: Float, color: Int) {
        bar.setColor(ColorUtils.blendARGB(if (darkMode) Color.WHITE else Color.BLACK, color, current / max))
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
        setAllColors(this.alpha.toInt(), this.red.toInt(), this.green.toInt(), this.blue.toInt(), false)
    }

    fun setColor(color: Int) {
        intColor(color)
    }

    fun setColor(color: String) {
        stringColor(color)
    }

    private fun stringColor(color: String) {
        val tmp = Color.parseColor(color)
        setAllColors(Color.alpha(tmp), Color.red(tmp), Color.green(tmp), Color.blue(tmp), false)
    }

    private fun intColor(color: Int) {
        setAllColors(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color), false)
    }

    private fun setAllColors(a: Float, r: Float, g: Float, b: Float) {
        setAllColors(a.toInt(), r.toInt(), g.toInt(), b.toInt(), true)
    }

    private fun setAllColors(a: Int, r: Int, g: Int, b: Int, self: Boolean) {
        val color = getIntFromColor(a.toFloat(), r.toFloat(), g.toFloat(), b.toFloat())
        val ac = Color.alpha(color)
        val rc = Color.red(color)
        val gc = Color.green(color)
        val bc = Color.blue(color)

        this.alpha = ac.toFloat()
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
                animateSeek(alphaBar, ac, time, alphaBarColor)
                animateSeek(redBar, rc, time, redBarColor)
                animateSeek(greenBar, gc, time, greenBarColor)
                animateSeek(blueBar, bc, time, blueBarColor)
                setHex(color)
            }

            alphaTxt.text = ac.toString()
            redTxt.text = rc.toString()
            greenTxt.text = gc.toString()
            blueTxt.text = bc.toString()
            shape.setColor(color)

            colorBar(alphaBar, alphaBar.progress.toFloat(), alphaBar.max.toFloat(), alphaBarColor)
            colorBar(redBar, redBar.progress.toFloat(), redBar.max.toFloat(), redBarColor)
            colorBar(greenBar, greenBar.progress.toFloat(), greenBar.max.toFloat(), greenBarColor)
            colorBar(blueBar, blueBar.progress.toFloat(), blueBar.max.toFloat(), blueBarColor)
        } catch (ignored: Exception) {
        }
    }

    private fun animateSeek(seekBar: SeekBar, toVal: Int, time: Int, color: Int) {
        val anim = ValueAnimator.ofInt(seekBar.progress, ((toVal.toFloat() / 255) * 360).toInt())
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
        hexCode.setText(if (hexCharp) "#${Integer.toHexString(color).substring(colorMode.sub)}" else Integer.toHexString(color).substring(colorMode.sub))
    }


    private fun getIntFromColor(Alpha: Float, Red: Float, Green: Float, Blue: Float): Int {
        return Alpha.toInt() shl 24 or (Red.toInt() shl 16) or (Green.toInt() shl 8) or Blue.toInt()
    }

    fun getColorString(alpha: Int, red: Int, green: Int, blue: Int): String {
        return "#${Integer.toHexString(getIntFromColor(alpha.toFloat(), red.toFloat(), green.toFloat(), blue.toFloat())).substring(colorMode.sub)}"
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
        if (!toast) colorMode.text.dismiss()
    }

    private fun toast(i: Int, type: Int) {
        colorMode.setText(i, getIntFromColor(alpha, red, green, blue), type)
        colorMode.text.show()
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

    private fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    private fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    private fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = v.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }
}
