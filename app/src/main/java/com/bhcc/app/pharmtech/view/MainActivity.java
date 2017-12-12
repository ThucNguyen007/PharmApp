package com.bhcc.app.pharmtech.view;

import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.data.MedicineLab;
import com.bhcc.app.pharmtech.view.about.AboutFragment;
import com.bhcc.app.pharmtech.view.administrator.Settings;
import com.bhcc.app.pharmtech.view.filter.FilterFragment;
import com.bhcc.app.pharmtech.view.legal.LegalFragment;
import com.bhcc.app.pharmtech.view.navigation.ReplaceFragmentCommand;
import com.bhcc.app.pharmtech.view.quiz.SelectQuizFragment;
import com.bhcc.app.pharmtech.view.review.ReviewFragment;
import com.bhcc.app.pharmtech.view.study.CardContainerFragment;
import com.bhcc.app.pharmtech.view.study.MedicineListFragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String fileName  = "ReviewInfo.txt";
    private static final String ARG_MEDICINE_ID = "arg: medicine id";


    private static final int ASCENDING_ID = 0;
    private static final int DESCENDING_ID = 1;
    private static final int ASCENDING_BRAND_ID = 2;
    private static final int DESCENDING_BRAND_ID = 3;


    private String mGestureType;
    private GestureDetector mGestureDetector;

    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        loadDefaultFragment();
        createReviewFile();

        // To make the drug of the day pop up more often, uncomment this code in onResume()
        // As of now, it pops up when the app is (re)opened
        FragmentManager manager = getSupportFragmentManager();
        DailyDrugDialogFragment dailyDrugDialogFragment =
                DailyDrugDialogFragment
                        .newInstance(MedicineLab.get(getApplication()).getRandomMedicine());
        dailyDrugDialogFragment.show(manager, ARG_MEDICINE_ID);

    }

    /**
     * If there's no fragment in container, display study fragment
     */
    private void loadDefaultFragment() {
        ReplaceFragmentCommand.startNewFragment(this, new MedicineListFragment(), false);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int itemId = item.getItemId();

        if (itemId == R.id.study) {
            ReplaceFragmentCommand.startNewFragment(this, new MedicineListFragment(), false);
        }

        if (itemId == R.id.quiz) {
            ReplaceFragmentCommand.startNewFragment(this, new SelectQuizFragment(), false);
        }

        if (itemId == R.id.filter) {
            ReplaceFragmentCommand.startNewFragment(this, new FilterFragment(), false);
        }

        if (itemId == R.id.sort) {
            showRadioButtonDialog();
        }

        if (itemId == R.id.review) {
            ReplaceFragmentCommand.startNewFragment(this, new ReviewFragment(), false);
        }

        if (itemId == R.id.legal) {
            ReplaceFragmentCommand.startNewFragment(this, new LegalFragment(), false);
        }
        if (itemId == R.id.about) {
            ReplaceFragmentCommand.startNewFragment(this, new AboutFragment(), false);
        }
        if (itemId == R.id.administrator) {
            ReplaceFragmentCommand.startNewFragment(this, new Settings(), false);
        }
        closeDrawer();

        return false;
    }

    private boolean closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (!closeDrawer()) {
            super.onBackPressed();
        }
    }


    /**
     * To Show dialog for the sorting selection
     */
    private void showRadioButtonDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choose_sorting_dialog);

        List<String> stringList=new ArrayList<>();  // list to hold choices
        // add choices
        stringList.add("Ascending (Generic name)");
        stringList.add("Descending (Generic name)");
        stringList.add("Ascending (Brand name)");
        stringList.add("Descending (Brand name)");

        // Radio group to hold radio buttons
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        // add radio buttons to radio group
        RadioButton rbAscending = new RadioButton(this);
        rbAscending.setText(stringList.get(ASCENDING_ID));
        rbAscending.setId(ASCENDING_ID);
        rg.addView(rbAscending);

        RadioButton rbDescending = new RadioButton(this);
        rbDescending.setText(stringList.get(DESCENDING_ID));
        rbDescending.setId(DESCENDING_ID);
        rg.addView(rbDescending);

        RadioButton rbAscendingBrand = new RadioButton(this);
        rbAscendingBrand.setText(stringList.get(ASCENDING_BRAND_ID));
        rbAscendingBrand.setId(ASCENDING_BRAND_ID);
        rg.addView(rbAscendingBrand);

        RadioButton rbDescendingBrand = new RadioButton(this);
        rbDescendingBrand.setText(stringList.get(DESCENDING_BRAND_ID));
        rbDescendingBrand.setId(DESCENDING_BRAND_ID);
        rg.addView(rbDescendingBrand);

        TextView tvOK = (TextView) dialog.findViewById(R.id.choose_sorting_ok_button);
        tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = rg.getCheckedRadioButtonId();
                switch (id) {
                    case ASCENDING_ID:
                        MedicineLab.get(getApplication()).sortAscending();
                        Log.i("test3", MedicineLab.get(getApplication()).getMedicines().get(0).getGenericName());
                        break;
                    case DESCENDING_ID:
                        MedicineLab.get(getApplication()).sortDescending();
                        Log.i("test3", MedicineLab.get(getApplication()).getMedicines().get(0).getGenericName());
                        break;
                    case ASCENDING_BRAND_ID:
                        MedicineLab.get(getApplication()).sortAscendingBrand();
                        Log.i("test3", MedicineLab.get(getApplication()).getMedicines().get(0).getGenericName());
                        break;
                    case DESCENDING_BRAND_ID:
                        MedicineLab.get(getApplication()).sortDescendingBrand();
                        Log.i("test3", MedicineLab.get(getApplication()).getMedicines().get(0).getGenericName());
                        break;

                }
                dialog.dismiss();
                loadDefaultFragment();
            }
        });

        dialog.show();
    }

    /**
     * To Create a file to hold review files information
     */
    private void createReviewFile() {
        File file = new File(getApplicationContext().getFilesDir(),fileName);

        if(!file.exists()) {
            try {
                PrintWriter printWriter = new PrintWriter(file);
                printWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void showDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = this.getLayoutInflater().inflate(R.layout.fragment_card_container, null);
        builder.setView(v)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void onResume(){
        super.onResume();
        // Make the drug of the "day" pop up each time the user returns to the list of drugs
//        FragmentManager manager = getSupportFragmentManager();
//        DailyDrugDialogFragment dailyDrugDialogFragment =
//                DailyDrugDialogFragment
//                        .newInstance(MedicineLab.get(getApplication()).getRandomMedicine());
//        dailyDrugDialogFragment.show(manager, ARG_MEDICINE_ID);
    }

}
