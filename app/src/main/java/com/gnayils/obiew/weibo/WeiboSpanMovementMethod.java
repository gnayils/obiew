package com.gnayils.obiew.weibo;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Gnayils on 18/02/2017.
 */

public class WeiboSpanMovementMethod extends LinkMovementMethod {

    private static LinkMovementMethod instance = new WeiboSpanMovementMethod();
    private static TouchListener listener = new TouchListener();

    private TouchableSpan pressedSpan;

    private WeiboSpanMovementMethod() {

    }

    @Override
    public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pressedSpan = getPressedSpan(textView, spannable, event);
            if (pressedSpan != null) {
                pressedSpan.setPressed(true);
                Selection.setSelection(spannable, spannable.getSpanStart(pressedSpan), spannable.getSpanEnd(pressedSpan));
                return true;
            }
            return false;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
            if (pressedSpan != null && touchedSpan != pressedSpan) {
                pressedSpan.setPressed(false);
                pressedSpan = null;
                Selection.removeSelection(spannable);
                return false;
            }
            return true;
        } else {
            if (pressedSpan != null) {
                pressedSpan.setPressed(false);
                super.onTouchEvent(textView, spannable, event);
            }
            pressedSpan = null;
            Selection.removeSelection(spannable);
            return true;
        }
    }

    private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= textView.getTotalPaddingLeft();
        y -= textView.getTotalPaddingTop();

        x += textView.getScrollX();
        y += textView.getScrollY();

        Layout layout = textView.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
        TouchableSpan touchedSpan = null;
        if (link.length > 0) {
            touchedSpan = link[0];
        }
        return touchedSpan;
    }

    public static MovementMethod getInstance() {
        return instance;
    }

    public static TouchListener getTouchListener() {
        return listener;
    }

    private static class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(v instanceof TextView) {
                TextView textView = (TextView) v;
                CharSequence text = textView.getText();
                if(text instanceof Spannable) {
                    return instance.onTouchEvent(textView, (Spannable) text, event);
                }
            }
            return false;
        }
    }

}
