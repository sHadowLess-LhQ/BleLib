package cn.com.shadowless.blelib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.List;

import cn.com.shadowless.blelib.broadcast.BluetoothReceiver;
import cn.com.shadowless.blelib.broadcast.LocationReceiver;
import cn.com.shadowless.blelib.callback.BlueToothStatueCallBack;
import cn.com.shadowless.blelib.callback.LocationStatueCallBack;

/**
 * The type Base ble.
 *
 * @author sHadowLess
 */
public abstract class BaseBle implements LifecycleEventObserver, BlueToothStatueCallBack, LocationStatueCallBack {

    /**
     * The Context.
     */
    private final Context context;

    /**
     * The Bluetooth manager.
     */
    private final BluetoothManager bluetoothManager;

    /**
     * The Manager.
     */
    private final LocationManager locationManager;

    /**
     * The Receiver.
     */
    private final BluetoothReceiver blueReceiver;

    /**
     * The Location receiver.
     */
    private final LocationReceiver locationReceiver;

    /**
     * Instantiates a new Base ble.
     *
     * @param context the context
     */
    public BaseBle(Context context) {
        boolean hasBleFeature = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        if (!hasBleFeature) {
            throw new RuntimeException("该设备不支持低功耗蓝牙功能");
        }
        this.context = context;
        this.bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        IntentFilter blueFilter = new IntentFilter();
        IntentFilter locationFilter = new IntentFilter();
        locationFilter.addAction(LocationManager.MODE_CHANGED_ACTION);
        blueFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        blueReceiver = new BluetoothReceiver(this);
        locationReceiver = new LocationReceiver(locationManager, this);
        context.registerReceiver(blueReceiver, blueFilter);
        context.registerReceiver(locationReceiver, locationFilter);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.getLifecycle().removeObserver(this);
            context.unregisterReceiver(blueReceiver);
            context.unregisterReceiver(locationReceiver);
            onDestroy();
        }
    }

    @Override
    public void blueToothOn() {

    }

    @Override
    public void blueToothOff() {
        Toast.makeText(context, "请打开定位", Toast.LENGTH_LONG).show();
    }

    @Override
    public void locationOn() {

    }

    @Override
    public void locationOff() {
        Toast.makeText(context, "请打开蓝牙", Toast.LENGTH_LONG).show();
    }

    /**
     * Gets bluetooth manager.
     *
     * @return the bluetooth manager
     */
    public BluetoothManager getBluetoothManager() {
        return bluetoothManager;
    }

    /**
     * Gets location manager.
     *
     * @return the location manager
     */
    public LocationManager getLocationManager() {
        return locationManager;
    }

    /**
     * Gets bluetooth is open.
     *
     * @return the bluetooth is open
     */
    public boolean getBluetoothIsOpen() {
        return getBluetoothManager().getAdapter().isEnabled();
    }

    /**
     * Gets location is open.
     *
     * @return the location is open
     */
    public boolean getLocationIsOpen() {
        return getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Get connected device list.
     *
     * @return the list
     */
    public List<BluetoothDevice> getConnectedDevice() {
        return getBluetoothManager().getConnectedDevices(BluetoothProfile.GATT);
    }

    /**
     * On destroy.
     */
    protected abstract void onDestroy();
}
