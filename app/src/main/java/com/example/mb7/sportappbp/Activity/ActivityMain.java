package com.example.mb7.sportappbp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.mb7.sportappbp.BusinessLayer.BackgroundClock;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMessage;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;
import com.example.mb7.sportappbp.MotivationMethods.TrainingReminder;
import com.example.mb7.sportappbp.R;
import com.example.mb7.sportappbp.Fragments.TabFragment;
import com.example.mb7.sportappbp.Fragments.TbNotificationContent;
import com.example.mb7.sportappbp.Fragments.TbReportContent;
import com.example.mb7.sportappbp.Fragments.TbTaskContent;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.mb7.sportappbp.R.id.container;

public class ActivityMain extends AppCompatActivity {


    /** sdf sd
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    // list which consists of all used motivation methods
    private LinkedList<MotivationMethod> fixMotivationMethods = new LinkedList<MotivationMethod>();
    private LinkedList<MotivationMethod> variableMotivationMethods = new LinkedList<MotivationMethod>();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private final String mainColor = "#2648FF";
    public  static  User mainUser ;
    public static ActivityMain activityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set the main URL
        DAL_Utilities.DatabaseURL = "https://sportapp-cbd6b.firebaseio.com/players/";

        // create the current User
        mainUser = User.Create("TestUser");
        activityMain = this;

        // create a new motivation method and add it to the list of methods
        TrainingReminder t = new TrainingReminder(this);
        fixMotivationMethods.add(t);
        MotivationMessage m = new MotivationMessage(this);
        variableMotivationMethods.add(m);

        // initialize the settings activity
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // TODO activate line below for init settings every start
        // preferences.edit().putBoolean("initialized", false).apply();
        /*
        Intent i = new Intent(this, ActivityMotivationMessage.class);
        startActivity(i);
        if(!preferences.getBoolean("initialized",false)) {
            Intent settingInitializerIntent = new Intent(this, ActivitySettingInitializer.class);
            startActivity(settingInitializerIntent);
        }
        */

        // start background clock
        BackgroundClock backgroundClock = new BackgroundClock();
        backgroundClock.startClock(this,fixMotivationMethods,variableMotivationMethods);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setTabLayout();
    }

    // Set all the properties of the main TabLayout in the main page here
    private void  setTabLayout()
    {
        this.getResources().getColor(R.color.colorMain);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(mainColor));
        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#808080"),Color.parseColor(mainColor) );
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item= menu.findItem(R.id.action_settings);
        return true;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            startActivity(new Intent(this,ActivitySettings.class));
            return true;
        }
        return super.onContextItemSelected(item);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,ActivitySettings.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     * and the factory for creating fragments
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
        public static TabFragment newInstance(int sectionNumber, Activity activity) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            TabFragment tabFragment = null;
            switch(sectionNumber)
            {
                case 1:
                    tabFragment = new TbTaskContent();
                    tabFragment.Initialize(activity,"Tasks");
                    return tabFragment;
                case 2:
                    tabFragment = new TbNotificationContent();
                    tabFragment.Initialize(activity,"Notfikation");
                    return tabFragment;
                case 3:
                    tabFragment = new TbReportContent();
                    tabFragment.Initialize(activity,"Berichte");
                    return tabFragment;
                default:
                    tabFragment = new TbNotificationContent();
                    tabFragment.Initialize(activity,"Notfikation");
                    return tabFragment;
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        List<TabFragment> fragments = new ArrayList<TabFragment>();
        Activity activity ;
        public SectionsPagerAdapter(FragmentManager fm, Activity act) {
            super(fm);
            activity = act;
            for (int i = 1; i < 4; i++) {
                TabFragment tabFragment = PlaceholderFragment.newInstance(i,activity);
                fragments.add(tabFragment);
            }
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (!fragments.isEmpty() && fragments.size() >= position + 1) {
                //Log.d("Title", fragments.get(position).getTitle());
                return fragments.get(position).getTitle();
            }
            else
                return "No Value";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        for(int i = 0; i < fixMotivationMethods.size(); i++) {
            fixMotivationMethods.get(i).evaluatePermissionResults(requestCode, permissions, grantResults);
        }
        for(int i = 0; i < variableMotivationMethods.size(); i++) {
            variableMotivationMethods.get(i).evaluatePermissionResults(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // open new activity at the specified tab
        int startTab = intent.getIntExtra("startTab",-1);
        if(startTab != -1 ) {
            mViewPager.setCurrentItem(startTab);
        }
    }
}
