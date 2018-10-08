package com.example.batterylowmessenger.viewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

import com.example.batterylowmessenger.CheckedContact;
import com.example.batterylowmessenger.repository.ContactsRepository;

public class BatteryLevelFragmentViewModel extends ViewModel {

    private MutableLiveData<String> batteryLevel =new MutableLiveData<>();
    private MutableLiveData<Boolean>isDataCorrect = new MutableLiveData<>();
    public final ObservableBoolean enableSaveButton = new ObservableBoolean(false);
    public final ObservableField<String> emptyText = new ObservableField<>();
    private final ContactsRepository contactsRepository = new ContactsRepository();
    private MutableLiveData<Boolean>isContactChecked = new MutableLiveData<>();

    public MutableLiveData<String> getBatteryLevel(){
        return batteryLevel;
    }

    public MutableLiveData<Boolean> getIsDataCorrect(){
        return isDataCorrect;
    }

    public MutableLiveData<Boolean> getIsContactChecked() {
        return isContactChecked;
    }

    public void checkContactList(){
        contactsRepository.getCheckedList(new CheckedContact.CheckedContactCallback() {
            @Override
            public void onContactsLoaded(boolean resultCheck) {
                if(resultCheck){
                    isContactChecked.postValue(false);
                }
                if(!resultCheck){
                    isContactChecked.postValue(true);
                }
            }
        });
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(s.length()>0 && !(s.toString().matches("^0"))) {
            if(Integer.parseInt(s.toString())!=0){
                if(s.toString().startsWith("0")){
                    batteryLevel.postValue(s.toString().substring(1));
                }
                batteryLevel.postValue(s.toString());
                enableSaveButton.set(true);
            }

            else {
                isDataCorrect.setValue(false);
                enableSaveButton.set(false);

            }
        }
        else {
            enableSaveButton.set(false);
            emptyText.set("");
        }
    }
}