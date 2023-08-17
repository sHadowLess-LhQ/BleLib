package cn.com.shadowless.blelib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;

import java.util.List;
import java.util.UUID;

/**
 * The type Ble server.
 *
 * @author sHadowLess
 */
public class BleServer extends BaseBle {

    /**
     * The Tag.
     */
    private final String tag = BleServer.class.getSimpleName();

    /**
     * The Context.
     */
    private final Context context;

    /**
     * The Server id.
     */
    private final UUID serverId;

    /**
     * The Write id.
     */
    private final UUID writeId;

    /**
     * The Read id.
     */
    private final UUID readId;

    /**
     * The Server name.
     */
    private final String serverName;

    /**
     * The Bluetooth gatt server.
     */
    private BluetoothGattServer bluetoothGattServer;

    /**
     * The Settings.
     */
    private AdvertiseSettings settings;

    /**
     * The Advertise data.
     */
    private AdvertiseData advertiseData;

    /**
     * The Bluetooth le advertiser.
     */
    private BluetoothLeAdvertiser bluetoothLeAdvertiser;

    /**
     * The Read gatt.
     */
    private BluetoothGattCharacteristic readGatt;

    /**
     * The Write gatt.
     */
    private BluetoothGattCharacteristic writeGatt;

    /**
     * The Call back.
     */
    private final StatueCallBack callBack;

    /**
     * The Gatt server callback.
     */
    private BluetoothGattServerCallback gattServerCallback;

    /**
     * The Advertise callback.
     */
    private AdvertiseCallback advertiseCallback;

    /**
     * Instantiates a new Ble server.
     *
     * @param context            the context
     * @param serverId           the server id
     * @param writeId            the write id
     * @param readId             the read id
     * @param serverName         the server name
     * @param settings           the settings
     * @param advertiseData      the advertise data
     * @param readGatt           the read gatt
     * @param writeGatt          the write gatt
     * @param callBack           the call back
     * @param gattServerCallback the gatt server callback
     * @param advertiseCallback  the advertise callback
     * @param lifecycle          the lifecycle
     */
    public BleServer(Context context, UUID serverId, UUID writeId, UUID readId, String serverName, AdvertiseSettings settings, AdvertiseData advertiseData, BluetoothGattCharacteristic readGatt, BluetoothGattCharacteristic writeGatt, StatueCallBack callBack, BluetoothGattServerCallback gattServerCallback, AdvertiseCallback advertiseCallback, LifecycleOwner lifecycle) {
        super(context);
        this.context = context;
        this.serverId = serverId;
        this.writeId = writeId;
        this.readId = readId;
        this.serverName = serverName;
        this.settings = settings;
        this.readGatt = readGatt;
        this.writeGatt = writeGatt;
        this.advertiseData = advertiseData;
        this.callBack = callBack;
        this.gattServerCallback = gattServerCallback;
        this.advertiseCallback = advertiseCallback;
        if (lifecycle != null) {
            lifecycle.getLifecycle().addObserver(this);
        }
    }

    @Override
    protected void onDestroy() {
        stopServer();
        Log.e(tag, "onStateChanged: BLE服务已关闭");
    }

    /**
     * 构造者
     *
     * @return the net utils . net utils builder
     */
    public static ServerBuilder builder() {
        return new ServerBuilder();
    }

    /**
     * 构造者实体
     */
    public static class ServerBuilder {
        /**
         * The Context.
         */
        private Context context;

        /**
         * The Server id.
         */
        private UUID serverId;

        /**
         * The Write id.
         */
        private UUID writeId;

        /**
         * The Read id.
         */
        private UUID readId;

        /**
         * The Server name.
         */
        private String serverName;

        /**
         * The Settings.
         */
        private AdvertiseSettings advertiseSetting;

        /**
         * The Advertise data.
         */
        private AdvertiseData advertiseData;

        /**
         * The Characteristic read.
         */
        private BluetoothGattCharacteristic readGatt;

        /**
         * The Characteristic write.
         */
        private BluetoothGattCharacteristic writeGatt;

        /**
         * The Call back.
         */
        private StatueCallBack callBack;

        /**
         * The Gatt server callback.
         */
        private BluetoothGattServerCallback gattServerCallback;

        /**
         * The Advertise callback.
         */
        private AdvertiseCallback advertiseCallback;

        /**
         * The Observer.
         */
        private LifecycleOwner lifecycle;

        /**
         * Base url net utils . net utils builder.
         *
         * @param context the context
         * @return the net utils . net utils builder
         */
        public ServerBuilder context(Context context) {
            this.context = context;
            return this;
        }

        /**
         * Server id client builder.
         *
         * @param serverId the server id
         * @return the client builder
         */
        public ServerBuilder serverId(UUID serverId) {
            this.serverId = serverId;
            return this;
        }

        /**
         * Write id client builder.
         *
         * @param writeId the write id
         * @return the client builder
         */
        public ServerBuilder writeId(UUID writeId) {
            this.writeId = writeId;
            return this;
        }

        /**
         * Read id client builder.
         *
         * @param readId the read id
         * @return the client builder
         */
        public ServerBuilder readId(UUID readId) {
            this.readId = readId;
            return this;
        }

        /**
         * Server name client builder.
         *
         * @param serverName the server name
         * @return the client builder
         */
        public ServerBuilder serverName(String serverName) {
            this.serverName = serverName;
            return this;
        }

        /**
         * Time out net utils . net utils builder.
         *
         * @param settings the settings
         * @return the net utils . net utils builder
         */
        public ServerBuilder advertiseSetting(AdvertiseSettings settings) {
            this.advertiseSetting = settings;
            return this;
        }

        /**
         * Retry count location utils builder.
         *
         * @param data the data
         * @return the location utils builder
         */
        public ServerBuilder advertiseData(AdvertiseData data) {
            this.advertiseData = data;
            return this;
        }

        /**
         * Delay time location utils builder.
         *
         * @param read the read
         * @return the location utils builder
         */
        public ServerBuilder readGatt(BluetoothGattCharacteristic read) {
            this.readGatt = read;
            return this;
        }

        /**
         * Time out unit net utils . net utils builder.
         *
         * @param write the write
         * @return the net utils . net utils builder
         */
        public ServerBuilder writeGatt(BluetoothGattCharacteristic write) {
            this.writeGatt = write;
            return this;
        }

        /**
         * Location listener location utils . location utils builder.
         *
         * @param callBack the call back
         * @return the location utils . location utils builder
         */
        public ServerBuilder statueCallBack(StatueCallBack callBack) {
            this.callBack = callBack;
            return this;
        }

        /**
         * Gatt server callback server builder.
         *
         * @param callBack the call back
         * @return the server builder
         */
        public ServerBuilder gattServerCallback(BluetoothGattServerCallback callBack) {
            this.gattServerCallback = callBack;
            return this;
        }

        /**
         * Advertise call back server builder.
         *
         * @param callBack the call back
         * @return the server builder
         */
        public ServerBuilder advertiseCallBack(AdvertiseCallback callBack) {
            this.advertiseCallback = callBack;
            return this;
        }

        /**
         * Lifecycle server builder.
         *
         * @param lifecycle the lifecycle
         * @return the server builder
         */
        public ServerBuilder lifecycle(LifecycleOwner lifecycle) {
            this.lifecycle = lifecycle;
            return this;
        }

        /**
         * Build net utils.
         *
         * @return the net utils
         */
        public BleServer build() {
            return new BleServer(this.context, this.serverId, this.writeId, this.readId, this.serverName, this.advertiseSetting, this.advertiseData, this.readGatt, this.writeGatt, this.callBack, this.gattServerCallback, this.advertiseCallback, this.lifecycle);
        }
    }

    /**
     * The interface Statue call back.
     */
    public interface StatueCallBack {
        /**
         * Start success.
         *
         * @param settingsInEffect the settings in effect
         */
        void startSuccess(AdvertiseSettings settingsInEffect);

        /**
         * Start fail.
         *
         * @param errorCode the error code
         */
        void startFail(int errorCode);

        /**
         * Connecting.
         *
         * @param device   the device
         * @param status   the status
         * @param newState the new state
         */
        void connecting(BluetoothDevice device, int status, int newState);

        /**
         * Disconnecting.
         *
         * @param device   the device
         * @param status   the status
         * @param newState the new state
         */
        void disconnecting(BluetoothDevice device, int status, int newState);

        /**
         * Connect success.
         *
         * @param device   the device
         * @param status   the status
         * @param newState the new state
         */
        void connectSuccess(BluetoothDevice device, int status, int newState);

        /**
         * Connect fail.
         *
         * @param device   the device
         * @param status   the status
         * @param newState the new state
         */
        void connectFail(BluetoothDevice device, int status, int newState);

        /**
         * Gets client write data.
         *
         * @param device         the device
         * @param requestId      the request id
         * @param characteristic the characteristic
         * @param preparedWrite  the prepared write
         * @param responseNeeded the response needed
         * @param offset         the offset
         * @param value          the value
         */
        void getClientWriteData(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value);

        /**
         * Gets client read data.
         *
         * @param device         the device
         * @param requestId      the request id
         * @param offset         the offset
         * @param characteristic the characteristic
         */
        void getClientReadData(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic);

        /**
         * Mtu statue.
         *
         * @param device the device
         * @param mtu    the mtu
         */
        void mtuStatue(BluetoothDevice device, int mtu);
    }

    /**
     * Start advertising.
     */
    public void starServer() {
        if (advertiseCallback == null && gattServerCallback == null) {
            if (callBack == null) {
                throw new IllegalArgumentException("请传入StatueCallBack或自实现AdvertiseCallback和BluetoothGattServerCallback");
            }
        }
        if (!getBluetoothIsOpen() || !getLocationIsOpen()) {
            Toast.makeText(context, "请打开蓝牙和定位", Toast.LENGTH_LONG).show();
            return;
        }
        BluetoothAdapter bluetoothAdapter = getBluetoothManager().getAdapter();
        if (settings == null) {
            initAdvertiseSetting();
        }
        if (advertiseData == null) {
            initAdvertiseData();
        }
        bluetoothAdapter.setName(serverName);
        bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        if (advertiseCallback == null) {
            initAdvertiseCallBack();
        }
        bluetoothLeAdvertiser.startAdvertising(settings, advertiseData, advertiseCallback);
    }

    /**
     * Stop server.
     */
    public void stopServer() {
        bluetoothLeAdvertiser.stopAdvertising(advertiseCallback);
        bluetoothGattServer.clearServices();
        for (BluetoothDevice device : getConnectedDevice()) {
            bluetoothGattServer.cancelConnection(device);
        }
        bluetoothGattServer.close();
        bluetoothLeAdvertiser = null;
        advertiseCallback = null;
        bluetoothGattServer = null;
    }

    /**
     * Send data to all device.
     *
     * @param data the data
     */
    public void sendDataToAllDevice(byte[] data) {
        List<BluetoothDevice> list = getConnectedDevice();
        if (list != null && !list.isEmpty()) {
            for (BluetoothDevice device : list) {
                writeGatt.setValue(data);
                writeGatt.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                bluetoothGattServer.notifyCharacteristicChanged(device, writeGatt, false);
            }
        }
    }

    /**
     * Send data.
     *
     * @param device the device
     * @param data   the data
     */
    public void sendDataToDevice(BluetoothDevice device, byte[] data) {
        writeGatt.setValue(data);
        writeGatt.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        bluetoothGattServer.notifyCharacteristicChanged(device, writeGatt, false);
    }

    /**
     * Add service.
     */
    public void initParam() {
        BluetoothGattService gattService = new BluetoothGattService(serverId, BluetoothGattService.SERVICE_TYPE_PRIMARY);
        if (readGatt == null) {
            //只读的特征值
            readGatt = new BluetoothGattCharacteristic(readId,
                    BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                    BluetoothGattCharacteristic.PERMISSION_READ);
        }
        if (writeGatt == null) {
            //只写的特征值
            writeGatt = new BluetoothGattCharacteristic(writeId,
                    BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_READ
                            | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                    BluetoothGattCharacteristic.PERMISSION_WRITE | BluetoothGattCharacteristic.PERMISSION_READ);
        }
        if (gattServerCallback == null) {
            initGattServerCallBack();
        }
        //将特征值添加至服务里
        gattService.addCharacteristic(readGatt);
        gattService.addCharacteristic(writeGatt);
        //监听客户端的连接
        bluetoothGattServer = getBluetoothManager().openGattServer(context, gattServerCallback);
        //添加服务
        bluetoothGattServer.addService(gattService);
    }

    /**
     * Init advertise setting.
     */
    private void initAdvertiseSetting() {
        settings = new AdvertiseSettings.Builder()
                .setConnectable(true)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .build();
    }

    /**
     * Init advertise data.
     */
    private void initAdvertiseData() {
        advertiseData = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .addServiceUuid(new ParcelUuid(serverId))
                .setIncludeTxPowerLevel(true)
                .build();
    }

    /**
     * Init gatt server call back.
     */
    private void initGattServerCallBack() {
        gattServerCallback = new BluetoothGattServerCallback() {
            @Override
            public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
                super.onConnectionStateChange(device, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    callBack.connectSuccess(device, status, newState);
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    stopServer();
                    starServer();
                    callBack.connectFail(device, status, newState);
                } else if (newState == BluetoothProfile.STATE_CONNECTING) {
                    callBack.connecting(device, status, newState);
                } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                    callBack.disconnecting(device, status, newState);
                }
            }

            @Override
            public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
                callBack.getClientWriteData(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            }

            @Override
            public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
                callBack.getClientReadData(device, requestId, offset, characteristic);
            }

            @Override
            public void onMtuChanged(BluetoothDevice device, int mtu) {
                super.onMtuChanged(device, mtu);
                callBack.mtuStatue(device, mtu);
            }
        };
    }

    /**
     * Init advertise call back.
     */
    private void initAdvertiseCallBack() {
        advertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                callBack.startSuccess(settingsInEffect);
                initParam();
            }

            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
                callBack.startFail(errorCode);
            }
        };
    }
}
