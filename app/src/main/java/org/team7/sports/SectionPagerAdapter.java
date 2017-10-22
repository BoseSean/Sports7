package org.team7.sports;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MessageFragment();
            case 1:
                return new GameFragment();
            case 2:
                return new TeamFragment();
            case 3:
                return new FriendFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

}
