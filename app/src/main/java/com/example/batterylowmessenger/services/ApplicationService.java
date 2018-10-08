package com.example.batterylowmessenger.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.IBinder;
import android.telephony.SmsManager;

import com.example.batterylowmessenger.LoadData;
import com.example.batterylowmessenger.data.Contact;
import com.example.batterylowmessenger.receivers.AlarmReceiver;
import com.example.batterylowmessenger.receivers.BootReceiver;
import com.example.batterylowmessenger.repository.ContactsRepository;
import com.example.batterylowmessenger.sharedPreferenceStorage.ApplicationSharedPreference;

import java.util.ArrayList;
import java.util.List;



public class ApplicationService extends Service {
    private static final String TAG = "YourService";
    public static final String BATTERY_UPDATE = "battery_update";
    public static final String HANDLE_REBOOT = "first_start";
    ContactsRepository contactsRepository = new ContactsRepository();




    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        if (intent != null && intent.hasExtra(BootReceiver.ACTION_BOOT)){

            AlarmReceiver.startAlarms(ApplicationService.this.getApplicationContext());
        }
        if (intent != null && intent.hasExtra(BATTERY_UPDATE)){

            new BatteryCheckAsync().execute();
        }
        if (intent != null && intent.hasExtra(HANDLE_REBOOT)){

            AlarmReceiver.startAlarms(ApplicationService.this.getApplicationContext());
        }


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class BatteryCheckAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            String batteryChargeLevel = ApplicationSharedPreference.getStoredBatteryLevel(getBaseContext());

            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = ApplicationService.this.registerReceiver(null, ifilter);

            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;


            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int percent = (level * 100) / scale;

            if(!isCharging) {

                if (batteryChargeLevel != null &&
                        batteryChargeLevel.length() > 0) {

                    boolean flag = false;

                    while (percent == Integer.parseInt(batteryChargeLevel)) {

                        if (!flag) {
                            fillListCheckedContacts(getBaseContext());
                            flag = true;
                        }
                    }

                }
                else if (batteryChargeLevel == null || batteryChargeLevel.length() == 0) {

                    boolean flag = false;

                    while (percent == 3) {

                        if (!flag) {
                            fillListCheckedContacts(getBaseContext());
                            flag = true;
                        }
                    }
                }
            }

            return null;
        }


        public void fillListCheckedContacts(final Context context){

            contactsRepository.getCheckedContactList(new LoadData.LoadContactCallback() {
                @Override
                public void onContactsLoaded(List<Contact> tasks) {
                    List<String>list = new ArrayList<>();
                    for(Contact contact:tasks){
                        list.add(contact.getContactNumber());
                    }
                    sendMessageToContacts(list,context);
                }

                @Override
                public void onDataNotAvailable() {}
            });
        }


        public void sendMessageToContacts(List<String> list,Context context) {
            String message = ApplicationSharedPreference.getStoredMessage(context);
            for(String number:list){
                sendSMSMessage(number, message);
            }
        }


        public void sendSMSMessage(String contact,String message) {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(contact, null, message, null, null);
        }

    }
}
