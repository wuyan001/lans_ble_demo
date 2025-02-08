package com.ble.demo

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.hardware.usb.UsbDevice
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.ble.demo.databinding.BleConnBinding2
import com.ble.demo.utils.DateUtil
import com.ble.demo.utils.T
import com.ble.demo.view.PopBlueSearchPop
import com.ble.demo.viewmodel.BleModel
import com.ble.mylibrary.interfaces.PeelMode
import com.ble.mylibrary.interfaces.WeightUpdateCallback
import com.ble.mylibrary.outdevice.LScaleHelper
import com.lsg.uvccamera.UVCCameraProxy
import com.lsg.uvccamera.bean.PicturePath
import com.lsg.uvccamera.callback.ConnectCallback
import com.lsg.uvccamera.callback.PreviewCallback
import kotlinx.android.synthetic.main.fragment_bd2.*
import java.math.BigDecimal

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: BleConnBinding2
    private var model: BleModel? = null
    private var list_system_order = listOf("线性补偿参数查询","零点内码查询","允许模式切换","禁止模式切换","允许单位切换","禁止单位切换")
    private var permission = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding =    DataBindingUtil.setContentView(this, R.layout.fragment_bd2)
        model = ViewModelProvider(this).get(BleModel::class.java)
        binding.model = model
        model!!.rssi = -1
        model!!.voltage = -1.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission,1)
        }
        initUVCCamera()
        initLisnter()
        getData()
    }
    lateinit var mUVCCamera: UVCCameraProxy
    private fun initUVCCamera() {
        mUVCCamera = UVCCameraProxy(this)
        // 已有默认配置，不需要可以不设置
        mUVCCamera.getConfig()
            .isDebug(true)
            .setPicturePath(PicturePath.APPCACHE)
            .setDirName("uvccamera")
            .setProductId(0)
            .setVendorId(0)
        mUVCCamera.setPreviewTexture(textureView)

        mUVCCamera.setConnectCallback(object : ConnectCallback {
            override  fun onAttached(usbDevice: UsbDevice?) {
                mUVCCamera.requestPermission(usbDevice)
            }

            override  fun onGranted(usbDevice: UsbDevice?, granted: Boolean) {
                if (granted) {
                    mUVCCamera.connectDevice(usbDevice)
                }
            }

            override   fun onConnected(usbDevice: UsbDevice?) {
                mUVCCamera.openCamera()
            }

            override   fun onCameraOpened() {
//                showAllPreviewSizes()
                mUVCCamera.setPreviewSize(640, 480)
                mUVCCamera.startPreview()
            }

            override  fun onDetached(usbDevice: UsbDevice?) {
                mUVCCamera.closeCamera()
            }
        })
        mUVCCamera.setPhotographCallback {
            mUVCCamera.takePicture()

        }
        mUVCCamera.setPreviewCallback(object : PreviewCallback {
            override  fun onPreviewFrame(yuv: ByteArray?) {}
        })
        mUVCCamera.setPictureTakenCallback { path ->
//            path1 = path
//            mImageView1.setImageURI(null)
//            mImageView1.setImageURI(Uri.parse(path))
        }
    }
    private fun initLisnter() {
        binding.btSystem.setOnClickListener {
            showSysDilog()
        }

    }

    @SuppressLint("CheckResult")
    private fun showSysDilog() {
        MaterialDialog(this).show {
            title(text = "指令")
            listItems(items = list_system_order) { _, index, text ->
                T.showShortCenter("当前指令未开放")
                when(index){
//                    0 -> LSSystemHelper.getInstance().queryArgLinear()
//                    1 -> LSSystemHelper.getInstance().queryCoreZero()
//                    2 -> LSSystemHelper.getInstance().setModeChange(true)
//                    3 -> LSSystemHelper.getInstance().setModeChange(false)
//                    4 -> LSSystemHelper.getInstance().setUnitChange(true)
//                    5 -> LSSystemHelper.getInstance().setUnitChange(false)
                }
            }
        }

    }






    override fun onResume() {
        super.onResume()
     // LScaleHelper.getInstance(this).retryConnect()
    }
    private fun resetUi() {
        binding.tvName.text = "名称: "
        binding.tvAddress.text = "地址: "
        binding.tvRssi.text = "rssi: "
        binding.tvBatter.text = "电压: "
        binding.tvState.text = "状态: "
        binding.tvStable.text = "稳定信号: "
        binding.tvZeroLight.text = "零点指示灯: "
        binding.tvPeelLight.text = "去皮指示灯: "
        binding.tvPeelData.text = "去皮量: "
        binding.tvCharge.text = "充电: "
        binding.tvData.text = "数据: "
    }
    fun getData(){
        LScaleHelper.getInstance(this).setWUCallback(object : WeightUpdateCallback {
            @SuppressLint("SetTextI18n")
            override fun onWeightUpdate(
                weight: BigDecimal?,
                isStable: Boolean,
                isTared: Boolean,
                isZero: Boolean,
                peelMode: PeelMode?,
                dataStatus: Int,
                error: Boolean
            ) {

                if (error) {
                    when (dataStatus) {
                        3 -> {
                            binding.tvState.text = "状态: 断开"
                        }
                        31 -> {
                            binding.tvState.text = "状态: 被动断开"
                        }
                        32 ->{
                            binding.tvState.text = "状态: 未测到可用usb"
                        }
                        33 ->{
                            binding.tvState.text = "状态: usb数据线有问题"
                        }
                        4 -> { //过载
                            binding.tvState.text = "状态: 过载"
                        }
                        5 -> {
                            binding.tvState.text = "状态: 负重"
                        }
                    }
                } else {
                    binding.tvState.text = "状态: 工作"
                    binding.tvData.text = "数据: " + weight?.toPlainString()
                    binding.tvStable.text = "稳定信号:$isStable"
                    binding.tvZeroLight.text = "零点指示灯:$isZero"
                    binding.tvPeelLight.text = "去皮指示灯:$isTared"
                    if (peelMode == null) {
                        binding.tvPeelData.text = "去皮量: "
                    } else if (peelMode == PeelMode.WeightPell) {
                        binding.tvPeelData.text = "去皮量(称重皮): " + peelMode.data
                    } else if (peelMode == PeelMode.DigitPell) {
                        binding.tvPeelData.text = "去皮量(电子皮): " + peelMode.data
                    }
                    if (dataStatus == 6) {
                        T.showShortCenter("外卖订单")
                    }
                }
            }

            override fun onrssi(rssi: Int) {
                binding.tvRssi.text = "rssi: $rssi dbm"
                model!!.rssi = rssi
            }
        })
        LScaleHelper.getInstance(this)
            .getBatteryStatus { isCharge, voltage, voltage_percent ->
                binding.tvCharge.text = "充电: $isCharge"
                binding.tvBatter.text = "电压: $voltage( $voltage_percent% )"
                model!!.voltage = voltage
            }
    }
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.menu_search, menu)
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)
        return true
    }



    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            showSearchPop()

        }else if (item.itemId == R.id.action_send){
            T.showShortCenter("当前指令未开放")
//            MaterialDialog(this).show {
//                message(text = "请输入自定义指令")
//                input { _, charSequence ->
//                    var order = charSequence.toString().trim()
//                    BleManager.send(order.toByteArray(),null)
//                }
//            }
        }
        return true
    }

    private fun showSearchPop(){
        PopBlueSearchPop.Builder(this).canConnect(true).setModel(model!!)
            .setOnSelectCallback { result, device ->
                updateConnectResult(result, device)
            }.build().show_center()
    }

    @SuppressLint("SetTextI18n")
    private fun updateConnectResult(result: Int, device: BluetoothDevice?) {
        when (result) {
            1 -> {
                binding.tvName.text = "名称: " + device?.name
                binding.tvAddress.text = "地址: " + device?.address
                binding.tvFlow.append(DateUtil.getDate_hms() + " 连接成功\r\n")
                T.showShortCenter("连接成功")
            }
            2 -> {
                binding.tvFlow.append(
                    DateUtil.getDate_hms() + " 连接失败\r\n")
                resetUi()
                T.showShortCenter("连接失败")

            }
            21 ->{
                binding.tvFlow.append(DateUtil.getDate_hms() + " 有usb设备连接\r\n")
                resetUi()
                T.showShortCenter("有usb设备连接,拒绝连接")
            }
            3 -> {
                binding.tvFlow.append(
                    DateUtil.getDate_hms() + " 连接主动断开\r\n")
                resetUi()
                T.showShortCenter("连接主动断开")
            }
            31 ->{
                binding.tvFlow.append(
                    DateUtil.getDate_hms() + " 连接意外断开\r\n")
                resetUi()
                T.showShortCenter("连接意外断开")
            }

        }
    }

}