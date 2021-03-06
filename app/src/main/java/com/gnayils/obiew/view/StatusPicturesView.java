package com.gnayils.obiew.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gnayils.obiew.R;
import com.gnayils.obiew.activity.PicturePagerActivity;
import com.gnayils.obiew.fragment.PictureFragment;
import com.gnayils.obiew.util.Popup;
import com.gnayils.obiew.util.ViewUtils;
import com.gnayils.obiew.weibo.bean.PicUrls;

import static com.gnayils.obiew.util.ViewUtils.*;

import java.util.List;
import java.util.Objects;

/**
 * Created by Gnayils on 26/11/2016.
 */

public class StatusPicturesView extends ViewGroup implements View.OnClickListener {

    public final int MARGIN_IN_IMAGES = dp2px(getContext(), 4);

    private int imageViewVisibleCount;
    private List<PicUrls> picUrlsList;
    private OnPictureItemClickListener listener;

    public StatusPicturesView(Context context) {
        this(context, null);
    }

    public StatusPicturesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusPicturesView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public StatusPicturesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        for (int i = 0; i < 9; i++) {
            GiFHintImageView imageView = new GiFHintImageView(getContext());
            imageView.setLayoutParams(new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setForegroundResource(ViewUtils.getResourceIdByAttrId(context, R.attr.selectableItemBackground));
            imageView.setBackgroundResource(R.drawable.bg_rect_shape_round_corner);
            imageView.setClipToOutline(true);
            imageView.setOnClickListener(this);
            addView(imageView);
        }
    }

    public void setPictureUrls(List<PicUrls> picUrlsList) {
        if(picUrlsList == null || picUrlsList.isEmpty()) {
            for (int i = 0; i < getChildCount(); i++) {
                GiFHintImageView child = (GiFHintImageView) getChildAt(i);
                child.setImageResource(0);
            }
            setVisibility(View.GONE);
            return;
        }
        if(picUrlsList.size() > getChildCount()) {
            setVisibility(View.GONE);
            Popup.toast("Too much picture url for the StatusPicturesView");
            return;
        }
        this.picUrlsList = picUrlsList;
        for (int i = 0; i < getChildCount(); i++) {
            GiFHintImageView imageView = (GiFHintImageView)getChildAt(i);
            if(i < picUrlsList.size()) {
                PicUrls picUrls = picUrlsList.get(i);
                imageView.setVisibility(View.VISIBLE);
                imageView.setHintVisible(picUrls.isGif());
                Glide.with(getContext()).load(picUrls.middle()).asBitmap().into(imageView);
            } else {
                imageView.setImageResource(0);
                imageView.setVisibility(View.GONE);
            }
        }
        imageViewVisibleCount = 0;
        for (int i = 0; i < getChildCount(); i++) {
            imageViewVisibleCount += getChildAt(i).getVisibility() == View.VISIBLE ? 1 : 0;
        }
        if(imageViewVisibleCount > 0) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int imageSize = (MeasureSpec.getSize(widthMeasureSpec) - MARGIN_IN_IMAGES * 2 - getPaddingLeft() - getPaddingRight()) / 3 ;
        if(imageViewVisibleCount > 0) {
            for (int i = 0; i < imageViewVisibleCount; i++) {
                View child = getChildAt(i);
                if(imageViewVisibleCount == 1) {
                    LayoutParams layoutParams = child.getLayoutParams();
                    layoutParams.width = (int) (imageSize * 3 * 0.625f);
                    layoutParams.height = (int) (layoutParams.width * 0.75f);
                    child.setLayoutParams(layoutParams);
                } else {
                    LayoutParams layoutParams = child.getLayoutParams();
                    layoutParams.width = imageSize;
                    layoutParams.height = imageSize;
                    child.setLayoutParams(layoutParams);
                }
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            }
            int rows = (int) Math.ceil(imageViewVisibleCount / 3d);
            View child = getChildAt(0);
            setMeasuredDimension(widthMeasureSpec, resolveSizeAndState(child.getMeasuredHeight() * rows + (rows - 1) * MARGIN_IN_IMAGES + getPaddingTop() + getPaddingBottom(), heightMeasureSpec, child.getMeasuredState()));
        } else {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int childLeft =  getPaddingLeft();
        int childTop = getPaddingTop();

        for (int i = 0; i < imageViewVisibleCount; i++) {

            View child = getChildAt(i);
            child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
            childLeft = child.getRight() + MARGIN_IN_IMAGES;
            childTop = child.getTop();

            if(imageViewVisibleCount == 4 && i == 1) {
                childLeft = getPaddingLeft();
                childTop = child.getBottom() + MARGIN_IN_IMAGES;
            } else if(5 <= imageViewVisibleCount && imageViewVisibleCount <= 9 && (i == 2 || i == 5)) {
                childLeft = getPaddingLeft();
                childTop = child.getBottom() + MARGIN_IN_IMAGES;
            }
        }
    }

    public void setOnPictureItemClickListener(OnPictureItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnPictureItemClickListener {

        void onPictureItemClick(StatusPicturesView view, View v, int index);

    }

    @Override
    public void onClick(View v) {
        if(listener == null) {
            return;
        }
        for(int i = 0; i < imageViewVisibleCount; i ++) {
            if(v == getChildAt(i)) {
                listener.onPictureItemClick(this, v, i);
                break;
            }
        }
    }
}
