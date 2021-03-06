package com.gnayils.obiew.weibo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.text.Spanned;
import android.view.View;

import com.gnayils.obiew.Obiew;
import com.gnayils.obiew.R;

/**
 * Created by Gnayils on 07/05/2017.
 */
public class WeiboSpan extends TouchableSpan {

    public static final String SCHEME_SEPARATOR = "://";

    public static final String HTTP_SCHEME = "http";
    public static final String TOPIC_SCHEME = Obiew.getAppResources().getString(R.string.topic_scheme);
    public static final String MENTION_SCHEME = Obiew.getAppResources().getString(R.string.mention_scheme);

    public String url;
    public int start;
    public int end;
    public int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

    public WeiboSpan(String url, int start, int end, int normalTextColor, int pressedTextColor, int pressedBackgroundColor) {
        super(normalTextColor, pressedTextColor, pressedBackgroundColor);
        this.url = url;
        this.start = start;
        this.end = end;
    }

    @Override
    public void onClick(View widget) {
        Uri uri = Uri.parse(url);
        Context context = widget.getContext();
        if (uri.getScheme().startsWith(HTTP_SCHEME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
            context.startActivity(intent);
        }
    }
}
