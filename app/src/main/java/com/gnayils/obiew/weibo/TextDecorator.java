package com.gnayils.obiew.weibo;

import android.graphics.Bitmap;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;

import com.gnayils.obiew.Obiew;
import com.gnayils.obiew.R;
import com.gnayils.obiew.Preferences;
import com.gnayils.obiew.util.ViewUtils;
import com.gnayils.obiew.view.CenteredImageSpan;
import com.gnayils.obiew.weibo.bean.Comment;
import com.gnayils.obiew.weibo.bean.Comments;
import com.gnayils.obiew.weibo.bean.Repost;
import com.gnayils.obiew.weibo.bean.Reposts;
import com.gnayils.obiew.weibo.bean.Status;
import com.gnayils.obiew.weibo.bean.Statuses;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gnayils on 02/02/2017.
 */

public class TextDecorator {

    public static final Pattern URL_PATTERN = Pattern.compile(Obiew.getAppResources().getString(R.string.url_regex));
    public static final Pattern EMOTION_PATTERN = Pattern.compile(Obiew.getAppResources().getString(R.string.emotion_regex));

    public static final Pattern MENTION_PATTERN = Pattern.compile(Obiew.getAppResources().getString(R.string.mention_regex));
    public static final Pattern TOPIC_PATTERN = Pattern.compile(Obiew.getAppResources().getString(R.string.topic_regex));

    public static final int SPAN_BACKGROUND_COLOR = Obiew.getAppResources().getColor(R.color.accent_alpha_4D);

    private static int themeResource;
    public static int spanPressedColor;
    public static int spanNormalColor;
    public static int sourceColor;

    public static void decorate(Statuses statuses) {
        ensureThemeColor();
        if(statuses != null && statuses.statuses != null) {
            for(Status status : statuses.statuses) {
                status.setSpannableText(decorate(status.text));
                status.setSpannableSource(decorateSource(status.source));
                if(status.retweeted_status != null && status.retweeted_status.text != null && !status.retweeted_status.text.isEmpty()) {
                    String username = "";
                    if(status.retweeted_status.user != null) {
                        username = "@" + status.retweeted_status.user.screen_name + ": ";
                    }
                    status.retweeted_status.setSpannableText(decorate(username + status.retweeted_status.text));
                }
            }
        }
    }

    public static void decorate(Comments comments) {
        ensureThemeColor();
        if(comments != null && comments.comments != null) {
            for(Comment comment : comments.comments) {
                comment.setSpannableText(decorate(comment.text));
            }
        }
    }

    public static void decorate(Reposts reposts) {
        ensureThemeColor();
        if(reposts != null && reposts.reposts != null) {
            for(Repost repost : reposts.reposts) {
                repost.setSpannableText(decorate(repost.text));
            }
        }
    }

    public static SpannableString decorate(String text) {
        ensureThemeColor();
        SpannableString spannableString = null;
        if(text != null && !text.isEmpty()) {
            spannableString = decorateURLs(text);
            decorateTopics(spannableString);
            decorateMentions(spannableString);
            decorateEmotions(spannableString);
        }
        return spannableString;
    }

    public static SpannableString decorateURLs(String text) {
        ensureThemeColor();
        List<WeiboSpan> spanList = new ArrayList<>();
        Matcher httpMatcher;
        String replacement = " 网页链接 ";
        while((httpMatcher = URL_PATTERN.matcher(text)).find()) {
            String webURL = httpMatcher.group();
            text = text.replace(webURL, replacement);
            WeiboSpan weiboSpan = new WeiboSpan(webURL, httpMatcher.start(), httpMatcher.end() - (webURL.length() - replacement.length()), spanNormalColor, spanPressedColor, SPAN_BACKGROUND_COLOR);
            spanList.add(weiboSpan);
        }
        SpannableString spannableString = new SpannableString(text);
        for(WeiboSpan weiboSpan : spanList) {
            spannableString.setSpan(weiboSpan, weiboSpan.start, weiboSpan.end, weiboSpan.flag);
        }
        return spannableString;
    }

    public static void decorateTopics(SpannableString spannableString) {
        ensureThemeColor();
        Linkify.addLinks(spannableString, TOPIC_PATTERN, WeiboSpan.TOPIC_SCHEME + WeiboSpan.SCHEME_SEPARATOR);
        replaceUrlSpan(spannableString, spanNormalColor, spanPressedColor, SPAN_BACKGROUND_COLOR);
    }

    public static void decorateMentions(SpannableString spannableString) {
        ensureThemeColor();
        Linkify.addLinks(spannableString, MENTION_PATTERN, WeiboSpan.MENTION_SCHEME + WeiboSpan.SCHEME_SEPARATOR);
        replaceUrlSpan(spannableString, spanNormalColor, spanPressedColor, SPAN_BACKGROUND_COLOR);
    }

    public static void decorateEmotions(SpannableString spannableString) {
        ensureThemeColor();
        String string =spannableString.toString();
        Matcher emotionKeyMatcher = EMOTION_PATTERN.matcher(string);
        while(emotionKeyMatcher.find()) {
            String emotionKey = emotionKeyMatcher.group();
            Bitmap bitmap = EmotionDB.get(emotionKey, Obiew.getAppResources().getDimension(R.dimen.emotion_size_in_text));
            if(bitmap == null) {
                continue;
            } else {
                spannableString.setSpan(new CenteredImageSpan(Obiew.getAppContext(), bitmap), emotionKeyMatcher.start(), emotionKeyMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public static Spannable decorateSource(String source) {
        ensureThemeColor();
        return replaceUrlSpan((Spannable) Html.fromHtml(source), sourceColor, sourceColor, sourceColor);
    }

    private static Spannable replaceUrlSpan(Spannable spannable, int normalTextColor, int pressedTextColor, int pressedBackgroundColor) {
        URLSpan[] urlSpans = spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            int start = spannable.getSpanStart(urlSpan);
            int end = spannable.getSpanEnd(urlSpan);
            WeiboSpan linkSpan = new WeiboSpan(urlSpan.getURL(), start, end, normalTextColor, pressedTextColor, pressedBackgroundColor);
            spannable.removeSpan(urlSpan);
            spannable.setSpan(linkSpan, start, end, linkSpan.flag);
        }
        return spannable;
    }

    private static void ensureThemeColor() {
        if(themeResource != Preferences.getThemeResource()) {
            themeResource = Preferences.getThemeResource();
            spanPressedColor = ViewUtils.getColorByAttrId(Obiew.getAppContext(), R.attr.themeColorAccentInverse);
            spanNormalColor = ViewUtils.getColorByAttrId(Obiew.getAppContext(), R.attr.themeColorAccentInverse);
            sourceColor = ViewUtils.getColorByAttrId(Obiew.getAppContext(), R.attr.themeColorSecondaryText);
        }
    }
}
