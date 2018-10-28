package com.dertyp7214.themeablecomponents.components;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dertyp7214.themeablecomponents.R;
import com.dertyp7214.themeablecomponents.colorpicker.ColorPicker;
import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener;
import com.dertyp7214.themeablecomponents.utils.Theme;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@SuppressLint("ValidFragment")
public class ThemeBottomSheet extends BottomSheetDialogFragment {

    private final List<OnThemeChangeListener> listeners;
    private final SharedPreferences sharedPreferences;
    private View rootView;
    private FragmentManager manager;
    private String tag;

    public ThemeBottomSheet(SharedPreferences sharedPreferences, List<OnThemeChangeListener> listeners) {
        this.listeners = listeners;
        this.sharedPreferences = sharedPreferences;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.theme_bottom_sheet, null);
        dialog.setContentView(rootView);

        List<Item> items = new ArrayList<>();

        for (OnThemeChangeListener listener : listeners)
            items.add(
                    new Item(listener, listener.getId(), sharedPreferences.getInt(listener.getId(),
                            Color.GRAY)));

        Adapter adapter = new Adapter(this, getContext(), items);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_bottom_sheet);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        this.manager = manager;
        this.tag = tag;
    }

    private void setAlpha(float alpha) {
        View view = (View) rootView.getParent();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(view.getAlpha(), alpha);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(animation -> {
            float a = (float) animation.getAnimatedValue();
            view.setAlpha(a);
        });
        valueAnimator.start();
    }

    private class Item {
        OnThemeChangeListener listener;
        String text;
        @ColorInt
        int color;

        Item(OnThemeChangeListener listener, String text, @ColorInt int color) {
            this.listener = listener;
            this.text = text;
            this.color = color;
        }
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {
        List<Item> items;
        Context context;
        ThemeBottomSheet bottomSheet;

        Adapter(ThemeBottomSheet bottomSheet, Context context, List<Item> items) {
            this.items = items;
            this.context = context;
            this.bottomSheet = bottomSheet;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bottom_sheet_item, null);
            return new ViewHolder(view);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Item item = items.get(position);
            TextView textView = holder.textView;
            CardView colorCard = holder.colorCard;
            RelativeLayout layout = holder.layout;

            layout.setOnClickListener(v -> {
                int tempOldColor = item.color;
                ColorPicker colorPicker = new ColorPicker(context);
                colorPicker.setColor(item.color);
                colorPicker.setAnimationTime(0);
                colorPicker.setCancelable(false);
                colorPicker.onTouchListener(new ColorPicker.TouchListener() {
                    @Override
                    public void startTouch() {
                        setAlpha(0.01F);
                        colorPicker.toast(true);
                        colorPicker.setAlpha(0.01F);
                        Objects.requireNonNull(getDialog().getWindow())
                                .clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        Objects.requireNonNull(colorPicker.getWindow())
                                .clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    }

                    @Override
                    public void stopTouch() {
                        setAlpha(1F);
                        colorPicker.toast(false);
                        colorPicker.setAlpha(1F);
                        Objects.requireNonNull(getDialog().getWindow())
                                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        Objects.requireNonNull(colorPicker.getWindow())
                                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    }
                });
                colorPicker.setListener(new ColorPicker.Listener() {
                    @Override
                    public void color(int color) {
                        item.color = color;
                        sharedPreferences.edit().putInt(item.text, color).apply();
                    }

                    @Override
                    public void update(int color) {
                        colorCard.setCardBackgroundColor(color);
                        item.listener.onThemeChanged(new Theme(color), false);
                    }

                    @Override
                    public void cancel() {
                        colorCard.setCardBackgroundColor(tempOldColor);
                        item.listener.onThemeChanged(new Theme(tempOldColor), true);
                    }
                });
                colorPicker.show();
            });

            textView.setText(item.text);
            colorCard.setCardBackgroundColor(item.color);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        CardView colorCard;
        TextView textView;
        RelativeLayout layout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorCard = itemView.findViewById(R.id.colorCard);
            textView = itemView.findViewById(R.id.textView);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
