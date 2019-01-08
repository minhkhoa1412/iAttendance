package com.tangtuongco.chamcong.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class AdapterMain extends FragmentPagerAdapter {
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> titleArrayList = new ArrayList<>();

    public AdapterMain(FragmentManager fm, ArrayList<Fragment> fragmentArrayList, ArrayList<String> titleArrayList) {
        super(fm);
        this.fragmentArrayList = fragmentArrayList;
        this.titleArrayList = titleArrayList;
    }

    public AdapterMain(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleArrayList.get(position);
    }
}
