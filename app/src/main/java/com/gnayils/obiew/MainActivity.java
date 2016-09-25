package com.gnayils.obiew;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gnayils.obiew.bmpldr.BitmapLoader;
import com.gnayils.obiew.user.LoginHandler;
import com.gnayils.obiew.user.LoginView;
import com.gnayils.obiew.view.CircleImageView;
import com.gnayils.obiew.view.OverScrollView;
import com.sina.weibo.sdk.openapi.models.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoginView {


    private Button loginButton;
    private Button signupButton;
    private TextView weiboNumberTextView;
    private TextView followNumberTextView;
    private TextView followerNumberTextView;
    private TextView usernameTextView;
    private TextView aboutMeTextView;
    private CircleImageView avatarCircleImageView;

    private ViewGroup linearLayoutLogin;
    private ViewGroup linearLayoutUserCenter;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    private LoginHandler loginHandler = new LoginHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        weiboNumberTextView = (TextView) findViewById(R.id.text_view_weibo_number);
        followNumberTextView = (TextView) findViewById(R.id.text_view_follow_number);
        followerNumberTextView = (TextView) findViewById(R.id.text_view_follower_number);
        usernameTextView = (TextView) findViewById(R.id.text_view_username);
        aboutMeTextView = (TextView) findViewById(R.id.text_view_about_me);
        avatarCircleImageView = (CircleImageView) findViewById(R.id.circle_image_view_avatar);

        linearLayoutLogin = (ViewGroup) findViewById(R.id.linear_layout_login);
        linearLayoutUserCenter = (ViewGroup) findViewById(R.id.linear_layout_user_center);

        loginButton = (Button) findViewById(R.id.button_login);
        signupButton = (Button) findViewById(R.id.button_signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginHandler.login(MainActivity.this);
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                loginHandler.signup(MainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginHandler.authorizeCallBack(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updateUser(User user) {
        weiboNumberTextView.setText(String.valueOf(user.statuses_count) + "\n微博");
        followNumberTextView.setText(String.valueOf(user.friends_count) + "\n关注");
        followerNumberTextView.setText(String.valueOf(user.followers_count) + "\n粉丝");
        usernameTextView.setText(user.screen_name);
        aboutMeTextView.setText(user.description == null || user.description.isEmpty() ? "暂无介绍" : user.description);
        BitmapLoader.getInstance().display(user.avatar_large, avatarCircleImageView);

        linearLayoutLogin.setVisibility(View.INVISIBLE);
        linearLayoutUserCenter.setVisibility(View.VISIBLE);
    }
}
