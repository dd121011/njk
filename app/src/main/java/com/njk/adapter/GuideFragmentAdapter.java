package com.njk.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.njk.R;
import com.njk.fragment.GuideFragment;
import com.njk.viewpager.IconPagerAdapter;

public class GuideFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    protected static final String[] CONTENT = new String[] { "This", "Is", "Test", };
    protected static final int[] ICONS = new int[] {
            R.mipmap.guide_01,
            R.mipmap.guide_02,
            R.mipmap.guide_03
    };

    private int mCount = CONTENT.length;

    public GuideFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return GuideFragment.newInstance(CONTENT[position % CONTENT.length],ICONS[position % ICONS.length]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return GuideFragmentAdapter.CONTENT[position % CONTENT.length];
    }

    @Override
    public int getIconResId(int index) {
      return ICONS[index % ICONS.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}