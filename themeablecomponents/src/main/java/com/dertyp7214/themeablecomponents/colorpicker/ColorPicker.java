package com.dertyp7214.themeablecomponents.colorpicker;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dertyp7214.themeablecomponents.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;

/**
 * Created by lengw on 20.09.2017.
 */

public class ColorPicker extends Dialog {

    private TextView redTxt, greenTxt, blueTxt, charp;
    private ColorSeekBar redBar, greenBar, blueBar;
    private float red = Color.red(Color.GRAY), green = Color.green(Color.GRAY), blue =
            Color.blue(Color.GRAY);
    private View colorView;
    private Context c;
    private GradientDrawable shape;
    private EditText hexCode;
    private Listener listener;
    private int time = 1100;
    private float minBrighness = 0;
    private float maxBrighness = 1;
    private boolean darkMode = false;
    private TouchListener touchListener;
    private Drawable background;
    private boolean toast = false;
    private BottomSheetText text;

    public ColorPicker(Context context) {
        super(context, R.style.Theme_MaterialComponents_Light_Dialog_Transparent);
        c = context;
        text = new BottomSheetText(getContext());
        background = new ColorDrawable(Color.WHITE);
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.color_picker);

        background.setAlpha(255);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(background);

        hexCode = findViewById(R.id.hexTxt);

        charp = findViewById(R.id.charp);

        redTxt = findViewById(R.id.txtRed);
        greenTxt = findViewById(R.id.txtGreen);
        blueTxt = findViewById(R.id.txtBlue);

        redBar = findViewById(R.id.red);
        greenBar = findViewById(R.id.green);
        blueBar = findViewById(R.id.blue);
        colorView = findViewById(R.id.colorView);
        LayerDrawable bgDrawable = (LayerDrawable) colorView.getBackground();
        shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.color_plate);

        redBar.setColor(Color.RED);
        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                red = i;
                setAllColors(red, green, blue);
                if (b) setHex(getIntFromColor(red, green, blue));
                if (listener != null) listener.update(getIntFromColor(red, green, blue));
                if (toast) toast(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (touchListener != null) touchListener.startTouch();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (touchListener != null) touchListener.stopTouch();
            }
        });

        greenBar.setColor(Color.GREEN);
        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                green = i;
                setAllColors(red, green, blue);
                if (b) setHex(getIntFromColor(red, green, blue));
                if (listener != null) listener.update(getIntFromColor(red, green, blue));
                if (toast) toast(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (touchListener != null) touchListener.startTouch();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (touchListener != null) touchListener.stopTouch();
            }
        });

        blueBar.setColor(Color.BLUE);
        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                blue = i;
                setAllColors(red, green, blue);
                if (b) setHex(getIntFromColor(red, green, blue));
                if (listener != null) listener.update(getIntFromColor(red, green, blue));
                if (toast) toast(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (touchListener != null) touchListener.startTouch();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (touchListener != null) touchListener.stopTouch();
            }
        });

        Setup();

        Button btn_ok = findViewById(R.id.btn_ok);
        Button btn_cancel = findViewById(R.id.btn_cancel);

        if (darkMode) {
            redTxt.setTextColor(Color.WHITE);
            greenTxt.setTextColor(Color.WHITE);
            blueTxt.setTextColor(Color.WHITE);
            charp.setTextColor(Color.WHITE);
            btn_ok.setTextColor(Color.WHITE);
            btn_cancel.setTextColor(Color.WHITE);
        }

        btn_ok.setOnClickListener(view -> {
            listener.color(getIntFromColor(red, green, blue));
            dismiss();
        });
        btn_cancel.setOnClickListener(view -> {
            listener.cancel();
            dismiss();
        });
        hexCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Math.abs(i1 - i2) == 1 && hexCode.getText().length() == 6) {
                    String hex = hexCode.getText().toString();
                    int color = Color.parseColor("#" + hex);
                    int red = Color.red(color);
                    int green = Color.green(color);
                    int blue = Color.blue(color);
                    setAllColors(red, green, blue, false);
                    hexCode.setSelection(hex.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (hexCode.getText().length() == 6) {
                    int color = Color.parseColor("#" + hexCode.getText().toString());
                    setAllColors(Color.red(color), Color.green(color), Color.blue(color), true);
                }
            }
        });
    }

    public void onTouchListener(TouchListener touchListener) {
        this.touchListener = touchListener;
    }

    public void setMinMaxBrighness(float min, float max) {
        this.minBrighness = min;
        this.maxBrighness = max;
    }

    public void setAnimationTime(int time) {
        this.time = time;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void Setup() {
        setAllColors((int) this.red, (int) this.green, (int) this.blue, false);
    }

    public void setColor(int color) {
        intColor(color);
    }

    public void setColor(String color) {
        stringColor(color);
    }

    private void stringColor(String color) {
        int tmp = Color.parseColor(color);
        setAllColors(Color.red(tmp), Color.green(tmp), Color.blue(tmp), false);
    }

    private void intColor(int color) {
        setAllColors(Color.red(color), Color.green(color), Color.blue(color), false);
    }

    private void setAllColors(float r, float g, float b) {
        setAllColors((int) r, (int) g, (int) b, true);
    }

    private void setAllColors(int r, int g, int b, boolean self) {
        int color = getIntFromColor(r, g, b);
        int rc = Color.red(color), gc = Color.green(color), bc = Color.blue(color);

        this.red = rc;
        this.green = gc;
        this.blue = bc;

        try {
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            Button btn = findViewById(R.id.btn_ok);
            if (hsv[2] < minBrighness || hsv[2] > maxBrighness) {
                btn.setEnabled(false);
                btn.setTextColor(Color.LTGRAY);
            } else {
                btn.setEnabled(true);
                btn.setTextColor(darkMode ? Color.WHITE : Color.BLACK);
            }
        } catch (Exception ignored) {
        }

        try {
            if (! self) {
                animateSeek(redBar, rc, time);
                animateSeek(greenBar, gc, time);
                animateSeek(blueBar, bc, time);
                setHex(color);
            }

            redTxt.setText(String.valueOf(rc));
            greenTxt.setText(String.valueOf(gc));
            blueTxt.setText(String.valueOf(bc));
            shape.setColor(color);
        } catch (Exception ignored) {
        }
    }

    private void animateSeek(final SeekBar seekBar, final int toVal, final int time) {
        ValueAnimator anim = ValueAnimator.ofInt(seekBar.getProgress(), toVal);
        anim.setDuration(time);
        anim.addUpdateListener(animation -> {
            int animProgress = (Integer) animation.getAnimatedValue();
            try {
                seekBar.setProgress(animProgress);
            } catch (Exception ignored) {
            }
        });
        anim.start();
    }

    private void setHex(int color) {
        String hex = String.format("#%06X", (0xFFFFFF & color));
        hexCode.setText(hex.replace("#", ""));
    }

    private int getIntFromColor(float Red, float Green, float Blue) {
        int R = Math.round(255 * (256 - Red));
        int G = Math.round(255 * (256 - Green));
        int B = Math.round(255 * (256 - Blue));

        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;

        return 0xFF000000 | R | G | B;
    }

    public int getColorInt() {
        return getIntFromColor(red, green, blue);
    }

    public String getColorString(int red, int green, int blue) {
        return String.format("#%06X", (0xFFFFFF & getIntFromColor(red, green, blue)));
    }

    public void setAlpha(float alpha) {
        View view = findViewById(android.R.id.content);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(view.getAlpha(), alpha);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(animation -> {
            float a = (float) animation.getAnimatedValue();
            view.setAlpha(a);
            background.setAlpha((int) (255 * a));
            Objects.requireNonNull(getWindow()).setBackgroundDrawable(background);
        });
        valueAnimator.start();
    }

    public void toast(boolean toast) {
        this.toast = toast;
        if (! toast) text.dismiss();
    }

    private void toast(int i) {
        text.setText(String.valueOf(i), getIntFromColor(red, green, blue));
        text.show();
    }

    public interface Listener {
        void color(int color);

        void update(int color);

        void cancel();
    }

    public interface TouchListener {
        void startTouch();

        void stopTouch();
    }

    @SuppressLint("ValidFragment")
    private class BottomSheetText extends BottomSheetDialog {
        String text;
        TextView textView;

        BottomSheetText(@NonNull Context context) {
            super(context);
            textView = new TextView(context);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(18F);
            setContentView(textView);
            Objects.requireNonNull(getWindow())
                    .clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        void setText(String text, @ColorInt int color) {
            this.text = text;
            textView.setText(text);
            textView.setBackgroundColor(color);
            textView.setTextColor(
                    ColorUtils.calculateLuminance(color) < 0.5 ? Color.WHITE : Color.BLACK);
        }
    }
}
