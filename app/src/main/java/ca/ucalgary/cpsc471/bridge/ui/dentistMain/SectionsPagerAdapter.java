package ca.ucalgary.cpsc471.bridge.ui.dentistMain;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ca.ucalgary.cpsc471.bridge.R;
import ca.ucalgary.cpsc471.bridge.ui.AboutFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_4, R.string.tab_text_2, R.string.tab_text_5, R.string.tab_text_3,R.string.tab_text_6};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = null;
        if (position == 0){
            fragment = DentistViewFragment.newInstance(null ,null);
        }
        else if (position == 1){
            fragment = DentistBookFragment.newInstance(null, null);
        }
        else if (position == 2){
            fragment = DentistSearchFragment.newInstance(null, null);
        }
        else if (position == 3){
            fragment = DentistAcctFragment.newInstance(null, null);
        }
        else if (position == 4){
            fragment = AboutFragment.newInstance(null, null);
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}