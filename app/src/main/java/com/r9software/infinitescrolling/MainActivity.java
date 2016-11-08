package com.r9software.infinitescrolling;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.r9software.infinitescrolling.adapters.PageAdapter;
import com.r9software.infinitescrolling.fragments.EmptyFragment;
import com.r9software.infinitescrolling.fragments.MyPicturesFragment;

import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    private PagerAdapter mPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initializePaging();
    }

    private void initializePaging() {

        List<Fragment> fragments = new Vector<Fragment>();

        // Add fragments here
        fragments.add(Fragment.instantiate(this, MyPicturesFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, EmptyFragment.class.getName()));

        this.mPagerAdapter  = new PageAdapter(super.getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager)super.findViewById(R.id.pager);
        pager.setAdapter(this.mPagerAdapter);
    }
}
