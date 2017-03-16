package com.app.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.fragments.RegisterClinicFragment;
import com.app.fragments.RegisterHospitalFragment;

/**
 * Created by masood on 9/13/15.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context _context;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context=context;

    }
    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        switch(position){
            case 0:
               // 1st tab
                f= RegisterClinicFragment.newInstance(_context);
                break;
            case 1:
                f= RegisterHospitalFragment.newInstance(_context);
                break;
        }
        return f;
    }
    @Override
    public int getCount() {
        return 2;
    }
}
