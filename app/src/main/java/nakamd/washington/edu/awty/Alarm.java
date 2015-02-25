package nakamd.washington.edu.awty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by danielnakamura on 2/24/15.
 */
public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Alarm", "got here");
        String message = intent.getStringExtra("message");
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
