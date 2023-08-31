package cn.com.shadowless.blelib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;

import java.util.List;
import java.util.UUID;


/**
 * Ble客户端
 *
 * @author sHadowLess
 */
public class BleClient extends BaseBle {

    /**
     * The Tag.
     */
    private final String tag = BleClient.class.getSimpleName();

    /**
     * 上下文
     */
    private final Context context;

    /**
     * 服务端id
     */
    private final UUID serverId;

    /**
     * 写入通道id
     */
    private final UUID writeId;

    /**
     * 读取通道id
     */
    private final UUID readId;

    /**
     * 服务端名称
     */
    private final String serverName;

    /**
     * 最大每包大小
     */
    private int mtuSize;

    /**
     * 蓝牙适配器
     */
    private BluetoothAdapter bluetoothAdapter;

    /**
     * 客户端协议
     */
    private BluetoothGatt bluetoothGatt;

    /**
     * 蓝牙扫描
     */
    private BluetoothLeScanner scanner;

    /**
     * 扫描回调
     */
    private ScanCallback scanCallback;

    /**
     * 协议回调
     */
    private BluetoothGattCallback bluetoothGattCallback;

    /**
     * 状态回调
     */
    private final StatueCallBack callBack;

    /**
     * 构造
     *
     * @param context               the context
     * @param serverId              the server id
     * @param writeId               the write id
     * @param readId                the read id
     * @param serverName            the server name
     * @param mtuSize               the mtu size
     * @param scanCallback          the scan callback
     * @param bluetoothGattCallback the bluetooth gatt callback
     * @param callBack              the call back
     * @param lifecycleOwner        the lifecycle owner
     */
    public BleClient(Context context, UUID serverId, UUID writeId, UUID readId, String serverName, int mtuSize, ScanCallback scanCallback, BluetoothGattCallback bluetoothGattCallback, StatueCallBack callBack, LifecycleOwner lifecycleOwner) {
        super(context);
        this.context = context;
        this.serverId = serverId;
        this.writeId = writeId;
        this.readId = readId;
        this.serverName = serverName;
        this.mtuSize = mtuSize;
        this.scanCallback = scanCallback;
        this.bluetoothGattCallback = bluetoothGattCallback;
        this.callBack = callBack;
        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().addObserver(this);
        }
    }

    @Override
    protected void onDestroy() {
        stopConnect();
        Log.e(tag, "onStateChanged: BLE客户端已关闭");
    }

    /**
     * 构造者
     *
     * @return the net utils . net utils builder
     */
    public static ClientBuilder builder() {
        return new ClientBuilder();
    }

    /**
     * 构造者实体
     */
    public static class ClientBuilder {
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
         * The Mtu size.
         */
        private int mtuSize;

        /**
         * The Scan callback.
         */
        private ScanCallback scanCallback;

        /**
         * The Bluetooth gatt callback.
         */
        private BluetoothGattCallback bluetoothGattCallback;

        /**
         * The Call back.
         */
        private StatueCallBack callBack;

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
        public ClientBuilder context(Context context) {
            this.context = context;
            return this;
        }

        /**
         * Server id client builder.
         *
         * @param serverId the server id
         * @return the client builder
         */
        public ClientBuilder serverId(UUID serverId) {
            this.serverId = serverId;
            return this;
        }

        /**
         * Write id client builder.
         *
         * @param writeId the write id
         * @return the client builder
         */
        public ClientBuilder writeId(UUID writeId) {
            this.writeId = writeId;
            return this;
        }

        /**
         * Read id client builder.
         *
         * @param readId the read id
         * @return the client builder
         */
        public ClientBuilder readId(UUID readId) {
            this.readId = readId;
            return this;
        }

        /**
         * Server name client builder.
         *
         * @param serverName the server name
         * @return the client builder
         */
        public ClientBuilder serverName(String serverName) {
            this.serverName = serverName;
            return this;
        }

        /**
         * Server name client builder.
         *
         * @param mtuSize the mtu size
         * @return the client builder
         */
        public ClientBuilder mtuSize(int mtuSize) {
            this.mtuSize = mtuSize;
            return this;
        }

        /**
         * Retry count location utils builder.
         *
         * @param scanCallback the scan callback
         * @return the location utils builder
         */
        public ClientBuilder scanCallback(ScanCallback scanCallback) {
            this.scanCallback = scanCallback;
            return this;
        }

        /**
         * Time out unit net utils . net utils builder.
         *
         * @param bluetoothGattCallback the bluetooth gatt callback
         * @return the net utils . net utils builder
         */
        public ClientBuilder bluetoothGattCallback(BluetoothGattCallback bluetoothGattCallback) {
            this.bluetoothGattCallback = bluetoothGattCallback;
            return this;
        }

        /**
         * Statue call back client builder.
         *
         * @param callBack the call back
         * @return the client builder
         */
        public ClientBuilder statueCallBack(StatueCallBack callBack) {
            this.callBack = callBack;
            return this;
        }

        /**
         * Lifecycle server builder.
         *
         * @param lifecycle the lifecycle
         * @return the server builder
         */
        public ClientBuilder lifecycle(LifecycleOwner lifecycle) {
            this.lifecycle = lifecycle;
            return this;
        }

        /**
         * Build net utils.
         *
         * @return the net utils
         */
        public BleClient build() {
            return new BleClient(this.context, this.serverId, this.writeId, this.readId, this.serverName, this.mtuSize, this.scanCallback, this.bluetoothGattCallback, this.callBack, this.lifecycle);
        }
    }

    /**
     * The interface Statue call back.
     */
    public interface StatueCallBack {
        /**
         * Start success.
         *
         * @param device the device
         */
        void getScannerDevice(BluetoothDevice device);

        /**
         * Connecting.
         *
         * @param gatt     the gatt
         * @param status   the status
         * @param newState the new state
         */
        void connecting(BluetoothGatt gatt, int status, int newState);

        /**
         * Disconnecting.
         *
         * @param gatt     the gatt
         * @param status   the status
         * @param newState the new state
         */
        void disconnecting(BluetoothGatt gatt, int status, int newState);

        /**
         * Connect success.
         *
         * @param gatt     the gatt
         * @param status   the status
         * @param newState the new state
         */
        void connectSuccess(BluetoothGatt gatt, int status, int newState);

        /**
         * Connect fail.
         *
         * @param gatt     the gatt
         * @param status   the status
         * @param newState the new state
         */
        void connectFail(BluetoothGatt gatt, int status, int newState);


        /**
         * Gets server.
         *
         * @param gatt   the gatt
         * @param status the status
         */
        void getServer(BluetoothGatt gatt, int status);

        /**
         * Gets client write data.
         *
         * @param gatt           the gatt
         * @param characteristic the characteristic
         */
        void getServerNotifyData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);

        /**
         * Gets server read data.
         *
         * @param gatt           the gatt
         * @param characteristic the characteristic
         * @param statue         the statue
         */
        void getServerReadData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int statue);

        /**
         * Gets server write data.
         *
         * @param gatt           the gatt
         * @param characteristic the characteristic
         * @param statue         the statue
         */
        void getServerWriteData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int statue);

        /**
         * Mtu statue.
         *
         * @param device the device
         * @param mtu    the mtu
         * @param status the status
         */
        void mtuStatue(BluetoothGatt device, int mtu, int status);

        /**
         * Sets mtu is success.
         *
         * @param isSuccess the is success
         */
        void setMtuIsSuccess(Boolean isSuccess);
    }

    /**
     * Start scan.
     */
    public void startScan() {
        if (scanCallback == null && bluetoothGattCallback == null) {
            if (callBack == null) {
                throw new IllegalArgumentException("请传入StatueCallBack或自实现ScanCallback或LeScanCallback和BluetoothGattCallback");
            }
        }
        if (!getBluetoothIsOpen() || !getLocationIsOpen()) {
            Toast.makeText(context, "请打开蓝牙和定位", Toast.LENGTH_LONG).show();
            return;
        }
        if (bluetoothAdapter == null) {
            bluetoothAdapter = getBluetoothManager().getAdapter();
        }
        scanner = bluetoothAdapter.getBluetoothLeScanner();
        if (scanner != null) {
            if (scanCallback == null) {
                initScanCallback();
            }
            scanner.startScan(scanCallback);
        }
    }

    /**
     * Stop connect.
     */
    public void stopConnect() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
        }
        stopScan();
        bluetoothAdapter = null;
        bluetoothGatt = null;
        scanCallback = null;
        bluetoothGattCallback = null;
    }

    /**
     * Stop scan.
     */
    public void stopScan() {
        if (bluetoothAdapter != null) {
            scanner = bluetoothAdapter.getBluetoothLeScanner();
            if (scanner != null && scanCallback != null) {
                scanner.stopScan(scanCallback);
            }
        }
    }

    /**
     * Send data.
     *
     * @param data      the data
     * @param writeType the write type
     */
    public void sendData(byte[] data, int writeType) {
        //找到服务
        BluetoothGattService service = bluetoothGatt.getService(serverId);
        //拿到写的特征值
        if (service != null) {
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(writeId);
            bluetoothGatt.setCharacteristicNotification(characteristic, true);
            characteristic.setValue(data);
            characteristic.setWriteType(writeType);
            bluetoothGatt.writeCharacteristic(characteristic);
        }
    }

    /**
     * Init scan callback.
     */
    private void initScanCallback() {
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                BluetoothDevice device = result.getDevice();
                String name = device.getName() == null ? "未知" : device.getName();
                Log.e(tag, "找到的服务: " + name);
                if (serverName.equals(name)) {
                    callBack.getScannerDevice(device);
                    if (bluetoothGattCallback == null) {
                        initBluetoothGattCallback();
                    }
                    bluetoothGatt = device.connectGatt(context, false, bluetoothGattCallback);
                    scanner.stopScan(scanCallback);
                }
            }
        };
    }

    /**
     * Init bluetooth gatt callback.
     */
    private void initBluetoothGattCallback() {
        bluetoothGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    callBack.connectSuccess(gatt, status, newState);
                    if (mtuSize == 0) {
                        mtuSize = 512;
                    }
                    boolean isSuccess = bluetoothGatt.requestMtu(mtuSize + 3);
                    callBack.setMtuIsSuccess(isSuccess);
                    bluetoothGatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
                } else if (newState == BluetoothProfile.STATE_CONNECTING) {
                    callBack.connecting(gatt, status, newState);
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    scanner.startScan(scanCallback);
                    callBack.connectFail(gatt, status, newState);
                } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                    callBack.disconnecting(gatt, status, newState);
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                callBack.getServer(gatt, status);
                BluetoothGattService service = bluetoothGatt.getService(serverId);
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(readId);
                boolean b = bluetoothGatt.setCharacteristicNotification(characteristic, true);
                if (b) {
                    List<BluetoothGattDescriptor> descriptorList = characteristic.getDescriptors();
                    if (descriptorList != null && descriptorList.size() > 0) {
                        for (BluetoothGattDescriptor descriptor : descriptorList) {
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            bluetoothGatt.writeDescriptor(descriptor);
                        }
                    }
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                callBack.getServerNotifyData(gatt, characteristic);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                callBack.getServerWriteData(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                callBack.getServerReadData(gatt, characteristic, status);
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                super.onMtuChanged(gatt, mtu, status);
                callBack.mtuStatue(gatt, mtu, status);
                bluetoothGatt.discoverServices();
            }
        };
    }

}
