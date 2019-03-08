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
import android.util.Log;
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

/**стартовая активность лаунчера, реализует слушатель обработки собітий навигации */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /** */

    /**тулбар */
    private Toolbar toolbar;
    /**ViewModel для управления навигацией */
    private InteractionViewModel interactionViewModel;
    /**база данных */
    MyDatabase database;
    /**интерфейс доступа к базе данных */
    ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**инициализация тулбара */
        toolbar = findViewById(R.id.toolbar);
        /**тулбар будет использовать свойства экшенбара */
        setSupportActionBar(toolbar);
        setDrawerLayout();
        /**инициализация ViewModel для навигации */
        interactionViewModel = obtainViewModel(this);
        observeInteractionViewModel(interactionViewModel);
        /**при первой загрузке приложения по умолчанию открівается HomeFragment */
        if (savedInstanceState == null) {
            openFragment(HomeFragment.newInstance());
        }
        /**инициализация базы данных Рум и интерфейса доступа к ней */
        database = App.getInstance().getDatabase();
        contactDao = database.contactDao();
    }


    /**обработка нажатия на аппаратную кнопку Назад */
    @Override
    public void onBackPressed() {
        tellFragments();
    }


    /**получение списка всех фрагментов.Установка обработчика нажатия аппаратной кнопки Назад для фрагментов */
    private void tellFragments(){
        //получает список фрагментов
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment f : fragments){
            //для ContactFragment нажатие кнопки Назад будет вызывать метод onBackPressed() этого фрагмента
            if (f != null && f instanceof ContactFragment){
                ((ContactFragment) f).onBackPressed();
            }
            //для HomeFragment и InfoFragment нажатие кнопки Назад будет вызывать закрытие приложения
            if (f != null && (f instanceof HomeFragment||f instanceof InfoFragment)){
                finish();
            }
        }
    }

    /**создает фабрику,которая создает навигационную ViewModel и возвращает ее  */
    public static InteractionViewModel obtainViewModel(FragmentActivity activity) {
        InteractionModelFactory factory = InteractionModelFactory.getInstance();
        return ViewModelProviders.of(activity, factory).get(InteractionViewModel.class);
    }

    /**установка меню шторки */
    private void setDrawerLayout(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    /**подписка на события, приходящие из навигационной ViewModel */
    private void observeInteractionViewModel(final InteractionViewModel model){
        model.getSelected().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String message) {
                if (message != null) {
                    if (message.equals("openHomeFragment")) {
                        openFragment(HomeFragment.newInstance());
                    }
                    /**открыть фрагмент со списком контактов */
                    if (message.equals("openContactList")) {
                        openFragment(ContactFragment.newInstance(false));
                    }
                    /**открыть фрагмент для выбора сообщения */
                    if (message.equals("openMessageFragment")) {
                        openFragment(MessageFragment.newInstance());
                    }
                    if (message.equals("openBatteryLevelFragment")){
                        openFragment(BatteryLevelFragment.newInstance());
                    }
                    if (message.equals("openInfoFragment")) {
                        openFragment(InfoFragment.newInstance());
                    }
                }
            }
        });
    }

    /**обработка нажатия на элементы меню в шторке */
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


    /**открывает фрагмент.Если в контейнере уже есть фрагмент,заменяет его на новый */
    private void openFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
        ft.addToBackStack(null);
    }

    /**метод для локализации */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    /**метод для локализации */
    private void updateViews(String languageCode) {
        LocaleHelper.setLocale(getApplication(), languageCode);
        updateScreen();
    }

    /**установка меню на тулбаре */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    /**обработка нажатия на элементы меню в тулбаре */
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

    /**обновление текущего активити  */
    public void updateScreen(){
        Intent intent= new Intent(MainActivity.this,MainActivity.class);
        finish();
        startActivity(intent);
    }
}
