package com.java.sortsmsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.Toast;

import com.java.sortsmsapp.pojo.SMSData;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static MainActivity inst;
    private SMSListAdapter adapter;
    private List<SMSData> listData;
    private RecyclerView recyclerView;
    private SMSData data;
    private Timestamp timestamp;
    SimpleDateFormat sdf;
    Button knownBtn;
    Button unknownBtn;
    ContentResolver contentResolver;
    List<String> contactlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_PHONE_NUMBERS,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.SEND_SMS},
                PackageManager.PERMISSION_GRANTED);

        knownBtn = (Button) findViewById(R.id.btnKnown);
        unknownBtn = (Button) findViewById(R.id.btnUnknown);

        knownBtn.setOnClickListener(e ->{
            refreshSmsInbox(contacts(), true);
        });

        unknownBtn.setOnClickListener(e ->{
            refreshSmsInbox(contacts(), false);
        });

        listData = new ArrayList<>();
        listData.add(new SMSData("0277678387", "Hello World", "2022-02-01"));
        listData.add(new SMSData("0277678387", "Hello World", "2022-02-01"));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new SMSListAdapter(listData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        refreshSmsInbox(contacts(), true);

    }

    public List<String> contacts()
    {
        contentResolver = getContentResolver();
        Cursor contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null,
                null,
                null);

        List<String> contactlist = new ArrayList<>();

        while (contacts.moveToNext()) {

            @SuppressLint("Range")
            String number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            @SuppressLint("Range")
            int type = contacts.getInt(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));



            number = StringUtils.replace(number, " ", "");
            System.out.println("Phone Number>>>"+number);
            contactlist.add(number);
        }

        return contactlist;

    }


    public void refreshSmsInbox(List<String> smsList, boolean known) {
        contentResolver = getContentResolver();
        Cursor smscursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, "date desc");
        int indexMsg = smscursor.getColumnIndex("body");
        int indexPhone = smscursor.getColumnIndex("address");
        int indexDate = smscursor.getColumnIndex("date");
        if (indexMsg < 0 || !smscursor.moveToFirst())
            return;

        listData = new ArrayList<>();

        do {
            if (smscursor.getString(smscursor.getColumnIndexOrThrow("type")).contains("1")) {
                data = new SMSData();
                sdf = new SimpleDateFormat("dd-MM-yy HH:mm");
                data.setPhone(smscursor.getString(indexPhone));
                data.setMessage(smscursor.getString(indexMsg));
                timestamp = new Timestamp(Long.valueOf(smscursor.getString(indexDate)));
                data.setDate(""+sdf.format(new Date(timestamp.getTime())));

                if(known && smsList.contains(data.getPhone()))
                {
                    listData.add(data);
                }
                else if(!known && !smsList.contains((data.getPhone())))
                {
                    listData.add(data);
                }
                else
                {
                    //listData.clear();
                }

            }

        } while (smscursor.moveToNext());

        System.out.println("Final Data>>>"+listData.toString());

        adapter = new SMSListAdapter(listData);
        recyclerView.setAdapter(adapter);
    }

    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    //called from SmsBroadcastReceiver when new sms is received
    public void updateList(final String smsMessage) {
        refreshSmsInbox(contacts(), true);
    }
}