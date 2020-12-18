package com.procialize.bayer2020.ui.attendee.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.lifecycle.ViewModel;

public class AttendeeDetailsViewModel extends ViewModel {


    public void saveContact(Context context,String name,
                            String company,
                            String mobile,
                            String designation,String email) {
        try {

            Intent intent = new Intent(
                    ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
                    Uri.parse("tel:" + mobile));
            intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
            intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, mobile);
            intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, designation);
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
            intent.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
