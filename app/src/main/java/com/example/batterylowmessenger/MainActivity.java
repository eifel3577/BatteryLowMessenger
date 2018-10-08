package com.example.batterylowmessenger;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.batterylowmessenger.data.ContactDao;
import com.example.batterylowmessenger.data.MyDatabase;
import com.example.batterylowmessenger.view.AboutVersionFragment;
import com.example.batterylowmessenger.view.BatteryLevelFragment;
import com.example.batterylowmessenger.view.ContactFragment;
import com.example.batterylowmessenger.view.HomeFragment;
import com.example.batterylowmessenger.view.InfoFragment;
import com.example.batterylowmessenger.view.MessageFragment;
import com.example.batterylowmessenger.view.UserRulesFragment;
import com.example.batterylowmessenger.viewModels.InteractionViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private InteractionViewModel interactionViewModel;
    MyDatabase database;
    ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setDrawerLayout();
        interactionViewModel = obtainViewModel(this);
        observeInteractionViewModel(interactionViewModel);

        if (savedInstanceState == null) {
            openFragment(HomeFragment.newInstance());
        }
        database = App.getInstance().getDatabase();
        contactDao = database.contactDao();
    }

    /**вызывается при нажатиях аппаратной кнопки Назад во фрагментах */
    @Override
    public void onBackPressed() {
        tellFragments();
    }

    /**обработка нажатий аппаратной кнопки Назад во фрагментах */
    private void tellFragments(){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment f : fragments){
            if (f != null && f instanceof ContactFragment){
                ((ContactFragment) f).onBackPressed();
            }

            if (f != null && (f instanceof HomeFragment||f instanceof InfoFragment)){
                finish();
            }
        }
    }

    public static InteractionViewModel obtainViewModel(FragmentActivity activity) {
        InteractionModelFactory factory = InteractionModelFactory.getInstance();
        return ViewModelProviders.of(activity, factory).get(InteractionViewModel.class);
    }

    private void setDrawerLayout(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**наблюдение за источниками событий навигации.События кладут во InteractionViewModel
     * фрагменты */
    private void observeInteractionViewModel(final InteractionViewModel model){
        model.getSelected().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String message) {
                if (message != null) {
                    if (message.equals("openHomeFragment")) {
                        openFragment(HomeFragment.newInstance());
                    }
                    if (message.equals("openContactList")) {
                        openFragment(ContactFragment.newInstance(false));
                    }
                    if (message.equals("openMessageFragment")) {
                        openFragment(MessageFragment.newInstance());
                    }
                    if (message.equals("openInfoFragment")) {
                        openFragment(InfoFragment.newInstance());
                    }
                }
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            openFragment(HomeFragment.newInstance());
        }

        if(id == R.id.changeBatteryLevel){
            openFragment(BatteryLevelFragment.newInstance());
        }

        if(id == R.id.edit_contacts){
            openFragment(ContactFragment.newInstance(false));
        }

        if(id == R.id.update_contacts){
            openFragment(ContactFragment.newInstance(true));
        }

        if(id == R.id.edit_message){
            openFragment(MessageFragment.newInstance());
        }

        if(id == R.id.rules){
            openFragment(UserRulesFragment.newInstance());
        }

        if(id == R.id.about_version){
            AboutVersionFragment versionFragment = new AboutVersionFragment();
            versionFragment.show((this).getSupportFragmentManager().beginTransaction(), "dialog_licenses");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void openFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
        ft.addToBackStack(null);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }


    private void updateViews(String languageCode) {
        LocaleHelper.setLocale(getApplication(), languageCode);
        updateScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_russian:
                updateViews("ru");
                return true;

            case R.id.menu_item_english:
                updateViews("en");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public void updateScreen(){
        Intent intent= new Intent(MainActivity.this,MainActivity.class);
        finish();
        startActivity(getIntent());
    }
}
