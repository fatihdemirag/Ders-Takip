package fatihdemirag.net.dersprogram.helpers;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.ui.Fragment.Days.Fragments.Friday;
import fatihdemirag.net.dersprogram.ui.Fragment.Days.Fragments.Monday;
import fatihdemirag.net.dersprogram.ui.Fragment.Days.Fragments.Saturday;
import fatihdemirag.net.dersprogram.ui.Fragment.Days.Fragments.Sunday;
import fatihdemirag.net.dersprogram.ui.Fragment.Days.Fragments.Thursday;
import fatihdemirag.net.dersprogram.ui.Fragment.Days.Fragments.Tuesday;
import fatihdemirag.net.dersprogram.ui.Fragment.Days.Fragments.Wednesday;
import fatihdemirag.net.dersprogram.ui.Fragment.Syllabus;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES =
            new int[] {R.string.pazartesi, R.string.sali, R.string.carsamba,R.string.persembe,R.string.cuma,R.string.cumartesi,R.string.pazar};
    private final Context mContext;

    public TabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Monday();
            case 1:
                return new Tuesday();
            case 2:
                return new Wednesday();
            case 3:
                return new Thursday();
            case 4:
                return new Friday();
            case 5:
                return new Saturday();
            case 6:
                return new Sunday();
            default:
                return new Syllabus();
        }
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
    @Override
    public int getCount() {
        return 7;
    }
}
