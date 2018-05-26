package com.example.mahesh.alarmlocator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahesh.alarmlocator.model.Alarmmodel;
import com.example.mahesh.alarmlocator.model.MapAdressModel;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddAlarmActivity extends AppCompatActivity {
    TextView addadress;
    TextView addAlarm;
    TextView choosedate;
    TextView inichoosedate;
    TextView timeslot, timeslot2;
    MapAdressModel mapAdressModel;
    private String date;
    public Date edate , sdate ;
    public Date sTime , eTime ;
    private EditText remindertext;
    private Alarmmodel Editalarmmodel;
    public SimpleDateFormat ti = new SimpleDateFormat("HH:mm");
    public  SimpleDateFormat time = new SimpleDateFormat("dd-MM-yy HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm_activity);
        addadress = findViewById(R.id.chooseaddress);
        choosedate = findViewById(R.id.date_pick);
        inichoosedate = findViewById(R.id.date_pick2);
        addAlarm = findViewById(R.id.save);
        remindertext = findViewById(R.id.reminder_text);
        timeslot = findViewById(R.id.time_pick);
        timeslot2 = findViewById(R.id.time_pick1);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.ALARM_MODEl)) {
            Editalarmmodel=getIntent().getParcelableExtra(Constants.ALARM_MODEl);
            if(Editalarmmodel!=null){
                addadress.setText(Editalarmmodel.getAddress());
                try {
                    timeslot.setText(Editalarmmodel.getStime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    timeslot2.setText(Editalarmmodel.getEtime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                remindertext.setText(Editalarmmodel.getText());
            }
        }

        choosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddAlarmActivity.this, DateChooseActivity.class), 300);
            }
        });
        inichoosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddAlarmActivity.this, DateChooseActivity.class), 200);

            }
        });
        addadress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddAlarmActivity.this, MapsActivity.class), 100);

            }
        });

        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    sTime = ti.parse(timeslot.getText().toString());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    eTime = ti.parse(timeslot2.getText().toString());

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (mapAdressModel != null && date != null && date.length() > 0 && eTime != null && sTime != null && eTime.after(sTime)) {


                        Alarmmodel alarmmodel = new Alarmmodel("");
                        alarmmodel.setAddress(mapAdressModel.getAddress());
                        alarmmodel.setDisable(false);
                        alarmmodel.setLat(mapAdressModel.getLatitude());
                        alarmmodel.setLng(mapAdressModel.getLongitude());
                        alarmmodel.setText(remindertext.getText().toString());
                        alarmmodel.setedate(edate);

                        alarmmodel.setsdate(sdate);

                        alarmmodel.setetime(timeslot2.getText().toString());

                        alarmmodel.setstime(timeslot.getText().toString());

                        Uttils.saveAlarmModel(AddAlarmActivity.this, alarmmodel);

                        startService(new Intent(AddAlarmActivity.this, MyService.class));
                        finish();
                    } else {
                        if (mapAdressModel == null) {
                            Toast.makeText(AddAlarmActivity.this, "Please choose an address", Toast.LENGTH_LONG).show();
                        } else if (edate == null && sdate == null) {
                            Toast.makeText(AddAlarmActivity.this, "Please add expiry date", Toast.LENGTH_LONG).show();
                        } else if (sTime == null | eTime == null) {
                            Toast.makeText(AddAlarmActivity.this, "Please add time", Toast.LENGTH_SHORT).show();
                        } else if (eTime.before(sTime)) {
                            timeslot2.setText("");
                            Toast.makeText(AddAlarmActivity.this, "ENTER END TIME AFTER START TIME", Toast.LENGTH_SHORT).show();

                        }
                    }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            mapAdressModel = data.getParcelableExtra("address_model");
            addadress.setText(mapAdressModel.getAddress());
        } else if (requestCode == 300) {
            date = data.getStringExtra(Constants.DATE_CHOOSE);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
             sdate = null;
            try {
                sdate = sdf.parse(inichoosedate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

             edate = null;
            try {
                edate = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int i = inichoosedate.getText().equals(date)?1:0;
            if(edate.after(sdate) | i==1)
                choosedate.setText(date);

                else
                Toast.makeText(this, "ENTER END DATE AFTER START DATE", Toast.LENGTH_SHORT).show();

        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            date = data.getStringExtra(Constants.DATE_CHOOSE);

                inichoosedate.setText(date);

        }
    }
}
