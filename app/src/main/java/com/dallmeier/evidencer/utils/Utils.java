package com.dallmeier.evidencer.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.StringRes;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.common.Statics;
import com.dallmeier.evidencer.listener.IAudioDialog;
import com.dallmeier.evidencer.listener.ICommentDialog;
import com.dallmeier.evidencer.listener.IConfirmDialog;
import com.dallmeier.evidencer.ui.main.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.io.ByteStreams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    /**
     * Determines if the context calling has the required permissions
     *
     * @param context     - the IPC context
     * @param permissions - The permissions to check
     * @return true if the IPC has the granted permission
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        boolean hasAllPermissions = true;
        for (String permission : permissions) {
            //you can return false instead of assigning, but by assigning you can log all permission values
            if (!hasPermission(context, permission)) {
                hasAllPermissions = false;
            }
        }
        return hasAllPermissions;
    }

    /**
     * Determines if the context calling has the required permission
     *
     * @param context - the IPC context
     * @return true if the IPC has the granted permission
     */
    public static boolean hasPermission(Context context, String permission) {
        int res = context.checkCallingOrSelfPermission(permission);
        return res == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * getTimeAgo
     *
     * @param timeCreate
     * @param context
     * @param shortFormat
     * @return
     */
    public static String getTimeAgo(long timeCreate, Context context, boolean shortFormat) {
        long timeAgo = (System.currentTimeMillis() - timeCreate) / 1000;
        String timeAgoStr;
        if (timeAgo >= 60) {
            timeAgo /= 60;
            timeAgoStr = (shortFormat) ? (timeAgo + " " + context.getString(R.string.minutes_ago_short))
                    : (timeAgo + " " + context.getString(R.string.minutes_ago));
            if (timeAgo >= 60) {
                timeAgo /= 60;
                timeAgoStr = (shortFormat) ? (timeAgo + " " + context.getString(R.string.hours_ago_short))
                        : (timeAgo + " " + context.getString(R.string.hours_ago));
                if (timeAgo >= 24) {
                    timeAgo /= 24;
                    timeAgoStr = (shortFormat) ? (timeAgo + " " + context.getString(R.string.days_ago_short))
                            : (timeAgo + " " + context.getString(R.string.days_ago));
                }
            }
        } else {
            timeAgoStr = (shortFormat) ? (timeAgo + " " + context.getString(R.string.seconds_ago_short))
                    : (timeAgo + " " + context.getString(R.string.seconds_ago));
        }

        return timeAgoStr;
    }

    /**
     * getFormattedDate
     *
     * @param time long
     * @return date
     */
    public static String getFormattedDate(long time) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        df.setTimeZone(tz);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Calendar today = Calendar.getInstance();
        boolean check = calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR);
        check &= calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
        if (check) {
            df.applyPattern("hh:mm a");
            return "Today, " + df.format(new Date(time));
        } else {
            // df.applyPattern("EEE, hh:mm a, yyyy-MM-dd");
            df.applyPattern("EEE, dd MMM yyyy hh:mm a");
            return df.format(new Date(time));
        }
    }

    public static String getDateNotIncludedToday(long time) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        df.setTimeZone(tz);
        df.applyPattern("EEE, dd MMM yyyy hh:mm a");
        return df.format(new Date(time));
    }

    /**
     * getDistance
     *
     * @param toLat double
     * @param toLon double
     * @return Distance (String)
     */
    public static String getDistance(double toLat, double toLon) {
        String distanceStr = "no gps";
        Location location = LocationManagerUtils.getLocation();
        if (location != null) {
            double currentLat = location.getLatitude();
            double currentLon = location.getLongitude();
            float[] res = new float[1];
            Location.distanceBetween(currentLat, currentLon, toLat, toLon, res);
            int distance = Math.round(res[0]);
            if (distance >= 1000) {
                distanceStr = String.format("%.2f", res[0] / 1000) + "km";
            } else {
                distanceStr = distance + "m";
            }
        }
        return distanceStr;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getIcon(Context context, int iconResource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(iconResource);
        }
        return context.getResources().getDrawable(iconResource);
    }

    /**
     * hide keyboard
     *
     * @param activity Activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    /**
     * get thumb SeekBar
     *
     * @param progress  int
     * @param thumbView view
     * @param context   context
     * @return drawable
     */
    public static Drawable getThumb(int progress, View thumbView, Context context) {
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * Confirm dialog base
     *
     * @param context        Context
     * @param title          StringRes
     * @param content        StringRes
     * @param iConfirmDialog IConfirmDialog
     */
    public static void showConfirmDialogYesNo(Context context, @StringRes int title, @StringRes int content,
                                              IConfirmDialog iConfirmDialog, int task) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_confirm);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvTitle = dialog.findViewById(R.id.tv_dialog_title);
        TextView tvContent = dialog.findViewById(R.id.tv_dialog_content);
        TextView tvYes = dialog.findViewById(R.id.yes);
        TextView tvNo = dialog.findViewById(R.id.no);
        tvTitle.setText(title);
        tvContent.setText(content);
        tvYes.setOnClickListener(v -> iConfirmDialog.clickYes(dialog, task));
        tvNo.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    /**
     * @param context
     * @param iCommentDialog
     * @param
     */
    public static void showCommentDialog(Context context, ICommentDialog iCommentDialog) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_comment);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextInputEditText tvContent = dialog.findViewById(R.id.comment_edt);
        TextView tvYes = dialog.findViewById(R.id.yes);
        TextView tvNo = dialog.findViewById(R.id.no);
        tvYes.setOnClickListener(v -> iCommentDialog.clickYes(dialog, tvContent.getText().toString()));
        tvNo.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    /**
     * @param context
     * @param iAudioDialog
     * @param
     */
    public static void showRecordAudioDialog(Context context, IAudioDialog iAudioDialog) {
        CountDownTimer countDownTimer;
        final int[] record = {-1};
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_record_audio);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imgRecord = dialog.findViewById(R.id.img_record);
        TextView tvTime = dialog.findViewById(R.id.tv_timer);
        TextView tvYes = dialog.findViewById(R.id.yes);
        TextView tvNo = dialog.findViewById(R.id.no);
        //imgRecord.setOnClickListener(v -> iAudioDialog.clickRecordAudio(dialog));
        countDownTimer = onTickTime(tvTime);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(record[0] == Statics.RECORDING) && (record[0] > -1)) {
                    iAudioDialog.clickSaveAudio(dialog);
                }
            }
        });
        imgRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (record[0] == -1) {
                    if (countDownTimer != null) {
                        countDownTimer.start();
                    }
                    record[0] = Statics.RECORDING;//recording
                    imgRecord.setImageDrawable(Utils.getIcon(context, R.drawable.ic_recording_red));
                } else if (record[0] == Statics.RECORDING) {
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    // tvTime.setText(formatTime(getDuration(new File(sanitizePath(ContextHolder.getContext().getString(R.string.path_sd))))));
                    record[0] = Statics.DEFAULT_AUDIO;
                    imgRecord.setImageDrawable(Utils.getDrawableAttr(context, new int[]{R.attr.ic_micro}));
                }
                iAudioDialog.clickRecordAudio(dialog, record[0]);
            }
        });
        tvNo.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public static void showThemesDialog(Activity context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_themes);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CheckBox cbLight = dialog.findViewById(R.id.light_checkbox);
        CheckBox cbDark = dialog.findViewById(R.id.dark_checkbox);
        TextView tvYes = dialog.findViewById(R.id.yes);
        TextView tvNo = dialog.findViewById(R.id.no);
        if (SharedPrefUtil.getInstance().getTheme().equals(Statics.DARK_THEME)) {
            cbDark.setChecked(true);
            cbLight.setChecked(false);
        } else {
            cbLight.setChecked(true);
            cbDark.setChecked(false);
        }
        cbLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPrefUtil.getInstance().setTheme(Statics.LIGHT_THEME);
                    cbDark.setChecked(false);
                    cbLight.setChecked(true);
                }
            }
        });
        cbDark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPrefUtil.getInstance().setTheme(Statics.DARK_THEME);
                    cbDark.setChecked(true);
                    cbLight.setChecked(false);
                }
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbLight.isChecked()) {
                    SharedPrefUtil.getInstance().setTheme(Statics.LIGHT_THEME);
                } else if (cbDark.isChecked()) {
                    SharedPrefUtil.getInstance().setTheme(Statics.DARK_THEME);
                }
                dialog.dismiss();
                Intent mainIntent = new Intent(context, MainActivity.class);
                context.startActivity(mainIntent);
                context.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        tvNo.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public static CountDownTimer onTickTime(TextView tvTime) {
        final int[] cnt = {0};
        CountDownTimer countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String time = new Integer(cnt[0]).toString();
                long millis = cnt[0];
                int seconds = (int) (millis / 60);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                cnt[0]++;
                tvTime.setText(String.format("%02d:%02d", seconds, millis));
            }

            @Override
            public void onFinish() {
            }
        };
        return countDownTimer;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(Environment.getExternalStorageDirectory() + "/Evidencer/" + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            ByteStreams.copy(inputStream, outputStream);
            //IOUtils.copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String sanitizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.contains(".")) {
            path += ".mp3";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return BaseApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_AUDIOBOOKS) + "/" + BaseApplication.getInstance().getApplicationContext().getString(R.string.path_sd) + path;
        } else {
            return Environment.getExternalStorageDirectory() + "/" + BaseApplication.getInstance().getApplicationContext().getString(R.string.path_sd) + path;
        }
    }

    public static void deleteFile(File fDelete) {
        if (fDelete.exists()) {
            if (fDelete.delete()) {
                System.out.println("file Deleted");
            } else {
                System.out.println("file not Deleted");
            }
        }
    }

    public static String formatTime(long time) {
        StringBuffer buf = new StringBuffer();

        int hours = (int) (time / (1000 * 60 * 60));
        int minutes = (int) ((time % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = 0;
        if (time < 1000 && time > 0) {
            seconds = 1;
        } else {
            seconds = (int) (((time % (1000 * 60 * 60)) % (1000 * 60)) / 1000);
        }
        buf
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();


    }

    /**
     * get Duration of custom voice
     *
     * @param file File
     * @return long time
     */
    public static long getDuration(File file) {
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(file.getParent());
            String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            Log.d("getDuration", +Long.parseLong(durationStr) + "");
            return Long.parseLong(durationStr);
        } catch (Exception e) {

        }
        return 0;
    }

    public static int getTheme(String theme) {
        if (theme.equals(Statics.DARK_THEME)) return R.style.DarkTheme;
        if (theme.equals(Statics.LIGHT_THEME)) return R.style.LightTheme;
        return android.R.style.Theme; // stub
    }

    public static Drawable getDrawableAttr(Context context, int[] attr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(getTheme(SharedPrefUtil.getInstance().getTheme()), attr);
        int attributeResourceId = a.getResourceId(0, 0);
        Drawable drawable = context.getResources().getDrawable(attributeResourceId);
        a.recycle();
        return drawable;
    }

    public static int getColorAttr(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attr, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    public static File createImageFileVideo(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MP4_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir = new File(Utils.sanitizePathImage(BaseApplication.getInstance().getApplicationContext().getString(R.string.path_sd)));
        File video = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return video;
    }

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir = new File(Utils.sanitizePathImage(BaseApplication.getInstance().getApplicationContext().getString(R.string.path_sd)));
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    public static JSONObject convertStringToJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            Log.d("My App", obj.toString());
            return obj;
        } catch (Throwable tx) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
        }
        return null;
    }

    public static boolean isConnected(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sPref = sharedPrefs.getString("listPref", "Wi-Fi");
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        if ("Wi-Fi".equals(sPref) && networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Log.i("NET_WORK", "WIFI_CONNECTED");
            return true;
        } else if ("Wi-Fi".equals(sPref) && networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            Log.i("NET_WORK", "MOBILE_DATA_CONNECTED");
            return true;
        } else return "Any".equals(sPref) && networkInfo != null;
    }
}
