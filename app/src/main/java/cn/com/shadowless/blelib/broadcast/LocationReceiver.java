package cn.com.shadowless.blelib.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import cn.com.shadowless.blelib.callback.LocationStatueCallBack;

/**
 * The type Location receiver.
 *
 * @author sHadowLess
 */
public class LocationReceiver extends BroadcastReceiver {

    /**
     * The Call back.
     */
    private final LocationStatueCallBack callBack;

    /**
     * The Manager.
     */
    private final LocationManager manager;

    /**
     * Instantiates a new Location receiver.
     *
     * @param callBack the call back
     */
    public LocationReceiver(LocationManager manager, LocationStatueCallBack callBack) {
        this.callBack = callBack;
        this.manager = manager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (LocationManager.MODE_CHANGED_ACTION.equals(action)) {
                boolean enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (enabled) {
                    callBack.locationOn();
                    return;
                }
                callBack.locationOff();
            }
        }
    }
}
