package com.dallmeier.evidencer.utils.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.dallmeier.evidencer.utils.Utils.isConnected;


public class ConnectivityReceiver extends BroadcastReceiver {

    private final INetwork mConnectivityReceiverListener;

    public ConnectivityReceiver(INetwork listener) {
        mConnectivityReceiverListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mConnectivityReceiverListener.onNetworkConnectionChanged(isConnected(context));
    }
}
