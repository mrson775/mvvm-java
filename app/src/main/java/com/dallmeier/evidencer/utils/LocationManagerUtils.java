package com.dallmeier.evidencer.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.listener.EventBus.UpdateLocation;
import com.dallmeier.evidencer.utils.customview.NoticeDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.EventBus;

public class LocationManagerUtils {

    public static LocationManagerUtils sLocationService = null;
    public FusedLocationProviderClient mFusedLocation;
    private LocationDialog mDialog;
    private Activity mContext;
    private static Location sLocation;

    public static synchronized LocationManagerUtils getInstance(Activity activity) {
        if (sLocationService == null) {
            sLocationService = new LocationManagerUtils(activity);
        }
        return sLocationService;
    }

    private LocationManagerUtils(Activity context) {
        this.mContext = context;
        mFusedLocation = LocationServices.getFusedLocationProviderClient(context);
        if (!checkLocationService(mContext)) {
            showLocationDialog();
        } else {
            getLastLocation();
        }
    }


    @SuppressLint("MissingPermission")
    public void getLastLocation() {
        mFusedLocation.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                sLocation = task.getResult();
                if (sLocation == null) {
                    requestNewLocationData();
                } else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateLocationUser();
                        }
                    }, 1500);

                }
            }
        });
    }

    public void showLocationDialog() {
        mDialog = new LocationDialog(mContext);
        mDialog.setCancelable(false);
        if (!(mContext).isFinishing()) {
            mDialog.show();
        }
    }

    public void destroyDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @SuppressLint("MissingPermission")
    public void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocation = LocationServices.getFusedLocationProviderClient(BaseApplication.getInstance().getApplicationContext());
        mFusedLocation.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    protected LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            sLocation = locationResult.getLastLocation();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateLocationUser();
                }
            }, 1500);
        }
    };

    /**
     * get location after login
     */
    private void updateLocationUser() {
        if (sLocation != null) {
            EventBus.getDefault().postSticky(new UpdateLocation(sLocation));
        }
    }

    /**
     * @return true if location services is enabled or otherwise
     */
    public boolean checkLocationService(Context context) {
        /*	return !sLocationManager.getBestProvider(mCriteria, true).equals(LocationManager.PASSIVE_PROVIDER);*/
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            return gps_enabled;
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            return network_enabled;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Location notice dialog
     */
    private static class LocationDialog extends NoticeDialog {

        protected LocationDialog(Activity activity) {
            super(activity);
            title = activity.getString(R.string.location_service_disabled);
            message = activity.getString(R.string.location_message);
        }

        @Override
        protected void createDialogView() {
            super.createDialogView();
            setButton(BUTTON_NEGATIVE, mContext.getString(R.string.ignore), this);
            setButton(BUTTON_POSITIVE, mContext.getString(R.string.settings), this);
        }

        @Override
        protected void onPositiveBtnPress() {
            Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(viewIntent);
        }
    }

    public static Location getLocation() {
        return sLocation;
    }

}
