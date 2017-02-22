package com.example.project.danyaapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ShActivity fragment = new ShActivity();
        fragment.setDayID(position + 1);
        return fragment;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "ПНД";
            case 1:
                return "ВТР";
            case 2:
                return "СРД";
            case 3:
                return "ЧТВ";
            case 4:
                return "ПТН";
            case 5:
                return "СБТ";
            default:
                return "Вторник";
        }
    }
}