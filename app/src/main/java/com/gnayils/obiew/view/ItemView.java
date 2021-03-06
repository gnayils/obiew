package com.gnayils.obiew.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gnayils.obiew.R;
import com.gnayils.obiew.util.ViewUtils;

import static com.gnayils.obiew.util.ViewUtils.*;

/**
 * Created by Gnayils on 9/25/16.
 */
public class ItemView extends RelativeLayout {

    public final ImageView iconImageView;
    public final TextView titleTextView;
    public final TextView descriptionTextView;

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setClickable(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemView, defStyleAttr, defStyleRes);
        Drawable icon = typedArray.getDrawable(R.styleable.ItemView_icon);
        int iconTint = typedArray.getColor(R.styleable.ItemView_iconTint, Color.DKGRAY);
        String title = typedArray.getString(R.styleable.ItemView_title);
        String description = typedArray.getString(R.styleable.ItemView_description);

        iconImageView = new ImageView(context);
        iconImageView.setId(View.generateViewId());
        iconImageView.setImageDrawable(icon);
        iconImageView.setImageTintList(ColorStateList.valueOf(iconTint));
        iconImageView.setImageTintList(new ColorStateList(
                new int[][]{
                    new int[]{android.R.attr.state_selected},
                    new int[]{}
                },
                new int[]{
                        ViewUtils.getColorByAttrId(context, R.attr.themeColorPrimaryInverse),
                        ViewUtils.getColorByAttrId(context, R.attr.themeColorSecondaryText)
                }));
        LayoutParams layoutParams = new LayoutParams(dp2px(context, 24), dp2px(context, 24));
        layoutParams.setMargins(dp2px(context, 16), 0, 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        iconImageView.setLayoutParams(layoutParams);
        addView(iconImageView);

        titleTextView = new TextView(context);
        titleTextView.setTextSize(16);
        titleTextView.setText(title);
        titleTextView.setTextColor(new ColorStateList(new int[][]{
                new int[]{android.R.attr.state_selected},
                new int[]{}},
                new int[]{
                        ViewUtils.getColorByAttrId(context, R.attr.themeColorPrimaryInverse),
                        ViewUtils.getColorByAttrId(context, R.attr.themeColorSecondaryText)
                }));
        layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dp2px(context, 16), 0, 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, iconImageView.getId());
        titleTextView.setLayoutParams(layoutParams);
        addView(titleTextView);

        descriptionTextView = new TextView(context);
        descriptionTextView.setTextSize(12);
        descriptionTextView.setTextColor(ViewUtils.getColorByAttrId(context, R.attr.themeColorSecondaryText));
        descriptionTextView.setText(description);
        layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, dp2px(context, 16), 0);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        descriptionTextView.setLayoutParams(layoutParams);
        addView(descriptionTextView);

        typedArray.recycle();
    }

    @Override
    public void dispatchSetSelected(boolean selected) {
        super.dispatchSetSelected(selected);
        iconImageView.setSelected(selected);
        titleTextView.setSelected(selected);
    }
}
