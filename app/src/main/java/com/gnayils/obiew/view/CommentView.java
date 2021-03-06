package com.gnayils.obiew.view;

import android.content.Context;
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
import com.gnayils.obiew.weibo.WeiboSpanMovementMethod;
import com.gnayils.obiew.weibo.bean.Comment;

import static com.gnayils.obiew.util.ViewUtils.dp2px;
import static com.gnayils.obiew.util.ViewUtils.getDrawableByAttrId;

/**
 * Created by Gnayils on 12/03/2017.
 */

public class CommentView extends CardView {

    public final RelativeLayout rootView;
    public final AvatarView userAvatarView;
    public final TextView screenNameTextView;
    public final TextView commentTimeTextView;
    public final TextView commentTextTextView;

    public CommentView(Context context) {
        this(context, null);
    }

    public CommentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        rootView = new RelativeLayout(context);
        rootView.setPadding(dp2px(context, 8), dp2px(context, 8), dp2px(context, 8), dp2px(context, 8));
        LinearLayout.LayoutParams userInfoLayoutLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(userInfoLayoutLayoutParams);
        rootView.setClickable(true);
        rootView.setBackground(getDrawableByAttrId(context, R.attr.selectableItemBackground));

        userAvatarView = new AvatarView(context);
        userAvatarView.setId(View.generateViewId());
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

        commentTimeTextView = new TextView(context);
        commentTimeTextView.setText("15分钟前");
        commentTimeTextView.setId(View.generateViewId());
        commentTimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        commentTimeTextView.setTextColor(ViewUtils.getColorByAttrId(context, R.attr.themeColorSecondaryText));
        RelativeLayout.LayoutParams statusTimeTextViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        statusTimeTextViewLayoutParams.setMargins(dp2px(context, 8), 0, 0, dp2px(context, 4));
        statusTimeTextViewLayoutParams.addRule(RelativeLayout.RIGHT_OF, userAvatarView.getId());
        statusTimeTextViewLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, userAvatarView.getId());
        commentTimeTextView.setLayoutParams(statusTimeTextViewLayoutParams);

        commentTextTextView = new TextView(context);
        commentTextTextView.setText("微博评论微博评论微博评论微博评论微博评论微博评论微博评论微博评论微博评论微博评论微博评论微博评论微博评论微博评论");
        commentTextTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.comment_text_size));
        commentTextTextView.setOnTouchListener(WeiboSpanMovementMethod.getTouchListener());
        RelativeLayout.LayoutParams commentTextTextViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        commentTextTextViewLayoutParams.addRule(RelativeLayout.BELOW, userAvatarView.getId());
        commentTextTextViewLayoutParams.addRule(RelativeLayout.RIGHT_OF, userAvatarView.getId());
        commentTextTextViewLayoutParams.setMargins(dp2px(context, 8), dp2px(context, 8), 0, 0);
        commentTextTextView.setLayoutParams(commentTextTextViewLayoutParams);

        rootView.addView(userAvatarView);
        rootView.addView(screenNameTextView);
        rootView.addView(commentTimeTextView);
        rootView.addView(commentTextTextView);

        addView(rootView);

        getBackground().setTint(ViewUtils.getColorByAttrId(context, R.attr.themeColorViewBackground));
    }

    public void show(Comment comment) {
        Glide.with(getContext()).load(comment.user.avatar_large).into(userAvatarView.avatarCircleImageView);
        userAvatarView.verifiedIconImageView.setVisibility(comment.user.verified ? View.VISIBLE : View.INVISIBLE);
        if(comment.user.verified) {
            screenNameTextView.setTextColor(ViewUtils.getColorByAttrId(getContext(), R.attr.themeColorPrimaryInverse));
            switch(comment.user.verified_type) {
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
            screenNameTextView.setTextColor(ViewUtils.getColorByAttrId(getContext(), R.attr.themeColorPrimaryText));
        }
        screenNameTextView.setText(comment.user.screen_name);
        commentTimeTextView.setText(Weibo.format.date(comment.created_at));
        commentTextTextView.setText(comment.getSpannableText(), TextView.BufferType.SPANNABLE);
    }
}
