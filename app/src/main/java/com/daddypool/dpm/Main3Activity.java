package com.daddypool.dpm;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.daddypool.dpm.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Main3Activity extends AppCompatActivity  {
    private TextView textView;
    private TextView textDiffData2;
    private TextView textHashData2;
    private TextView textBalData2;
    private TextView textPaidData2;
    private TextView textWorkersData2;
    private TextView textMinerData2;
    private TextView textHashu2;
    //    private TextView textLuckDayData;
//    private TextView textLuckHoursData;
//    private TextView textSharesData;
    private EditText editText2;
    private String fileName = "Address.txt";
    private String text ="";
    private LineChart mChart;
    private int[] [] HashHistorys = null;
    private int MaxHash;
    private int MinHash;
    private String[] addresslist;
    private String serveraddress ="zny.daddy-pool.work";
    private String Currency ="bitzeny";
    private String Daddy ="DaddyPool";
    private String Macyan ="MacyanPool-ZENY";
    private String Macyan2 ="MacyanPool-BELL";
    private String Macyan3 ="MacyanPool-MONA";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    // サーバー選択肢
    private String spinnerItems[] = {"DaddyPool", "MacyanPool-ZENY","MacyanPool-BELL","MacyanPool-MONA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        // アイコンを指定
        toolbar.setNavigationIcon(R.drawable.daddypool48);

        //>>  広告用
//        AdView mAdView = findViewById(R.id.adView2);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        //<<  広告用

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main3, menu);
        return true;
    }

    // オプションメニューのアイテムが選択されたときに呼び出されるメソッド
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        TextView varTextView = (TextView) findViewById(R.id.textView);
        switch (item.getItemId()) {
            case R.id.item0:
                Intent varIntent_back =
                        new Intent(Main3Activity.this, aboutDPM.class);
                startActivity(varIntent_back);
                return true;
            case R.id.item1:
                Intent varIntent2 =
                        new Intent(Main3Activity.this, faucet.class);
                startActivity(varIntent2);
                return true;
            case R.id.item2:
                Intent varIntent1 =
                        new Intent(Main3Activity.this, OssLicensesMenuActivity.class);
                startActivity(varIntent1);
                return true;
            case R.id.item3:
                Intent varIntent3 =
                        new Intent(Main3Activity.this, Terms.class);
                startActivity(varIntent3);
                return true;
            case R.id.item4:
                Intent varIntent4 =
                        new Intent(Main3Activity.this, Main3Activity.class);
                startActivity(varIntent4);
                finish();
                return true;
//            case R.id.item5:
//                Intent varIntent5 =
//                        new Intent(MainActivity.this, OssLicensesMenuActivity.class);
//                startActivity(varIntent5);
//                return true;
        }
        return super.onOptionsItemSelected(item);
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
            View rootView = inflater.inflate(R.layout.fragment_main3, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.Address_select, getArguments().getInt(ARG_SECTION_NUMBER)));
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
    }
}
