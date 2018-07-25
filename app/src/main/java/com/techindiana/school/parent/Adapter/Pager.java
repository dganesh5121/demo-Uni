package com.techindiana.school.parent.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.techindiana.school.parent.Fragment.AboutUs;
import com.techindiana.school.parent.Fragment.ContactUs;
import com.techindiana.school.parent.Fragment.Feedback;
import com.techindiana.school.parent.Fragment.Management;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                ContactUs tab1 = new ContactUs();
                return tab1;
            case 1:
                Management tab2 = new Management();
                return tab2;
            case 2:
                Feedback tab3 = new Feedback();
                return tab3;
            case 3:
                AboutUs tab4 = new AboutUs();
                return tab4;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}