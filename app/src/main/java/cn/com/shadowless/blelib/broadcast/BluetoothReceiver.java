package cn.com.shadowless.blelib.broadcast;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.com.shadowless.blelib.callback.BlueToothStatueCallBack;


/**
 * The type Bluetooth monitor receiver.
 *
 * @author sHadowLess
 */
public class BluetoothReceiver extends BroadcastReceiver {

    /**
     * The Call back.
     */
    private final BlueToothStatueCallBack callBack;

    /**
     * Instantiates a new Bluetooth monitor receiver.
     *
     * @param callBack the call back
     */
    public BluetoothReceiver(BlueToothStatueCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_ON:
                        callBack.blueToothOn();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        callBack.blueToothOff();
                        break;
                    default:
                        break;
                }
            }

        }
    }
}
