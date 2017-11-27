package com.bill.usbprinterdemo;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Vector;

/**
 * author: Bill
 * created on: 17/11/26 下午2:40
 * description: 打印机服务
 */
public class PrinterService extends IntentService {

    private static final String ACTION_USB_PERMISSION = "com.usb.printer.USB_PERMISSION";

    private Context mContext;
    private UsbDevice mUsbDevice;
    private PendingIntent mPermissionIntent;
    private UsbManager mUsbManager;
    private UsbDeviceConnection mUsbDeviceConnection;

    public PrinterService() {
        super("PrinterService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        init(getApplicationContext());
    }

    private void init(Context context) {
        mContext = context;
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mContext.registerReceiver(mUsbDeviceReceiver, filter);
        // 列出所有的USB设备，并且都请求获取USB权限
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();

        for (UsbDevice device : deviceList.values()) {
            if (device.getInterface(0).getInterfaceClass() == 7)
                mUsbManager.requestPermission(device, mPermissionIntent);
        }
    }

    private final BroadcastReceiver mUsbDeviceReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        mUsbDevice = usbDevice;
                        print();
                    } else {
                        Toast.makeText(context, "Permission denied for device " + usbDevice, Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                if (mUsbDevice != null) {
                    Toast.makeText(context, "Device closed", Toast.LENGTH_SHORT).show();
                    destroy();
                }
            }
        }
    };

    public void print() {
        if (mUsbDevice != null) {
            UsbInterface usbInterface = mUsbDevice.getInterface(0);
            for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
                final UsbEndpoint usbEndpoint = usbInterface.getEndpoint(i);
                if (usbEndpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                    if (usbEndpoint.getDirection() == UsbConstants.USB_DIR_OUT) {
                        mUsbDeviceConnection = mUsbManager.openDevice(mUsbDevice);
                        if (mUsbDeviceConnection != null) {
                            Toast.makeText(mContext, "Device connected", Toast.LENGTH_SHORT).show();
                            mUsbDeviceConnection.claimInterface(usbInterface, true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    //小票打印机是ESC指令
                                    EscCommand esc = new EscCommand();
                                    esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
                                    esc.addSelectPrintModes(EscCommand.FONT.FONTB, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
                                    esc.addText("Sample佳博\n"); // 打印文字
                                    esc.addPrintAndLineFeed();
                                    //esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);

                                    //标签打印机是TSC指令
//                                    LabelCommand tsc = new LabelCommand();
//                                    tsc.addSize(60, 60); // 设置标签尺寸，按照实际尺寸设置
//                                    tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
//                                    tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
//                                    tsc.addReference(0, 0);// 设置原点坐标
//                                    tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
//                                    tsc.addCls();// 清除打印缓冲区
//                                    // 绘制简体中文
//                                    tsc.addText(250, 80, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "abc佳博");
//                                    // 绘制图片
//                                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
//                                    tsc.addBitmap(20, 50, LabelCommand.BITMAP_MODE.OVERWRITE, b.getWidth(), b);
//
//                                    tsc.addQRCode(250, 80, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, " www.gprinter.com.cn");
//                                    // 绘制一维条码
//                                    tsc.add1DBarcode(250, 80, LabelCommand.BARCODETYPE.CODE128, 100, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, "Gprinter");
//                                    tsc.addPrint(1, 1); // 打印标签
//                                    tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
//                                    tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
//                                    Vector<Byte> datas = tsc.getCommand(); // 发送数据

                                    Vector<Byte> datas = esc.getCommand(); // 发送数据
                                    byte[] bytes = GpUtils.ByteTo_byte(datas);
                                    int b = mUsbDeviceConnection.bulkTransfer(usbEndpoint, bytes, bytes.length, 3000);
                                    Log.i("Return Status", "b-->" + b);
                                }
                            }).start();
                        }
                        break;
                    }
                }
            }
        } else {
            Toast.makeText(mContext, "No available USB print device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mUsbDeviceReceiver);
    }

    private void destroy() {
        mContext.unregisterReceiver(mUsbDeviceReceiver);
        if (mUsbDeviceConnection != null) {
            mUsbDeviceConnection.close();
            mUsbDeviceConnection = null;
        }
        mContext = null;
        mUsbManager = null;
    }
}
