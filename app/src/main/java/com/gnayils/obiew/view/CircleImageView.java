package com.gnayils.obiew.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.VectorDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

    public CircleImageView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs, defStyle);
    }


    private void initialize(Context context, AttributeSet attrs, int defStyle) {
        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(Color.TRANSPARENT);
        backgroundDrawable.setShape(GradientDrawable.OVAL);
        setBackground(backgroundDrawable);
        setClipToOutline(true);
        setScaleType(ScaleType.CENTER);
    }
}
