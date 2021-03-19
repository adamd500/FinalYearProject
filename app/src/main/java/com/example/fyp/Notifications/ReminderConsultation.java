package com.example.fyp.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.fyp.R;

public class ReminderConsultation extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"Consultation Reminder")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Consultation Reminder")
                .setContentText("JobConnect Reminder : You have a consultation tomorrow. ")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200,builder.build());
    }
}
