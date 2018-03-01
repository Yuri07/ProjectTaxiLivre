package com.rsm.yuri.projecttaxilivredriver.main.ui;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Switch;

import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.avaliation.ui.AvaliationFragment;
import com.rsm.yuri.projecttaxilivredriver.home.ui.HomeFragment;
import com.rsm.yuri.projecttaxilivredriver.money.ui.MoneyFragment;
import com.rsm.yuri.projecttaxilivredriver.profile.ui.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.switch_main)
    Switch switchMain;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    //@Inject
    FragmentManager fragmentManager;
    HomeFragment homeFragment;
    MoneyFragment moneyFragment;
    AvaliationFragment avaliationFragment;
    ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        /*bottomNavigation.inflateMenu(R.menu.main_navigation_items);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.action_home:
                        //fragment = new SearchFragment();
                        break;
                    case R.id.action_money:
                        //fragment = new CartFragment();
                        break;
                    case R.id.action_avaliation:
                        //fragment = new DealsFragment();
                        break;
                    case R.id.action_profile:
                        //fragment = new DealsFragment();
                        break;
                }
                /*final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });*/


        setupBottomNavigationView();

        setSupportActionBar(toolbar);

        setupInjection();

        fragmentManager.beginTransaction()
                .add(R.id.content_frame, homeFragment)
                .commit();

    }

    private void setupInjection() {

        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        moneyFragment = new MoneyFragment();
        avaliationFragment = new AvaliationFragment();
        profileFragment = new ProfileFragment();

    }

    private void setupBottomNavigationView() {
        BottomNavigationViewHelper.removeShiftMode(bottomNavigation);

        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                Log.d("d", "actionHome");
                                displayFragment(homeFragment);
                                break;
                            case R.id.action_money:
                                displayFragment(moneyFragment);
                                Log.d("d", "actionMoney");
                                break;
                            case R.id.action_avaliation:
                                displayFragment(avaliationFragment);
                                Log.d("d", "actionAvaliation");
                                break;
                            case R.id.action_profile:
                                displayFragment(profileFragment);
                                Log.d("d", "actionProfile");
                                break;
                        }
                        return true;
                    }
                });
    }

    private void displayFragment(Fragment fragment) {

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

    }
}
