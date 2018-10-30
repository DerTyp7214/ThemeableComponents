package com.dertyp7214.themeablecomponents.components

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dertyp7214.themeablecomponents.R
import com.dertyp7214.themeablecomponents.colorpicker.ColorPicker
import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener
import com.dertyp7214.themeablecomponents.utils.Theme
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.Thread.sleep
import java.util.*

@SuppressLint("ValidFragment")
class ThemeBottomSheet(private val sharedPreferences: SharedPreferences, private val listeners: List<OnThemeChangeListener>) : BottomSheetDialogFragment() {
    private var rootView: View? = null
    private var manager: FragmentManager? = null
    private var TAG: String = ""

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        rootView = LayoutInflater.from(context).inflate(R.layout.theme_bottom_sheet, null)
        dialog.setContentView(rootView!!)

        val items = ArrayList<Item>()

        for (listener in listeners)
            items.add(
                    Item(listener, listener.id, sharedPreferences.getInt(listener.id,
                            Color.GRAY)))

        val adapter = Adapter(this, context!!, items)
        val recyclerView = rootView!!.findViewById<RecyclerView>(R.id.rv_bottom_sheet)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.notifyDataSetChanged()
    }

    override fun show(manager: FragmentManager, tag: String) {
        super.show(manager, tag)
        this.manager = manager
        this.TAG = tag
    }

    private fun setAlpha(alpha: Float) {
        val view = rootView!!.parent as View
        val valueAnimator = ValueAnimator.ofFloat(view.alpha, alpha)
        valueAnimator.duration = 300
        valueAnimator.addUpdateListener {
            val a = it.animatedValue as Float
            view.alpha = a
        }
        valueAnimator.start()
    }

    private inner class Item internal constructor(internal var listener: OnThemeChangeListener, internal var text: String, @param:ColorInt @field:ColorInt
    internal var color: Int)

    private inner class Adapter internal constructor(internal var bottomSheet: ThemeBottomSheet, internal var context: Context, internal var items: List<Item>) : RecyclerView.Adapter<ViewHolder>() {

        @SuppressLint("InflateParams")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.bottom_sheet_item, null)
            return ViewHolder(view)
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            val textView = holder.textView
            val colorCard = holder.colorCard
            val layout = holder.layout

            layout.setOnClickListener {
                val tempOldColor = item.color
                val colorPicker = ColorPicker(context)
                colorPicker.setColor(item.color)
                colorPicker.setAnimationTime(0)
                colorPicker.setCancelable(false)
                colorPicker.onTouchListener(object : ColorPicker.TouchListener {
                    override fun startTouch() {
                        setAlpha(0.01f)
                        colorPicker.toast(true)
                        colorPicker.setAlpha(0.01f)
                        colorPicker.disableInput()
                        Thread {
                            sleep(150)
                            activity!!.runOnUiThread {
                                dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                                colorPicker.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                            }
                        }.start()
                    }

                    override fun stopTouch() {
                        setAlpha(1f)
                        colorPicker.toast(false)
                        colorPicker.setAlpha(1f)
                        colorPicker.enableInput()
                        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        colorPicker.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    }
                })
                colorPicker.setListener(object : ColorPicker.Listener {
                    override fun color(color: Int) {
                        item.color = color
                        sharedPreferences.edit().putInt(item.text, color).apply()
                    }

                    override fun update(color: Int) {
                        colorCard.setCardBackgroundColor(color)
                        item.listener.onThemeChanged(Theme(color), false)
                    }

                    override fun cancel() {
                        colorCard.setCardBackgroundColor(tempOldColor)
                        item.listener.onThemeChanged(Theme(tempOldColor), true)
                    }
                })
                colorPicker.show()
            }

            textView.text = item.text
            colorCard.setCardBackgroundColor(item.color)
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

    private inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var colorCard: CardView = itemView.findViewById(R.id.colorCard)
        internal var textView: TextView = itemView.findViewById(R.id.textView)
        internal var layout: RelativeLayout = itemView.findViewById(R.id.layout)
    }
}
