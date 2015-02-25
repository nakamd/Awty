package nakamd.washington.edu.awty;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements TextWatcher {
    private static final int INTENT_ID = 1;
    private EditText message;
    private static EditText phoneNumber;
    private EditText minutes;
    private static Button button;
    private PendingIntent pi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = (EditText) findViewById(R.id.message);
        phoneNumber = (EditText) findViewById(R.id.phone);
        minutes = (EditText) findViewById(R.id.minutes);
        button = (Button) findViewById(R.id.button);

        message.addTextChangedListener(this);
        phoneNumber.addTextChangedListener(this);
        minutes.addTextChangedListener(this);
        button.setText("Start");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve a PendingIntent that will perform a broadcast
                Intent alarmIntent = new Intent(MainActivity.this, Alarm.class);
                if (phoneNumber.getText().length() == 10) {
                    String text = phoneNumber.getText().toString();
                    phoneNumber.setText("(" + text.substring(0, 3) + ") " + text.substring(3, 6) + "-" + text.substring(6));
                }
                alarmIntent.putExtra("message", phoneNumber.getText() + ": " + message.getText());
                pi = PendingIntent.getBroadcast(MainActivity.this, INTENT_ID,
                        alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                String buttonText = button.getText().toString();
                if(buttonText == "Start") {
                    button.setText("Stop");
                    start();
                } else {
                    button.setText("Start");
                    stop();
                }
            }
        });


    }

    private void start() {
        int interval = Integer.parseInt(minutes.getText().toString()) * 1000 * 60;

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pi);
        Toast.makeText(this, "Texting Set", Toast.LENGTH_SHORT).show();
    }

    private void stop() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pi);
        pi.cancel();
        Toast.makeText(this, "Texting Canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        validateFields();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        validateFields();
    }

    @Override
    public void afterTextChanged(Editable s) {
        validateFields();
    }

    private boolean validateFields() {

        if(message.getText().length() > 0 && phoneNumber.getText().length() > 0
                && minutes.getText().length() > 0 && Integer.parseInt(minutes.getText().toString()) > 0) {
            button.setClickable(true);
            button.setEnabled(true);
            return true;
        } else {
            button.setClickable(false);
            button.setEnabled(false);
            return false;
        }
    }

    private static boolean validatePhoneNumber(String phoneNo) {
        if (phoneNo.matches("\\d{10}")) return true;
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
        else {
            button.setEnabled(false);
            return false;
        }
    }
}
