package com.example.firestorechatjava.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.firestorechatjava.Fragment.FirstFragment;
import com.example.firestorechatjava.Fragment.SecondFragment;
import com.example.firestorechatjava.Fragment.Thirdfragment;
import com.example.firestorechatjava.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OnBoarding extends AppCompatActivity {
    public static TextView thisapp,monitor;
    public static ViewPager pager;
    public static  int page=0;
    ViewPagerAdapter adapter;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        pager = findViewById(R.id.viewPager);
        pager.setOffscreenPageLimit(2);
        thisapp = findViewById(R.id.thisapp);
        monitor = findViewById(R.id.monitor);
        adapter  = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FirstFragment(), "ONE");
        adapter.addFragment(new SecondFragment(), "TWO");
        adapter.addFragment(new Thirdfragment(), "THREE");
        submit=findViewById(R.id.submit);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager, true);
         pager.setAdapter(adapter);

        submit.setText("Next");
        OnBoarding.monitor.setText("Monitor activities");
        OnBoarding.thisapp.setText("This app will help you to monitor unusual activity without entering the priviacey of your child");
submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (pager.getCurrentItem() +1 < adapter.getCount()){
            pager.setCurrentItem(pager.getCurrentItem() + 1);
    }
       else {
Intent intent =new Intent(OnBoarding.this,SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
});

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                if(position==0)
                {
                    submit.setText("Next");
                    OnBoarding.monitor.setText("Monitor activities");
                    OnBoarding.thisapp.setText("This app will help you to monitor unusual activity without entering the privacy of your child");

                }
                else   if(position==1)
                {
                    submit.setText("Next");
                    OnBoarding.monitor.setText("All in one place");
                    OnBoarding.thisapp.setText("We check for you all the activities and inform you if you go deeper or not");

                }
                else   if(position==2)
                {
                    submit.setText("Start");
                    OnBoarding.monitor.setText("Go deeper");
                    OnBoarding.thisapp.setText("Feature: Alert unusual activity, get location, battery status, block app and much more.. ");
                    Intent intent =new Intent(OnBoarding.this,SignInActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("position---","position---"+position);
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.e("position---","position---title"+position);

        //return mFragmentTitleList.get(position);
        return null;

    }
}