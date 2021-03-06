package com.gnayils.obiew.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnayils.obiew.Obiew;
import com.gnayils.obiew.R;
import com.gnayils.obiew.util.ViewUtils;
import com.gnayils.obiew.weibo.Weibo;
import com.gnayils.obiew.weibo.bean.Status;
import com.gnayils.obiew.weibo.WeiboSpanMovementMethod;

import static com.gnayils.obiew.util.ViewUtils.*;

/**
 * Created by Gnayils on 30/01/2017.
 */

public class StatusView extends CardView {

    public static final String TAG = StatusView.class.getSimpleName();

    public final LinearLayout rootView;
    public final RelativeLayout userInfoLayout;
    public final AvatarView userAvatarView;
    public final TextView screenNameTextView;
    public final TextView statusTimeTextView;
    public final TextView statusSourceTextView;
    public final TextView statusTextTextView;
    public final StatusPicturesView statusPicturesView;
    public final VideoPreviewView videoPreviewView;

    public final LinearLayout retweetedStatusView;
    public final TextView retweetedStatusTextTextView;
    public final StatusPicturesView retweetedStatusPicturesView;
    public final VideoPreviewView retweetedVideoPreviewView;

    public final LinearLayout commentLayout;
    public final CenteredDrawableButton repostButton;
    public final CenteredDrawableButton commentButton;
    public final CenteredDrawableButton likeButton;

    public StatusView(Context context) {
        this(context, null);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        rootView = new LinearLayout(context);
        rootView.setId(View.generateViewId());
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
        rootView.setBackground(getDrawableByAttrId(context, R.attr.selectableItemBackground));

        userInfoLayout = new RelativeLayout(context);
        userInfoLayout.setPadding(dp2px(context, 8), dp2px(context, 8), dp2px(context, 8), dp2px(context, 4));
        LinearLayout.LayoutParams userInfoLayoutLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        userInfoLayout.setLayoutParams(userInfoLayoutLayoutParams);

        userAvatarView = new AvatarView(context);
        userAvatarView.setId(View.generateViewId());
        userAvatarView.avatarCircleImageView.setId(View.generateViewId());
        RelativeLayout.LayoutParams avatarViewLayoutParams = new RelativeLayout.LayoutParams(dp2px(context, 48), dp2px(context, 48));
        avatarViewLayoutParams.addRule(RelativeLayout.ALIGN_LEFT | RelativeLayout.ALIGN_TOP);
        userAvatarView.setLayoutParams(avatarViewLayoutParams);

        screenNameTextView = new TextView(context);
        screenNameTextView.setText("用户名");
        screenNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        RelativeLayout.LayoutParams userNameTextViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        userNameTextViewLayoutParams.setMargins(dp2px(context, 8), dp2px(context, 4), 0, 0);
        userNameTextViewLayoutParams.addRule(RelativeLayout.ALIGN_TOP, userAvatarView.getId());
        userNameTextViewLayoutParams.addRule(RelativeLayout.RIGHT_OF, userAvatarView.getId());
        screenNameTextView.setLayoutParams(userNameTextViewLayoutParams);

        statusTimeTextView = new TextView(context);
        statusTimeTextView.setText("15分钟前");
        statusTimeTextView.setId(View.generateViewId());
        statusTimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        statusTimeTextView.setTextColor(ViewUtils.getColorByAttrId(context, R.attr.themeColorSecondaryText));
        RelativeLayout.LayoutParams statusTimeTextViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        statusTimeTextViewLayoutParams.setMargins(dp2px(context, 8), 0, 0, dp2px(context, 4));
        statusTimeTextViewLayoutParams.addRule(RelativeLayout.RIGHT_OF, userAvatarView.getId());
        statusTimeTextViewLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, userAvatarView.getId());
        statusTimeTextView.setLayoutParams(statusTimeTextViewLayoutParams);

        statusSourceTextView = new TextView(context);
        statusSourceTextView.setText("微博 weibo.com");
        statusSourceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        statusSourceTextView.setTextColor(ViewUtils.getColorByAttrId(context, R.attr.themeColorSecondaryText));
        RelativeLayout.LayoutParams sourceTextViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        sourceTextViewLayoutParams.setMargins(dp2px(context, 8), 0, 0, 0);
        sourceTextViewLayoutParams.addRule(RelativeLayout.RIGHT_OF, statusTimeTextView.getId());
        sourceTextViewLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, statusTimeTextView.getId());
        statusSourceTextView.setLayoutParams(sourceTextViewLayoutParams);

        userInfoLayout.addView(userAvatarView);
        userInfoLayout.addView(screenNameTextView);
        userInfoLayout.addView(statusTimeTextView);
        userInfoLayout.addView(statusSourceTextView);

        statusTextTextView = new TextView(context);
        statusTextTextView.setId(View.generateViewId());
        statusTextTextView.setText("微博内容微博内容微博内容微博内容微博内容微博内容微博内容微博内容微博内容");
        statusTextTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.status_text_size));
        statusTextTextView.setOnTouchListener(WeiboSpanMovementMethod.getTouchListener());
        statusTextTextView.setPadding(dp2px(context, 8), dp2px(context, 4), dp2px(context, 8), dp2px(context, 4));
        LinearLayout.LayoutParams statusTextTextViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        statusTextTextView.setLayoutParams(statusTextTextViewLayoutParams);

        statusPicturesView = new StatusPicturesView(context);
        statusPicturesView.setId(View.generateViewId());
        statusPicturesView.setPadding(dp2px(context, 8), dp2px(context, 4), dp2px(context, 8), dp2px(context, 4));
        LinearLayout.LayoutParams statusPicturesViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        statusPicturesView.setLayoutParams(statusPicturesViewLayoutParams);

        videoPreviewView = new VideoPreviewView(context);
        videoPreviewView.coverImageView.setId(View.generateViewId());
        videoPreviewView.setPadding(dp2px(context, 8), dp2px(context, 4), dp2px(context, 8), dp2px(context, 4));
        LinearLayout.LayoutParams videoPreviewViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        videoPreviewView.setLayoutParams(videoPreviewViewLayoutParams);

        retweetedStatusView = new LinearLayout(context);
        retweetedStatusView.setId(View.generateViewId());
        retweetedStatusView.setOrientation(LinearLayout.VERTICAL);
        retweetedStatusView.setBackground(createRippleDrawable(ViewUtils.getColorByAttrId(context, R.attr.themeColorWindowBackground), 0));

        LinearLayout.LayoutParams retweetedStatusViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        retweetedStatusView.setLayoutParams(retweetedStatusViewLayoutParams);
        retweetedStatusTextTextView = new TextView(context);
        retweetedStatusTextTextView.setText("微博内容微博内容微博内容微博内容微博内容微博内容微博内容微博内容微博内容");
        retweetedStatusTextTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.status_text_size));
        retweetedStatusTextTextView.setOnTouchListener(WeiboSpanMovementMethod.getTouchListener());
        retweetedStatusTextTextView.setPadding(dp2px(context, 8), dp2px(context, 4), dp2px(context, 8), dp2px(context, 4));
        LinearLayout.LayoutParams retweetedStatusTextTextViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        retweetedStatusTextTextView.setLayoutParams(retweetedStatusTextTextViewLayoutParams);
        retweetedStatusPicturesView = new StatusPicturesView(context);
        retweetedStatusPicturesView.setId(View.generateViewId());
        retweetedStatusPicturesView.setPadding(dp2px(context, 8), dp2px(context, 4), dp2px(context, 8), dp2px(context, 4));
        LinearLayout.LayoutParams retweetedStatusPicturesViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        retweetedStatusPicturesView.setLayoutParams(retweetedStatusPicturesViewLayoutParams);
        retweetedVideoPreviewView = new VideoPreviewView(context);
        retweetedVideoPreviewView.coverImageView.setId(View.generateViewId());
        retweetedVideoPreviewView.setPadding(dp2px(context, 8), dp2px(context, 4), dp2px(context, 8), dp2px(context, 4));
        LinearLayout.LayoutParams retweetedVideoPreviewViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(context, 200));
        retweetedVideoPreviewView.setLayoutParams(retweetedVideoPreviewViewLayoutParams);
        retweetedStatusView.addView(retweetedStatusTextTextView);
        retweetedStatusView.addView(retweetedStatusPicturesView);
        retweetedStatusView.addView(retweetedVideoPreviewView);

        commentLayout = new LinearLayout(context);
        commentLayout.setOrientation(LinearLayout.HORIZONTAL);
        commentLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout.LayoutParams hotrankButtonsLayoutParams = new LinearLayout.LayoutParams(0, dp2px(context, 36));
        hotrankButtonsLayoutParams.weight = 1;

        repostButton = new CenteredDrawableButton(context);
        repostButton.setId(View.generateViewId());
        repostButton.setTextColor(ViewUtils.getColorByAttrId(getContext(), R.attr.themeColorSecondaryText));
        repostButton.setBackground(getDrawableByAttrId(context, R.attr.selectableItemBackground));
        repostButton.setCompoundDrawablePadding(dp2px(context, 2));
        repostButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        repostButton.setCompoundDrawablesWithIntrinsicBounds(ViewUtils.getTintedDrawable(context, R.drawable.ic_repost, ViewUtils.getColorByAttrId(getContext(), R.attr.themeColorSecondaryText)), null, null, null);
        repostButton.setLayoutParams(hotrankButtonsLayoutParams);

        commentButton = new CenteredDrawableButton(context);
        commentButton.setId(View.generateViewId());
        commentButton.setTextColor(ViewUtils.getColorByAttrId(getContext(), R.attr.themeColorSecondaryText));
        commentButton.setCompoundDrawablePadding(dp2px(context, 2));
        commentButton.setBackground(getDrawableByAttrId(context, R.attr.selectableItemBackground));
        commentButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        commentButton.setCompoundDrawablesWithIntrinsicBounds(ViewUtils.getTintedDrawable(context, R.drawable.ic_comment, ViewUtils.getColorByAttrId(getContext(), R.attr.themeColorSecondaryText)), null, null, null);
        commentButton.setLayoutParams(hotrankButtonsLayoutParams);

        likeButton = new CenteredDrawableButton(context);
        likeButton.setTextColor(ViewUtils.getColorByAttrId(getContext(), R.attr.themeColorSecondaryText));
        likeButton.setCompoundDrawablePadding(dp2px(context, 2));
        likeButton.setBackground(getDrawableByAttrId(context, R.attr.selectableItemBackground));
        likeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        likeButton.setCompoundDrawablesWithIntrinsicBounds(ViewUtils.getTintedDrawable(context, R.drawable.ic_like, ViewUtils.getColorByAttrId(getContext(), R.attr.themeColorSecondaryText)), null, null, null);
        likeButton.setLayoutParams(hotrankButtonsLayoutParams);

        commentLayout.addView(repostButton);
        commentLayout.addView(commentButton);
        commentLayout.addView(likeButton);

        rootView.addView(userInfoLayout);
        rootView.addView(statusTextTextView);
        rootView.addView(statusPicturesView);
        rootView.addView(videoPreviewView);
        rootView.addView(retweetedStatusView);
        rootView.addView(commentLayout);

        addView(rootView);

        getBackground().setTint(ViewUtils.getColorByAttrId(context, R.attr.themeColorViewBackground));
    }

    public void show(Status status) {
        if (status.user == null) {
            userInfoLayout.setVisibility(View.GONE);
        } else {
            userInfoLayout.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(status.user.avatar_large).into(userAvatarView.avatarCircleImageView);
            userAvatarView.verifiedIconImageView.setVisibility(status.user.verified ? View.VISIBLE : View.INVISIBLE);
            if (status.user.verified) {
                screenNameTextView.setTextColor(ViewUtils.getColorByAttrId(getContext(), R.attr.themeColorPrimaryInverse));
                switch (status.user.verified_type) {
                    case 0:
                        userAvatarView.verifiedIconImageView.setImageResource(R.drawable.avatar_vip_golden);
                        break;
                    case 1:
                        userAvatarView.verifiedIconImageView.setImageResource(R.drawable.avatar_vip);
                        break;
                    case 2:
                        userAvatarView.verifiedIconImageView.setImageResource(R.drawable.avatar_enterprise_vip);
                        break;
                }
            } else {
                userAvatarView.verifiedIconImageView.setImageResource(0);
                screenNameTextView.setTextColor(ViewUtils.getColorByAttrId(getContext(), R.attr.themeColorPrimaryText));
            }
            screenNameTextView.setText(status.user.screen_name);
            statusSourceTextView.setText(status.getSpannableSource());
        }
        statusTimeTextView.setText(Weibo.format.date(status.created_at));
        statusTextTextView.setText(status.getSpannableText(false), TextView.BufferType.SPANNABLE);
        statusPicturesView.setPictureUrls(status.pic_urls);
        videoPreviewView.show(status.videoUrls);
        if (status.retweeted_status == null) {
            retweetedStatusView.setVisibility(View.GONE);
        } else {
            retweetedStatusView.setVisibility(View.VISIBLE);
            retweetedStatusTextTextView.setText(status.retweeted_status.getSpannableText(true), TextView.BufferType.SPANNABLE);
            retweetedStatusPicturesView.setPictureUrls(status.retweeted_status.pic_urls);
            retweetedVideoPreviewView.show(status.retweeted_status.videoUrls);
        }
        repostButton.setText(Weibo.format.commentCount(status.reposts_count));
        commentButton.setText(Weibo.format.commentCount(status.comments_count));
        likeButton.setText(Weibo.format.commentCount(status.attitudes_count));
    }
}
