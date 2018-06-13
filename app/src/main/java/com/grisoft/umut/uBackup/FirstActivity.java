package com.grisoft.umut.uBackup;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.Telephony;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.io.File;
import java.util.ArrayList;
import java.util.List;


import static com.grisoft.umut.uBackup.R.drawable.banner;

public class FirstActivity extends AppCompatActivity {
    private static String filepath = "uBackup";
    public ArrayList<String> arr1;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_first, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = inflater.inflate(R.layout.first_backup, container, false);
                    final Button json = (Button) rootView.findViewById(R.id.circle_json);
                    json.setOnLongClickListener(new View.OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.flipping);
                            anim.setTarget(json);
                            anim.setDuration(1000);
                            anim.start();
                            ObjectAnimator anim2 = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.reverseflipping);
                            anim2.setTarget(json);
                            anim2.setDuration(1000);
                            anim2.start();
                            return false;
                        }
                    });
                    json.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.flipping);
                            anim.setTarget(json);
                            anim.setDuration(1000);
                            anim.start();
                            Snackbar.make(v, "Json Formatı yedeklenmek üzere seçildi.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            new CountDownTimer(500, 1000) {

                                public void onTick(long millisUntilFinished) {

                                }

                                public void onFinish() {
                                    json.setBackgroundResource(R.drawable.circle_tersok);
                                }
                            }.start();

                        }
                    });
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.recyclerviewlist, container, false);

                    final FragmentActivity c = getActivity();
                    final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(c);
                    recyclerView.setLayoutManager(layoutManager);




                    break;
                case 3:
                    textView.setText("3.sayfa");
                    break;
            }


            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Yedek Al";
                case 1:
                    return "Geri Yükle";
                case 2:
                    return "Beta";
            }
            return null;
        }

    }
    public static class FileAc extends ListActivity {
        public File[] allfiles;

        private ArrayList<String> GetFiles(String path) {
            ArrayList<String> arr2 = new ArrayList<>();
            File file = new File(path + "/" + filepath);
            allfiles = file.listFiles();

/*            Arrays.sort(allfiles, new Comparator<File>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                public int compare(File f1, File f2) {
                    return Long.compare(f2.lastModified(), f1.lastModified());
                }
            });*/
            if (allfiles != null) {
                if (allfiles.length == 0) {
                    return null;
                } else {
                    for (int i = 0; i < allfiles.length; i++) {
                        arr2.add(allfiles[i].getName());
                    }
                }
            }
            return arr2;
        }
    }
}
