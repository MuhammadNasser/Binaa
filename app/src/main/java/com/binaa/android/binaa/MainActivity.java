package com.binaa.android.binaa;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binaa.android.binaa.fragments.AboutUsFragment;
import com.binaa.android.binaa.fragments.CarsFragment;
import com.binaa.android.binaa.fragments.ContactUsFragment;
import com.binaa.android.binaa.fragments.HotelsFragment;
import com.binaa.android.binaa.fragments.NavigationDrawerFragment;
import com.binaa.android.binaa.fragments.PropertiesFragment;
import com.binaa.android.binaa.fragments.ServicesFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, NavigationDrawerFragment.NavigationDrawerCallbacks {

    public DrawerLayout drawerLayout;
    Fragment currentFragment;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private TabLayout tabLayout;
    private int currentFragmentIndex;
    private TextView textViewTitle;
    private RelativeLayout relativeLayoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                color = getResources().getColor(R.color.colorPrimaryDark, null);
            } else {
                // noinspection deprecation
                color = getResources().getColor(R.color.colorPrimaryDark);
            }
            getWindow().setStatusBarColor(color);
        }
        setContentView(R.layout.activity_main);
        setToolBar();
        setLocale();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        relativeLayoutLoading = (RelativeLayout) findViewById(R.id.relativeLayoutLoading);

        addTabs();

        if (savedInstanceState != null) {
            currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, currentFragment).commit();
            int currentPos = savedInstanceState.getInt("currentPos");
            TabLayout.Tab tab = tabLayout.getTabAt(currentPos);
            if (tab != null) {
                tab.select();
            }
        } else {
            replaceFragment(-1);
        }

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);

        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        switch (position) {
            case 0:
                replaceFragment(-1);
                break;
            case 1:
                replaceFragment(-2);
                break;
            case 2:
                replaceFragment(-3);
                break;

        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    private void setToolBar() {

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        View actionBarView = getLayoutInflater().inflate(R.layout.toolbar_customview, toolBar, false);

        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        textViewTitle = (TextView) actionBarView.findViewById(R.id.textViewActivityTitle);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        ActionBar actionBar = mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarView);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        // Set up the drawer.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, drawerLayout);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int selectedTabPosition = tabLayout.getSelectedTabPosition();
        outState.putInt("currentPos", selectedTabPosition);
        getSupportFragmentManager().putFragment(outState, "currentFragment", currentFragment);
        super.onSaveInstanceState(outState);
    }

    public void isLoading(boolean isLoading) {
        relativeLayoutLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void addTabs() {

        String[] tabs = getResources().getStringArray(R.array.home_tabs_text);
        TypedArray iconsWhite = getResources().obtainTypedArray(R.array.home_tabs_icons_white);
        TypedArray iconsGray = getResources().obtainTypedArray(R.array.home_tabs_icons_gray);

        for (int i = 0; i < tabs.length; i++) {
            String tabTitle = tabs[i];
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setIcon(getSelector(iconsWhite.getResourceId(i, R.mipmap.ic_launcher),
                    iconsGray.getResourceId(i, R.mipmap.ic_launcher)));
            tab.setText(tabTitle);
            tabLayout.addTab(tab);
        }

        iconsWhite.recycle();
        iconsGray.recycle();
    }

    public void setActivityTitle(String title) {
        if (textViewTitle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textViewTitle.setText(Html.fromHtml("<font color='#ffffff'>" + title + "</font>", Html.FROM_HTML_OPTION_USE_CSS_COLORS));
            } else {
                //noinspection deprecation
                textViewTitle.setText(Html.fromHtml("<font color='#ffffff'>" + title + "</font>"));
            }
        }
    }

    private StateListDrawable getSelector(int resWhite, int resBrown) {

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_selected}, getFromDrawables(resBrown));
        states.addState(new int[]{}, getFromDrawables(resWhite));

        return states;
    }

    private Drawable getFromDrawables(int resId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable = getResources().getDrawable(resId, null);
        } else {
            //noinspection deprecation
            drawable = getResources().getDrawable(resId);
        }

        return drawable;
    }

    private void replaceFragment(int position) {
        if (position == currentFragmentIndex) {
            return;
        }
        String[] slideMenuItems = getResources().getStringArray(R.array.slideMenuItems);
        String[] tabsText = getResources().getStringArray(R.array.home_tabs_text);

        Fragment fragment;
        String fragmentTitle;

        switch (position) {
            case -3:
                fragment = new HotelsFragment();
                fragmentTitle = tabsText[2];
                break;
            case -2:
                fragment = new CarsFragment();
                fragmentTitle = tabsText[1];
                break;
            case -1:
                fragment = new PropertiesFragment();
                fragmentTitle = tabsText[0];
                break;
            case 0:
                fragment = new PropertiesFragment();
                tabLayout.setVisibility(View.VISIBLE);

                fragmentTitle = slideMenuItems[position];
                break;
            case 1:
                fragment = new PropertiesFragment();
                tabLayout.setVisibility(View.VISIBLE);


                fragmentTitle = slideMenuItems[position - 1];
                break;
            case 2:
                fragment = new ServicesFragment();
                tabLayout.setVisibility(View.GONE);

                fragmentTitle = slideMenuItems[position - 1];
                break;
            case 3:
                fragment = new AboutUsFragment();
                tabLayout.setVisibility(View.GONE);

                fragmentTitle = slideMenuItems[position - 1];
                break;
            case 4:
                fragment = new ContactUsFragment();
                tabLayout.setVisibility(View.GONE);

                fragmentTitle = slideMenuItems[position - 1];
                break;
            default:
                return;
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        setActivityTitle(fragmentTitle);

        currentFragmentIndex = position;
        currentFragment = fragment;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        //noinspection SimplifiableIfStatement
        if (mNavigationDrawerFragment.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawerFragment.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mNavigationDrawerFragment.mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (currentFragmentIndex == 0) {
            super.onBackPressed();
        } else {
            replaceFragment(0);
        }
    }

    public void setLocale() {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale local = new Locale("ar");
        Locale.setDefault(local);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(local);
        } else {
            // noinspection deprecation
            config.locale = local;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getBaseContext().createConfigurationContext(config);
        } else {
            // noinspection deprecation
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        replaceFragment(position);
    }


}
