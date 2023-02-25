package com.ble.demo.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.ArrayMap;
import android.webkit.JavascriptInterface;


import com.ble.mylibrary.interfaces.BatteryStatusCallback;
import com.ble.mylibrary.interfaces.PeelMode;
import com.ble.mylibrary.interfaces.WeightMode;
import com.ble.mylibrary.interfaces.WeightUpdateCallback;
import com.ble.mylibrary.outdevice.LScaleHelper;

import java.math.BigDecimal;

public class Presenter1 {
//    private ArrayMap<String,BluetoothDevice> ble_devices = new ArrayMap<>();
//    private X5WebView webView;
//    private Context context;
//    public static BluetoothDevice connectedDevice;
//    int connect_result  = -1;
//    private String address = "";
//    public Presenter1(X5WebView webView, Context context) {
//        this.webView = webView;
//        this.context = context;
//    }
//
//
//    /**
//     * 搜索
//     */
//    @JavascriptInterface
//    public  void search( ) {
//
//        LScaleHelper.getInstance(context).startscan(false, (device, rssi) -> {
//
//            ble_devices.put(device.getName(),device);
//
//            //自动连接上次的设备----本地读取上次存储的blename
//            boolean connected = LScaleHelper.getInstance(context).isConnected();
//                if (!connected   && device.getAddress().equalsIgnoreCase(address)){
//                    connect(device.getName(),false,0);
//                }else {
//                    System.out.println(address+"++++++++++++"+device.getName());
//                    System.out.println(address+"++++++++++++"+device.getAddress());
//                    webView.post(() -> webView.loadUrl("javascript:androidMethods('" + device.getName() + "','0')"));
//                }
//                if (connected){
//                    webView.post(() -> webView.loadUrl("javascript:androidMethods('" + connectedDevice.getName() + "','1')"));
//                    webView.post(() -> webView.loadUrl("javascript:isblueya('true')"));
//                }
//
//        });
//
//
//    }
//    @JavascriptInterface
//    public void stopscan(){
//        LScaleHelper.getInstance(context).stopscan();
//    }
//
//
//    /**
//     *
//     * @param  :要连接的设备
//     * @param dis_autoConnect:断开后,重连----连接失败时---是否要再次连接(区分第一次连接,连接失败需要手动连接)
//     * @param count:自动连接时,要重连几次
//     * @return
//     */
//    @JavascriptInterface
//    public int connect(String deviceName,boolean dis_autoConnect,int count) {
//        BluetoothDevice device = ble_devices.get(deviceName);
//        LScaleHelper.getInstance(context).connect(device, (result, device1) -> {
//            this.connect_result = result;
//
//            switch (result){
//                case 1: //   连接成功
//                    stopscan();
//                    connectedDevice =device;
//                    webView.post(() -> webView.loadUrl("javascript:androidMethods('" + device.getName() + "','1')"));
//                    webView.post(() -> webView.loadUrl("javascript:isblueya('true')"));
//                    break;
//
//                case 2: //连接失败
//                    if (dis_autoConnect && count > 0){//断开后的重连
//                        connect(connectedDevice.getName(),true,count-1);
//                    }else{//手动重新连接
//                        webView.post(() -> webView.loadUrl("javascript:androidMethods('" + device.getName() + "','0')"));
//                    }
//                    break;
//                case 3:////连接断开, 自动重新连接
//                    System.out.println("蓝牙关闭");
//                    webView.post(() -> webView.loadUrl("javascript:androidMethods('" + device.getName() + "','0')"));
//                    connect(connectedDevice.getName(),true,5);
//                    break;
//            }
//
//
//
//        });
//        return  connect_result;
//    }
//
//    /**
//     * 获取数据
//     * weight:重量(公斤)
//     * isStable:是否稳定
//     * isTared:是否有皮重
//     * isZero:是否是零点
//     * PeelMode:皮重模式----
//     * dataStatus:数据标志
//     * error:是否有错误
//     * rssi:信号强度(负数)-----0至-100,越大信号越强
//     */
//    @JavascriptInterface
//    public void getData() {
//        LScaleHelper.getInstance(context).setWUCallback(new WeightUpdateCallback() {
//            @Override
//            public void onWeightUpdate(BigDecimal weight, boolean isStable, boolean isTared, boolean isZero, PeelMode peelMode, int dataStatus, boolean error) {
//                if (error) {
//                    switch (dataStatus) {
//                        case 3:
//                            break; //连接断开
//                        case 4: break;//过载
//                        case 5: break;//负重
//                    }
//                } else {
//                    //PeelMode.WeightPell:称重皮
//                    // PeelMode.DigitPell:电子皮
//                    System.out.println("fqqq"+weight);
//                    BigDecimal bigDecimal = new BigDecimal("2");
//                    BigDecimal finalWeight = weight.multiply(bigDecimal);
//                    float data=0;
//                    if (isTared){
//                        if( peelMode != null){
//                            data =  peelMode.getData();
//                        }else{
//                            data =  PeelMode.WeightPell.getData();
//                        }
//                    }
//                    float finalData = data;
//                    webView.post(() -> webView.loadUrl("javascript:androidvalMethods('" + finalWeight + "','"+isStable+"','"+ finalData*2 +"')"));
//                    //webView.post(() -> webView.loadUrl("javascript:androidvalMethods('" + finalWeight + "','"+isStable+"')"));
//
//                }
//
//
//            }
//
//            @Override
//            public void onrssi(int rssi) {
//
//            }
//        });
//    }
//
//    /**
//     * 各功能(去皮,置零,电子皮,模式切换)
//     */
//    public void features(Context context) {
//        /**
//         * 置零
//         * result
//         * -1:当前版本没有返回值
//         * 0:设备未连接
//         * 1:置零成功
//         * 2:有皮重不允许置零
//         * 3:置零超出范围
//         * 0xff:超时
//         */
//        int result = LScaleHelper.getInstance(context).zero();
//
//        /**
//         * 去皮
//         * -1:当前版本没有返回值
//         * 0:设备未连接
//         * 1:去皮成功
//         * 2:零点错误报警
//         * 3:超过去皮范围
//         * 4:有数字皮,不可去称重皮
//         * 5:重量不稳定
//         * 0xff:超时
//         */
//        int result2 = LScaleHelper.getInstance(context).tare();
//
//
//        /**
//         * 设置电子皮(框皮)
//         * 0:设备未连接
//         * 1:成功
//         * 2:有称重皮存在,添加数字皮失败
//         * 3:重量不稳定
//         * 4:零点错误
//         * 5:去皮超范围
//         * 0xff:超时
//         * 0xee:旧版本没有成功状态返回
//         */
//        int result3 = LScaleHelper.getInstance(context).setPeel("2.16");
//
//        /**
//         * 清皮
//         * -2:当前版本不支持此功能
//         * 0:设备未连接
//         * 1:清皮成功
//         * 0xff:超时
//         */
//        int result4 = LScaleHelper.getInstance(context).clearTare();
//
//        /**
//         * 模式切换(WeightMode.ANIMAL,WeightMode.BLANCE)
//         * -2:当前版本不支持此功能
//         * 0:设备未连接
//         * 1:切换成功
//         * 0xff:超时
//         */
//        int result5 = LScaleHelper.getInstance(context).setWeightMode(WeightMode.BLANCE);
////
////        /**
////         * 查询当前模式
////         * -2:当前版本不支持此功能
////         * 0:设备未连接
////         * 1:一般模式
////         * 2:动物模式
////         * 0xff:超时
////         */
////        int result6 = LScaleHelper.getInstance(MainApp.context).queryWeightMode();
//
//
//        /**
//         * 获取电子秤电量信息
//         * isCharge:是否在重点
//         * voltage:当前电压值---4.62为满电量
//         * voltage_percent:电量百分比
//         */
//        LScaleHelper.getInstance(context).getBatteryStatus(new BatteryStatusCallback() {
//            @Override
//            public void callback(boolean isCharge, double voltage, String voltage_percent) {
//
//            }
//        });
//
//
//         }
//
//    /**
//     * 获取连接设备:connectDevice是否为null
//     */
//    @JavascriptInterface
//    public void isNull(){
//        BluetoothDevice connectDevice = LScaleHelper.getInstance(context).getConnectDevice();
//        System.out.println(connectDevice.getName());
//    }
//
//    @JavascriptInterface
//    public void close(){
//        LScaleHelper.getInstance(context).close();
//        connectedDevice = null;
//        address = "";
//    }
//
//
//
//    @JavascriptInterface
//    public int tare(){
//
//        /**
//         * 去皮
//         * -1:当前版本没有返回值
//         * 0:设备未连接
//         * 1:去皮成功
//         * 2:零点错误报警
//         * 3:超过去皮范围
//         * 4:有数字皮,不可去称重皮
//         * 5:重量不稳定
//         * 0xff:超时
//         */
//        int result2 = LScaleHelper.getInstance(context).tare();
//        return  result2;
//    }
//    @JavascriptInterface
//    public int peel(String peelNum){
//    /**
//     * 设置电子皮(框皮)
//     * 0:设备未连接
//     * 1:成功
//     * 2:有称重皮存在,添加数字皮失败
//     * 3:重量不稳定
//     * 4:零点错误
//     * 5:去皮超范围
//     * 0xff:超时
//     * 0xee:旧版本没有成功状态返回
//     */
//     int result3 = LScaleHelper.getInstance(context).setPeel(peelNum);
//     return  result3;
//    }
//
//    public static void setConnBluetooth(BluetoothDevice connBluetoot) {
//        connectedDevice = connBluetoot;
//    }
//
//    public static BluetoothDevice getConnBluetooth() {
//        return connectedDevice;
//    }
//
//    public boolean connected (){
//        boolean connected = LScaleHelper.getInstance(context).isConnected();
//        return  connected;
//    }

}




























