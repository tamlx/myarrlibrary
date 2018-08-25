package b.laixuantam.myaarlibrary.helper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.widget.RemoteViews;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.model.BaseNotificationModel;
import b.laixuantam.myaarlibrary.model.TypeNotification;
import b.laixuantam.myaarlibrary.widgets.calendar.ConvertDate;

/**
 * Created by LaiXuanTam on 4/7/2016.
 */
public class MyNotification {

    public static final int GOTO_ACTIVITY_NOTIFICATION_ID = 2;
    public static final int DEFAULT_NOTIFICATION_ID = 1;

    private static MyNotification instance;

    public static MyNotification getInstance() {
        if (instance == null) {
            instance = new MyNotification();
        }
        return instance;
    }

    /**
     * addParentStack() – adds the parent activity to the task stack builder (this activity is specified in the <meta-data> element in the manifest)
     * addNextIntent() – adds the new intent to the task stack
     * PendingIntent – we use a pending intent to start the new activity when the user touches the notification. It contains information about the intent and the task back stack
     * FLAG_UPDATE_CURRENT – tells the system that if this pending intent exists, then it must keep it and replace any data with the intent’s new data, if any
     * setContentIntent() – it sets the pending intent to send when the notification is clicked.
     */

    public void showDefaultNotification(Context context, String title, String message, int smallIcon) {
        NotificationManagerCompat.from(context).cancel(DEFAULT_NOTIFICATION_ID);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(smallIcon);
        builder.setContentTitle(title);
        builder.setContentText(Html.fromHtml(message));
        builder.setColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setAutoCancel(true);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS);
        builder.setLights(0xff00ff00, 300, 100);
        builder.setPriority(Notification.PRIORITY_MAX);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(DEFAULT_NOTIFICATION_ID, builder.build());

    }

    public void showNotificationGotoActivity(Context context, int smallIcon, Class destinationClass, BaseNotificationModel model) {
        if (model == null) {
            return;
        }
        NotificationManagerCompat.from(context).cancel(GOTO_ACTIVITY_NOTIFICATION_ID);
        Intent intentGoNotificationActivity = new Intent(context, destinationClass);

        intentGoNotificationActivity.putExtra("model_notification", model);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(destinationClass);
        stackBuilder.addNextIntent(intentGoNotificationActivity);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(smallIcon);
        builder.setContentTitle(model.getTitle());
        builder.setColor(context.getResources().getColor(R.color.colorPrimary));

        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(model.getMessage()));
        builder.setContentText(Html.fromHtml(model.getMessage()));

        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS);
        builder.setLights(0xff00ff00, 300, 100);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        builder.setVibrate(new long[]{1000, 1000});
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //        Random random = new Random();
        //        int m = random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(GOTO_ACTIVITY_NOTIFICATION_ID, builder.build());

    }

    public void showCustomNotification(Context context, int smallIcon, BaseNotificationModel model) {

        if (model == null) {
            return;
        }
        String headerTitle = "Thông báo";

        String headerTime = "---";

        if (model.getTime() > 0) {
            headerTime = ConvertDate.getDistanceTime(model.getTime());
        }

        int icon = smallIcon;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, model.getTitle(), when);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);

        contentView.setTextViewText(R.id.tvNotificationHeaderTitle, headerTitle);
        contentView.setTextViewText(R.id.tvNotificationTimeSend, headerTime);
        contentView.setTextViewText(R.id.tvNotificationTitle, model.getTitle());
        contentView.setTextViewText(R.id.tvNotificationDescription, model.getMessage());
        contentView.setImageViewResource(R.id.imvIcNotify, smallIcon);
        contentView.setImageViewResource(R.id.imvLogoHeader, smallIcon);

        notification.contentView = contentView;


        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.FLAG_SHOW_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        notification.defaults |= Notification.DEFAULT_SOUND; // Sound
        notification.ledARGB = 0xff00ff00;
        notification.ledOnMS = 300;
        notification.ledOffMS = 100;
        notification.priority = Notification.PRIORITY_MAX;

        mNotificationManager.notify(DEFAULT_NOTIFICATION_ID, notification);
    }


    public void showCustomNotificationGoToActivity(Context context, int smallIcon, Class destinationClass, BaseNotificationModel model, Bitmap icomAvata) {

        if (model == null) {
            return;
        }
        String headerTitle = "Thông báo";

        String headerTime = "---";

        if (model.getTime() > 0) {
            headerTime = ConvertDate.getDistanceTime(model.getTime());
        }

        int icon = smallIcon;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, model.getTitle(), when);

        // you can do something with loaded bitmap here
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        String messageContent = model.getMessage();

        if (model.getTypeMess().equalsIgnoreCase(TypeNotification.TYPE_MESS_IMG)) {
            messageContent = "Bạn nhận được [Hình ảnh] mới";
        } else if (model.getTypeMess().equalsIgnoreCase(TypeNotification.TYPE_MESS_VIDEO)) {
            messageContent = "Bạn nhận được [Video] mới";
        } else if (model.getTypeMess().equalsIgnoreCase(TypeNotification.TYPE_MESS_FILE)) {
            messageContent = "Bạn nhận được [File] mới";
        }

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);

        contentView.setTextViewText(R.id.tvNotificationHeaderTitle, headerTitle);
        contentView.setTextViewText(R.id.tvNotificationTimeSend, headerTime);
        contentView.setTextViewText(R.id.tvNotificationTitle, model.getTitle());
        contentView.setTextViewText(R.id.tvNotificationDescription, messageContent);
        contentView.setImageViewResource(R.id.imvLogoHeader, smallIcon);

        if (icomAvata != null) {
            contentView.setImageViewBitmap(R.id.imvIcNotify, getCircledBitmap(icomAvata));
        } else {
            contentView.setImageViewResource(R.id.imvIcNotify, smallIcon);
        }

        notification.contentView = contentView;


        Intent intentGoNotificationActivity = new Intent(context, destinationClass);

        intentGoNotificationActivity.putExtra("model_notification", model);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(destinationClass);
        stackBuilder.addNextIntent(intentGoNotificationActivity);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = pendingIntent;

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.FLAG_SHOW_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        notification.defaults |= Notification.DEFAULT_SOUND; // Sound
        notification.ledARGB = 0xff00ff00;
        notification.ledOnMS = 300;
        notification.ledOffMS = 100;
        notification.priority = Notification.PRIORITY_MAX;

        mNotificationManager.notify(GOTO_ACTIVITY_NOTIFICATION_ID, notification);

    }


    public void showNotificationGotoActivitySendBigPicture(Context context, String title, String message, int bigPicture, int smallIcon, Class destinationClass) {

        Intent intentGotoActivity = new Intent(context, destinationClass);

        /*set  the flag - clear top of stack activity, putting new activity if is running ,else start it */
        intentGotoActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1001, intentGotoActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIconBitmap = BitmapFactory.decodeResource(context.getResources(), bigPicture);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true).setContentTitle(title).setContentText(message).setSmallIcon(smallIcon).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setContentIntent(pendingIntent);


        // build the big picture notification
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(largeIconBitmap);
        bigPictureStyle.setBigContentTitle("BigPictureTitle");
        bigPictureStyle.setSummaryText("Big Picture Summary Text");
        Notification compatNotification = builder.setStyle(bigPictureStyle).build();

        //get notification manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //send notification
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(1, compatNotification);


    }


    public void showNotificationWithLoadImageAvata(Context context, Class destinationClass, BaseNotificationModel model, int smallIcon) {
        new GeneratePictureStyleNotification(context, destinationClass, model,smallIcon).execute();
    }

    public class GeneratePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String imageUrl;
        private BaseNotificationModel model;
        private Class destinationClass;
        private int smallIcon;

        public GeneratePictureStyleNotification(Context context, Class destinationClass, BaseNotificationModel model, int smallIcon) {
            super();
            this.mContext = context;
            this.imageUrl = model.getImage();
            this.destinationClass = destinationClass;
            this.model = model;
            this.smallIcon = smallIcon;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (destinationClass != null)
                showCustomNotificationGoToActivity(mContext, smallIcon, destinationClass, model, result);


        }
    }

    public static Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
