package com.r9software.infinitescrolling.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {
 
    private List<Fragment> fragments;
 
    public PageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm); 
        this.fragments = fragments;
    } 
 
    @Override 
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    } 
 
    @Override 
    public int getCount() { 
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "My Matches";
        }else{
            return "All Photos";
        }
    }
} 