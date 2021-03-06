package com.gnayils.obiew.weibo.service;

import com.gnayils.obiew.Obiew;
import com.gnayils.obiew.R;
import com.gnayils.obiew.weibo.Weibo;
import com.gnayils.obiew.weibo.api.CommentAPI;
import com.gnayils.obiew.weibo.api.StatusAPI;
import com.gnayils.obiew.weibo.api.WeiboAPI;
import com.gnayils.obiew.weibo.bean.Comment;
import com.gnayils.obiew.weibo.bean.Reposts;
import com.gnayils.obiew.weibo.bean.Status;
import com.gnayils.obiew.weibo.bean.Statuses;
import com.gnayils.obiew.weibo.bean.User;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Gnayils on 30/05/2017.
 */

public class StatusService extends BaseService {

    public static final String TAG = StatusService.class.getSimpleName();

    private int[] homeTimelineCurrentPages = new int[Status.FEATURES_COUNT];
    private int[] userTimelineCurrentPages = new int[Status.FEATURES_COUNT];
    private int[] bilateralTimelineCurrentPages = new int[Status.FEATURES_COUNT];
    private int[] publicTimelineCurrentPages = new int[Status.FEATURES_COUNT];
    private int repostTimelineCurrentPage = 0;
    private int mentionTimelineCurrentPage = 0;

    public StatusService() {
    }

    public void showHomeTimeline(boolean loadLatest, int feature, SubscriberAdapter<Statuses> subscriberAdapter) {
        homeTimelineCurrentPages[feature] = loadLatest ? 1 : ++homeTimelineCurrentPages[feature];
        Subscription subscription = WeiboAPI.get(StatusAPI.class)
                .homeTimeline(feature, homeTimelineCurrentPages[feature], Weibo.consts.STATUS_TIMELINE_ITEM_COUNT_PER_PAGE)
                .doOnSubscribe(subscriberAdapter.onSubscribeAction)
                .doOnUnsubscribe(subscriberAdapter.onUnsubscribeAction)
                .doOnNext(Actions.PARSE_STATUS_VIDEO_INFO)
                .doOnNext(Actions.DECORATE_STATUS_TEXT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberAdapter);
        addSubscription(subscription);
    }

    public void showPublicTimeline(boolean loadLatest, int feature, SubscriberAdapter<Statuses> subscriberAdapter) {
        publicTimelineCurrentPages[feature] = loadLatest ? 1 : ++publicTimelineCurrentPages[feature];
        Subscription subscription = WeiboAPI.get(StatusAPI.class)
                .publicTimeline(feature, publicTimelineCurrentPages[feature], Weibo.consts.STATUS_TIMELINE_ITEM_COUNT_PER_PAGE)
                .doOnSubscribe(subscriberAdapter.onSubscribeAction)
                .doOnUnsubscribe(subscriberAdapter.onUnsubscribeAction)
                .doOnNext(Actions.DECORATE_STATUS_TEXT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberAdapter);
        addSubscription(subscription);
    }

    public void showBilateralTimeline(boolean loadLatest, int feature, SubscriberAdapter<Statuses> subscriberAdapter) {
        bilateralTimelineCurrentPages[feature] = loadLatest ? 1 : ++bilateralTimelineCurrentPages[feature];
        Subscription subscription = WeiboAPI.get(StatusAPI.class)
                .bilateralTimeline(feature, bilateralTimelineCurrentPages[feature], Weibo.consts.STATUS_TIMELINE_ITEM_COUNT_PER_PAGE)
                .doOnSubscribe(subscriberAdapter.onSubscribeAction)
                .doOnUnsubscribe(subscriberAdapter.onUnsubscribeAction)
                .doOnNext(Actions.DECORATE_STATUS_TEXT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberAdapter);
        addSubscription(subscription);
    }

    public void showMentionTimeline(boolean loadLatest, SubscriberAdapter<Statuses> subscriberAdapter) {
        mentionTimelineCurrentPage = loadLatest ? 1 : ++mentionTimelineCurrentPage;
        Subscription subscription = WeiboAPI.get(StatusAPI.class)
                .mentions(mentionTimelineCurrentPage, Weibo.consts.STATUS_TIMELINE_ITEM_COUNT_PER_PAGE)
                .doOnSubscribe(subscriberAdapter.onSubscribeAction)
                .doOnUnsubscribe(subscriberAdapter.onUnsubscribeAction)
                .doOnNext(Actions.DECORATE_STATUS_TEXT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberAdapter);
        addSubscription(subscription);
    }

    public void showUserTimeline(boolean loadLatest, User user, int feature, SubscriberAdapter<Statuses> subscriberAdapter) {
        userTimelineCurrentPages[feature] = loadLatest ? 1 : ++userTimelineCurrentPages[feature];
        Subscription subscription = WeiboAPI.get(StatusAPI.class)
                .userTimeline(user.id, feature, userTimelineCurrentPages[feature], Weibo.consts.STATUS_TIMELINE_ITEM_COUNT_PER_PAGE)
                .doOnSubscribe(subscriberAdapter.onSubscribeAction)
                .doOnUnsubscribe(subscriberAdapter.onUnsubscribeAction)
                .doOnNext(Actions.PARSE_STATUS_VIDEO_INFO)
                .doOnNext(Actions.DECORATE_STATUS_TEXT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberAdapter);
        addSubscription(subscription);
    }

    public void showRepostTimeline(Status status, boolean loadLatest, SubscriberAdapter<Reposts> subscriberAdapter) {
        repostTimelineCurrentPage = loadLatest ? 1 : ++repostTimelineCurrentPage;
        Subscription subscription = WeiboAPI.get(StatusAPI.class)
                .repostTimeline(status.id, repostTimelineCurrentPage, Weibo.consts.REPOST_TIMELINE_ITEM_COUNT_PER_PAGE)
                .doOnSubscribe(subscriberAdapter.onSubscribeAction)
                .doOnUnsubscribe(subscriberAdapter.onUnsubscribeAction)
                .doOnNext(Actions.DECORATE_REPOST_TEXT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberAdapter);
        addSubscription(subscription);
    }

    public void publishStatus(String statusText, List<String> selectedPhotoPaths, SubscriberAdapter<Status> subscriberAdapter) {
        if (statusText == null || statusText.trim().isEmpty() || statusText.length() > Weibo.consts.STATUS_TEXT_MAX_LENGTH
                || (selectedPhotoPaths != null && selectedPhotoPaths.size() > 9)) {
            return;
        }
        if (selectedPhotoPaths == null || selectedPhotoPaths.isEmpty()) {
            WeiboAPI.get(StatusAPI.class).update(statusText)
                    .doOnSubscribe(subscriberAdapter.onSubscribeAction)
                    .doOnUnsubscribe(subscriberAdapter.onUnsubscribeAction)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriberAdapter);
        } else if (!selectedPhotoPaths.isEmpty() && selectedPhotoPaths.size() == 1) {
            RequestBody statusTextRequestBody = RequestBody.create(MultipartBody.FORM, statusText);
            File photoFile = new File(selectedPhotoPaths.get(0));
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
            MultipartBody.Part photoPart = MultipartBody.Part.createFormData("pic", photoFile.getName(), photoRequestBody);
            WeiboAPI.get(StatusAPI.class).upload(statusTextRequestBody, photoPart)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(subscriberAdapter.onSubscribeAction)
                    .doOnUnsubscribe(subscriberAdapter.onUnsubscribeAction)
                    .subscribe(subscriberAdapter);
        } else if (!selectedPhotoPaths.isEmpty() && selectedPhotoPaths.size() > 1 && selectedPhotoPaths.size() < 10) {
            StatusPublishTask publishStatusTask = new StatusPublishTask(statusText, selectedPhotoPaths, subscriberAdapter);
            publishStatusTask.execute();
        }
    }

    public void repostStatus(Status status, String repostText, SubscriberAdapter<Comment> subscriberAdapter) {
        Subscription subscription = WeiboAPI.get(StatusAPI.class)
                .repost(status.id, repostText)
                .doOnSubscribe(subscriberAdapter.onSubscribeAction)
                .doOnUnsubscribe(subscriberAdapter.onUnsubscribeAction)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberAdapter);
        addSubscription(subscription);
    }
}
