package sg.edu.rp.c346.p06taskmanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    EditText etName, etDesc, etReminder;
    Button btnTask, btnCancel;

    Spinner spnTime;


    int reqCode = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etName = (EditText)findViewById(R.id.editText);
        etDesc = (EditText)findViewById(R.id.editText2);


        btnTask = (Button)findViewById(R.id.button);
        btnCancel = (Button)findViewById(R.id.button2);



        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String desc = etDesc.getText().toString();
                String remind = etReminder.getText().toString();

                int reminder = Integer.parseInt(remind);

                if (name.isEmpty() || desc.isEmpty() || remind.isEmpty()) {
                    Toast.makeText(AddActivity.this, "Enter Something",
                            Toast.LENGTH_SHORT).show();
                } else {

                    DBHelper dbh = new DBHelper(AddActivity.this);
                    long row_affected = dbh.insertTask(name, desc);
                    dbh.close();

                    if (row_affected != -1){
                        Toast.makeText(AddActivity.this, "Insert successful",
                                Toast.LENGTH_SHORT).show();
                    }

                    Intent i = new Intent(AddActivity.this,MyReceiver.class);

                    i.putExtra("name", name);
                    i.putExtra("desc", desc);

                    if (spnTime.getSelectedItemPosition() == 0){
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.SECOND, reminder);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                AddActivity.this, reqCode,
                                i, PendingIntent.FLAG_CANCEL_CURRENT);

                        AlarmManager am = (AlarmManager)
                                getSystemService(Activity.ALARM_SERVICE);
                        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                                pendingIntent);

                    } else if (spnTime.getSelectedItemPosition() == 1) {
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.MINUTE, reminder);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                AddActivity.this, reqCode,
                                i, PendingIntent.FLAG_CANCEL_CURRENT);

                        AlarmManager am = (AlarmManager)
                                getSystemService(Activity.ALARM_SERVICE);
                        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                                pendingIntent);
                    } else {
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.HOUR, reminder);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                AddActivity.this, reqCode,
                                i, PendingIntent.FLAG_CANCEL_CURRENT);

                        AlarmManager am = (AlarmManager)
                                getSystemService(Activity.ALARM_SERVICE);
                        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                                pendingIntent);
                    }

//                    Calendar cal = Calendar.getInstance();
//                    cal.add(Calendar.SECOND, reminder);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
