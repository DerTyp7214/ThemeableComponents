package com.dertyp7214.themeablecomponents.components;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener;
import com.dertyp7214.themeablecomponents.utils.Theme;
import com.dertyp7214.themeablecomponents.utils.ThemeManager;

import java.lang.reflect.Field;

import static com.dertyp7214.themeablecomponents.helpers.Utils.getIdFromView;

public class ThemeableEditText extends com.google.android.material.textfield.TextInputEditText {

    private final Context context;
    public OnThemeChangeListener onThemeChangeListener;

    public ThemeableEditText(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ThemeableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ThemeableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        ThemeManager themeManager = ThemeManager.getInstance(context);
        onThemeChangeListener = new OnThemeChangeListener() {
            @Override
            public void onThemeChanged(Theme theme, boolean animated) {
                applyTheme(theme);
            }

            @Override
            public ThemeManager.Component.TYPE getType() {
                return ThemeManager.Component.TYPE.EDITTEXT;
            }

            @Override
            public boolean accent() {
                return true;
            }

            @Override
            public String getId() {
                return getIdFromView(ThemeableEditText.this);
            }
        };
        themeManager.register(onThemeChangeListener);
    }

    private void applyTheme(Theme theme) {
        clearCursor();
        setTextHandleColor(theme);
        getBackground().mutate().setColorFilter(theme.getColor(), PorterDuff.Mode.SRC_ATOP);
    }

    private void setTextHandleColor(Theme theme) {
        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            if (! editorField.isAccessible()) {
                editorField.setAccessible(true);
            }

            Object editor = editorField.get(this);
            Class<?> editorClass = editor.getClass();

            String[] handleNames =
                    {"mSelectHandleLeft", "mSelectHandleRight", "mSelectHandleCenter"};
            String[] resNames = {"mTextSelectHandleLeftRes", "mTextSelectHandleRightRes",
                    "mTextSelectHandleRes"};

            for (int i = 0; i < handleNames.length; i++) {
                Field handleField = editorClass.getDeclaredField(handleNames[i]);
                if (! handleField.isAccessible()) {
                    handleField.setAccessible(true);
                }

                Drawable handleDrawable = (Drawable) handleField.get(editor);

                if (handleDrawable == null) {
                    Field resField = TextView.class.getDeclaredField(resNames[i]);
                    if (! resField.isAccessible()) {
                        resField.setAccessible(true);
                    }
                    int resId = resField.getInt(this);
                    handleDrawable = getResources().getDrawable(resId);
                }

                if (handleDrawable != null) {
                    Drawable drawable = handleDrawable.mutate();
                    drawable.setColorFilter(theme.getColor(), PorterDuff.Mode.SRC_IN);
                    handleField.set(editor, drawable);
                }
            }
            setHighlightColor(theme.getLightColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearCursor() {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(this, 0);
        } catch (Exception ignored) {
        }
    }
}
