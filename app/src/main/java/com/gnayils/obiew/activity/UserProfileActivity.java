package com.gnayils.obiew.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnayils.obiew.R;
import com.gnayils.obiew.util.Popup;
import com.gnayils.obiew.util.ViewUtils;
import com.gnayils.obiew.view.ImageTimelineView;
import com.gnayils.obiew.view.AvatarView;
import com.gnayils.obiew.view.LoadMoreRecyclerView;
import com.gnayils.obiew.view.StatusTimelineView;
import com.gnayils.obiew.weibo.bean.Status;
import com.gnayils.obiew.weibo.bean.StatusTimeline;
import com.gnayils.obiew.weibo.bean.User;
import com.gnayils.obiew.weibo.service.StatusService;
import com.gnayils.obiew.weibo.service.SubscriberAdapter;
import com.gnayils.obiew.weibo.service.UserService;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Gnayils on 26/03/2017.
 */

public class UserProfileActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String ARGS_KEY_USER = "ARGS_KEY_USER";

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.image_view_cover) ImageView coverImageView;
    @Bind(R.id.avatar_view) AvatarView avatarView;
    @Bind(R.id.text_view_screen_name) TextView screenNameTextView;
    @Bind(R.id.text_view_description) TextView descriptionTextView;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.app_bar_layout) AppBarLayout appBarLayout;
    @Bind(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.tab_layout) TabLayout tabLayout;
    @Bind(R.id.view_pager) ViewPager viewPager;

    private StatusTimelineView statusTimelineView;
    private ImageTimelineView imageTimelineView;

    private UserService userService = new UserService();
    private StatusService statusService = new StatusService();

    private int appBarCurrentVerticalOffset;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        swipeRefreshLayout.setProgressViewOffset(false, -swipeRefreshLayout.getProgressCircleDiameter(), ViewUtils.getStatusBarHeight(this) * 2);
        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                return 0 > appBarCurrentVerticalOffset;
            }
        });
        appBarLayout.addOnOffsetChangedListener(this);
        statusTimelineView = new StatusTimelineView(this);
        imageTimelineView = new ImageTimelineView(this);
        viewPager.setAdapter(new ViewPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
        swipeRefreshLayout.setOnRefreshListener(this);
        statusTimelineView.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadUserTimeline(false, Status.FEATURE_ALL);
            }
        });
        imageTimelineView.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadUserTimeline(false, Status.FEATURE_IMAGE);
            }
        });

        if(getIntent().hasExtra(ARGS_KEY_USER)) {
            fillViews((User) getIntent().getSerializableExtra(ARGS_KEY_USER));
        } else if(getIntent().getData() != null){
            final String screenName = getIntent().getData().getHost();
            if(screenName != null && !screenName.isEmpty()) {
                userService.showUserByName(screenName, new SubscriberAdapter<User>(){

                    @Override
                    public void onSubscribe() {
                        swipeRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Popup.toast(String.format("获取用户[%s]信息失败: %s",screenName, e.getMessage()));
                    }

                    @Override
                    public void onNext(User user) {
                        fillViews(user);
                    }

                    @Override
                    public void onUnsubscribe() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        statusService.unsubscribe();
        userService.unsubscribe();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        appBarCurrentVerticalOffset = verticalOffset;
        tabLayout.getBackground().setAlpha((int) (Math.abs(verticalOffset) / (appBarLayout.getTotalScrollRange() / 255d)));
        if(Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
            tabLayout.setElevation(ViewUtils.dp2px(this, 6));
        } else if(verticalOffset == 0) {
            tabLayout.setElevation(0);
        }
    }

    private void fillViews(User user) {
        this.user = user;
        collapsingToolbarLayout.setTitle(user.screen_name);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(user.cover_image_phone != null && !user.cover_image_phone.isEmpty()) {
            String coverImageUrl = user.cover_image_phone;
            if(coverImageUrl.indexOf(";") != 0) {
                String[] coverImageUrls = coverImageUrl.split(";");
                coverImageUrl = coverImageUrls[(int) (Math.random() * coverImageUrls.length)];
            }
            Glide.with(this).load(coverImageUrl).into(coverImageView);
        }
        Glide.with(this).load(user.avatar_large).into(avatarView.avatarCircleImageView);
        screenNameTextView.setText(user.screen_name);
        descriptionTextView.setText(user.description);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        loadUserTimeline(true, Status.FEATURE_ALL);
        loadUserTimeline(true, Status.FEATURE_IMAGE);
    }

    private void loadUserTimeline(final boolean loadLatest, final int feature) {
        statusService.showUserTimeline(loadLatest, user, feature, new SubscriberAdapter<StatusTimeline>(){
            @Override
            public void onSubscribe() {
                if(loadLatest) {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }

            @Override
            public void onUnsubscribe() {
                if(loadLatest) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                if(feature == Status.FEATURE_ALL) {
                    Popup.toast("获取用户微博失败: " + e.getMessage());
                } else if(feature == Status.FEATURE_IMAGE) {
                    Popup.toast("获取用户相册失败: " + e.getMessage());
                } else {
                    Popup.toast("Unknown Feature: " + feature);
                }
            }

            @Override
            public void onNext(StatusTimeline statusTimeline) {
                if(feature == Status.FEATURE_ALL) {
                    statusTimelineView.show(statusTimeline);
                } else if(feature == Status.FEATURE_IMAGE) {
                    imageTimelineView.show(statusTimeline);
                }
            }
        });
    }

    public static void start(Context context, User user) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(ARGS_KEY_USER, user);
        context.startActivity(intent);
    }

    class ViewPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            View view = null;
            switch (position) {
                case 0:
                    view = statusTimelineView;
                    break;
                case 1:
                    view = imageTimelineView;
                    break;
            }
            if(view != null) {
                collection.addView(view);
            }
            return view;
        }
        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "微博";
                case 1: return "相册";
                default: return null;
            }
        }
    }
}
