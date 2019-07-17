package animalcaresystem.com;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.EditText;

import animalcaresystem.com.Dashboard.DashboardActivity;
import animalcaresystem.com.Dashboard.ForumActivity;

public class HelperClass {
    public static String SEND_DATA = "v_send_data";

    public static void logme(String... args) {
        for (String arg : args) {
            Log.i("HelperClass", arg);
        }
    }


    public static void givenotification(Context context,String message) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, DashboardActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification notification = new Notification();
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(context);

        notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(context.getString(R.string.app_name)).setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setContentTitle(context.getString(R.string.app_name))
                .setContentText(message).build();
        notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification);
        notificationManager.notify(1, notification);
    }

    public static boolean v_input_lyt(TextInputLayout inputField){
        String emailInput = inputField.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            inputField.setError("Field can't be empty");
            return false;
        } else {
            inputField.setError(null);
            return true;
        }
    }
    public static boolean v_edt_txt(EditText inputField){
        String emailInput = inputField.getText().toString().trim();

        if (emailInput.isEmpty()) {
            inputField.setError("Field can't be empty");
            return false;
        } else {
            inputField.setError(null);
            return true;
        }
    }
}
