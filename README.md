# BleLib

软件架构

个人Android项目快速Ble使用框架

安装教程

Step 1. 添加maven仓库地址和配置

```
     //旧AndroidStudio版本
     //build.gradle
     allprojects {
         repositories {
            ...
              maven { url 'https://jitpack.io' }
         }
     }
     
     //新AndroidStudio版本
     //settings.gradle
     dependencyResolutionManagement {
          repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
          repositories {
            ...
             maven { url 'https://jitpack.io' }
          }
      }
```

Step 2. 添加依赖

a、克隆引入

直接下载源码引入model

b、远程仓库引入

[[![](https://jitpack.io/v/com.gitee.shadowless_lhq/ble-lib.svg)](https://jitpack.io/#com.gitee.shadowless_lhq/ble-lib)

```
     dependencies {
        implementation 'com.gitee.shadowless_lhq:base-view:Tag'
     }
```

#### 使用说明

### BleServer

```
     //MTU自行与客户端同步
     //发送的数据是原始数据，并没有任何数据格式
     BleServer server = BleServer.builder()
                //上下文
                .context(Context context)
                //服务端UUID
                .serverId(UUID serverId)
                //服务端写入通道UUID
                .writeId(UUID writeId)
                //服务端读取通道UUID
                .readId(UUID readId)
                //服务端名称
                .serverName(String serverName)
                //服务端回调(不传入有默认)
                .gattServerCallback(BluetoothGattServerCallback callBack)
                //广播回调(不传入有默认)
                .advertiseCallBack(AdvertiseCallback callBack)
                //需要监听生命周期的对象
                .lifecycle(LifecycleOwner lifecycle)
                //状态回调
                .statueCallBack(new BleServer.StatueCallBack() {
                    @Override
                    public void startSuccess(AdvertiseSettings settingsInEffect) {
                        Log.e("TAG", "开启服务成功");
                    }

                    @Override
                    public void startFail(int errorCode) {
                        Log.e("TAG", "开启服务失败");
                    }

                    @Override
                    public void connecting(BluetoothDevice device, int status, int newState) {
                        Log.e("TAG", "客户端：" + device.getAddress() + "/" + "正在连接" + "/" + "状态：" + status + "/" + newState);
                    }

                    @Override
                    public void disconnecting(BluetoothDevice device, int status, int newState) {
                        Log.e("TAG", "客户端：" + device.getAddress() + "/" + "正在断连" + "/" + "状态：" + status + "/" + newState);
                    }

                    @Override
                    public void connectSuccess(BluetoothDevice device, int status, int newState) {
                       Log.e("TAG", "客户端：" + device.getAddress() + "/" + "连接成功" + "/" + "状态：" + status + "/" + newState);
                    }

                    @Override
                    public void connectFail(BluetoothDevice device, int status, int newState) {
                        Log.e("TAG", "客户端：" + device.getAddress() + "/" + "连接断开" + "/" + "状态：" + status + "/" + newState);
                    }

                    @Override
                    public void getClientWriteData(BluetoothGattServer gattServer, BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                        Log.e("TAG", "接收客户端写入数据：" + device.getAddress() + "/" + "是否准备写入：" + preparedWrite + "/" + "是否需要响应：" + responseNeeded);
                    }

                    @Override
                    public void getClientReadData(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {

                    }

                    @Override
                    public void mtuStatue(BluetoothDevice device, int mtu) {

                    }
                })
                .build();
        //开启服务
        server.starServer();
        //关闭服务
        server.starServer();
        //已连接后初始化连接参数(只有自实现广播回调时使用)
        server.initParam();
        //发送数据到指定的客户端
        server.sendDataToDevice(BluetoothDevice device, byte[] data);
        //发送数据到全部已连接的客户端
        server.sendDataToAllDevice(byte[] data);
```

### BleClient

```
     //MTU自行与服务端同步
     //发送的数据是原始数据，并没有任何数据格式
     BleClient bleClient = BleClient.builder()
                //上下文
                .context(Context context)
                //服务端UUID
                .serverId(UUID serverId)
                //服务端写入通道UUID
                .writeId(UUID writeId)
                //服务端读取通道UUID
                .readId(UUID readId)
                //服务端名称
                .serverName(String serverName)
                //需要监听生命周期的对象
                .lifecycle(LifecycleOwner lifecycle)
                //传递数据包大小(传入的是实际包大小，实际内部会自动+3，要注意mtu的回调)
                .mtuSize(int mtuSize)
                //6.0以上扫描回调(不传有默认)
                .scanCallback(ScanCallback scanCallback)
                //客户端通道回调
                .bluetoothGattCallback(BluetoothGattCallback bluetoothGattCallback)
                //客户端状态回调
                .statueCallBack(new BleClient.StatueCallBack() {
                    @Override
                    public void getScannerDevice(BluetoothDevice device) {
                        Log.e("TAG", "扫描到匹配服务：" + device.getName() + "/" + device.getAddress());
                    }

                    @Override
                    public void connecting(BluetoothGatt gatt, int status, int newState) {
                        Log.e("TAG", "正在连接匹配服务：" + status + "/" + newState);
                    }

                    @Override
                    public void disconnecting(BluetoothGatt gatt, int status, int newState) {
                        Log.e("TAG", "正在断开匹配服务：" + status + "/" + newState);
                    }

                    @Override
                    public void connectSuccess(BluetoothGatt gatt, int status, int newState) {
                        Log.e("TAG", "连接匹配服务成功：" + status + "/" + newState);
                    }

                    @Override
                    public void connectFail(BluetoothGatt gatt, int status, int newState) {
                        Log.e("TAG", "连接匹配服务失败：" + status + "/" + newState);
                    }

                    @Override
                    public void getServer(BluetoothGatt gatt, int status) {
                        Log.e("TAG", "查询到服务业务：" + status);
                    }

                    @Override
                    public void getServerNotifyData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                        Log.e("TAG", "获取服务端传入数据: " + new String(characteristic.getValue()));
                    }

                    @Override
                    public void getServerReadData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int statue) {
                        Log.e("TAG", "获取服务端读数据: " + new String(characteristic.getValue()) + "/" + statue);
                    }

                    @Override
                    public void getServerWriteData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int statue) {
                        Log.e("TAG", "获取服务端写数据: " + new String(characteristic.getValue()) + "/" + statue);
                    }

                    @Override
                    public void mtuStatue(BluetoothGatt device, int mtu, int status) {
                        Log.e("TAG", "更改MTU后的成功值：" + mtu + "/" + status);
                    }

                    @Override
                    public void setMtuIsSuccess(Boolean isSuccess) {
                        Log.e("TAG", "更改MTU大小是否成功：" + isSuccess);
                    }
                })
                .build();
        //开始扫描
        bleClient.startScan();
        //停止扫描
        bleClient.stopScan();
        //断开连接
        bleClient.stopConnect();
        //发送数据
        bleClient.sendData(byte[] data, int writeType);
```
