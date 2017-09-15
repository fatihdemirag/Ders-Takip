package fatihdemirag.net.dersprogram;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        Calendar calendar;
        Cursor cursor;
        Ders ders;
        private static ListView liste;
        ArrayList<Ders> dersListe=new ArrayList<>();
        DbHelper dbHelper;
        private static TextView gunText;
        Custom_Adapter custom_adapter;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            liste=(ListView)rootView.findViewById(R.id.gunlukListe);
            gunText=(TextView)rootView.findViewById(R.id.gunText);

            dbHelper=new DbHelper(getActivity());

            calendar= Calendar.getInstance();
            int gun=calendar.get(Calendar.DAY_OF_WEEK);
            switch (gun)
            {
                case Calendar.MONDAY:
                    GunlukDers("Pazartesi");
                    gunText.setText("Pazartesi");
                    break;
                case Calendar.TUESDAY:
                    GunlukDers("Salı");
                    gunText.setText("Salı");
                    break;
                case Calendar.WEDNESDAY:
                    GunlukDers("Çarşamba");
                    gunText.setText("Çarşamba");
                    break;
                case Calendar.THURSDAY:
                    GunlukDers("Perşembe");
                    gunText.setText("Perşembe");
                    break;
                case Calendar.FRIDAY:
                    GunlukDers("Cuma");
                    gunText.setText("Cuma");
                    break;
                case Calendar.SATURDAY:
                    GunlukDers("Cumartesi");
                    gunText.setText("Cumartesi");
                    break;
                case Calendar.SUNDAY:
                    GunlukDers("Pazar");
                    gunText.setText("Pazar");
                    break;
            }


            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
        public void GunlukDers(String gun)
        {
            cursor= dbHelper.getAllData();
            while (cursor.moveToNext()) {
                if (cursor.getString(2).equals(gun)) {
                    ders = new Ders();
                    ders.setDersAdi(cursor.getString(1));
                    ders.setDersBaslangicSaati(cursor.getString(3));
                    ders.setDersBitisSaati(cursor.getString(4));
                    ders.setDersId((cursor.getInt(0)));
                    ders=new Ders(ders.getDersAdi(),ders.getDersBaslangicSaati(),ders.getDersBitisSaati(),ders.getDersId());
                    dersListe.add(ders);
                }
            }
            custom_adapter=new Custom_Adapter(getActivity(),dersListe);

            liste.setAdapter(custom_adapter);
        }
        @Override
        public void onResume() {
            custom_adapter.clear();
            custom_adapter.notifyDataSetChanged();
            GunlukDers(gunText.getText()+"");
            super.onResume();
        }

    }

}
