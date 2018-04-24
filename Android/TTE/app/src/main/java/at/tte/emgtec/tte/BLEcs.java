package at.tte.emgtec.tte;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Florian Leitner on 28.10.2014.
 */
public class BLEcs {
    private static String ArduSavedAddress = "DF:B3:1D:D4:BB:66";
    private static Boolean connected2Ardu = false;
    private static RBLService BLEservice;
    private static Map<UUID, BluetoothGattCharacteristic> map = new HashMap<UUID, BluetoothGattCharacteristic>();
    private static String connectiontype = "";
    private static BluetoothAdapter mBluetoothAdapter;
    private static String TAG = "BLEcs";
    public static Activity act;
    public static Context contxt;

    public static void BTConnect() {
        BluetoothManager mBluetoothManager = (BluetoothManager) act.getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        BLEservice = new RBLService();
        contxt.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        connectiontype = "";
        //textconnect.setText("Connecting");
        //progconnect.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                if (!mBluetoothAdapter.isEnabled()) {
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //textconnect.setText("Turning on Bluetooth");
                            Log.d(TAG,"Turning on Bluetooth");
                        }
                    });
                    mBluetoothAdapter.enable();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    act.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //textconnect.setText("Connecting");
                            Log.d(TAG,"Connecting");
                        }
                    });
                }

                if (ArduSavedAddress.equals(""))// No saved
                // devices.
                {
                    connectiontype = "NoSaved";
                } else {
                    //Log.d(TAG,"warum zum verdammten teufel!!!!!!!");
                    // BLEservice.connect(ArduSavedAddress);

                    // connectiontype =
                    // "connectionFailed";
                    // else
                    // connectiontype = "connected";
                    Intent gattServiceIntent = new Intent(contxt, RBLService.class);
                    contxt.bindService(gattServiceIntent, mServiceConnection, contxt.BIND_AUTO_CREATE);

                    new Thread() {
                        @Override
                        public void run() {

                            // while (!connected2Ardu)
                            for (int i = 0; i < 600; i++) {
                                try {
                                    sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (i == 599) {
                                    act.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(contxt, "timeout", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    try {
                                        this.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (connected2Ardu)
                                    i = 9999;
                            }
                            if (connected2Ardu) {
                                try {
                                    sleep(3000);
                                } catch (InterruptedException e) {

                                    e.printStackTrace();
                                }
                                BLEsendString("con BLE");
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                BLEsendString("dg setoutd 0");
                                // TODO reactivate BLE
                                connectiontype = "connected";
                                act.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(contxt,"Connected",Toast.LENGTH_SHORT).show();
                                        //textconnect.setText("Connected");
                                        //textconfailed.setVisibility(View.GONE);
                                        //progconnect.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                connectiontype = "conFailed";
                                Toast.makeText(contxt,"Failed",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }.start();
                    // while (connectiontype.equals(""))
                    // {}// warte auf Verbindung

                }
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (connectiontype.equals("connected")) {
                            //textconnect.setText("Connected");
                            //textconfailed.setVisibility(View.GONE);
                            //progconnect.setVisibility(View.GONE);
                        }
                        //threadBTfind.start();
                        //listpaired1.setAdapter(BTArrayAdapter);
                        //conrefresh.setVisibility(View.VISIBLE);
                        //progconnect.setVisibility(View.GONE);
                    }


                });
            }

        }.start();
    }

    public static void BTDisconnect()
    {
        //contxt.unbindService(mServiceConnection);
    }
    private final static ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service)
        {
            // Toast.makeText(contxt, "tt", Toast.LENGTH_SHORT).show();
            BLEservice = ((RBLService.LocalBinder) service).getService();
            if (!BLEservice.initialize())
            {
                Log.e("", "Unable to initialize Bluetooth");
            }
            // Automatically connects to the device upon successful start-up
            // initialization.
            Log.d(TAG,"bind...");

            if (BLEservice.connect(ArduSavedAddress)) {
                connected2Ardu = true;
                Log.d(TAG,"Verbunden...");
            }
            else {
                connected2Ardu = false;
                Log.d(TAG,"Nicht Verbunden...!");
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // Toast.makeText(contxt, "tt", Toast.LENGTH_SHORT).show();
            BLEservice = null;
        }
    };
    private final static BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            // Toast.makeText(contxt, "text", Toast.LENGTH_SHORT).show();
            //Log.d(TAG,"Action: " + action + " " + RBLService.ACTION_GATT_DISCONNECTED);
            if (RBLService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.d(TAG,"Disconnected");
            } else if (RBLService.ACTION_GATT_SERVICES_DISCOVERED
                    .equals(action)) {
                Log.d(TAG,"Connected");
                getGattService(BLEservice.getSupportedGattService());
            } else if (RBLService.ACTION_DATA_AVAILABLE.equals(action)) {
                BLEreceiveString(intent.getByteArrayExtra(RBLService.EXTRA_DATA));
            }
        }
    };
    public static void BLEsendString(String string)
    {
        String[] com2send = string.split("\n");
        for (String command : com2send)
        {
            if (command != "")
            {
                //Log.d(TAG,command);
                BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);

                command += '\n';

                byte[] tmp = command.getBytes();
                if(characteristic == null)
                    Log.d(TAG,"null");
                else {
                    //Log.d(TAG, "uuid"+RBLService.UUID_BLE_SHIELD_TX);
                    characteristic.setValue(tmp);
                    BLEservice.writeCharacteristic(characteristic);
                }
/*
                if (BLEchatAdapter != null)
                {
                    chatverlauf.add(command);
                }
*/
            }
        }
    }
    private static void BLEreceiveString(byte[] byteArray) {

        if (byteArray != null) {
            String data = new String(byteArray);
            //sentText += data;
            //sentText += '\n';
            // Toast.makeText(contxt, "test", Toast.LENGTH_SHORT).show()
            Variables.received = data;
        }
    }
    private static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
    private static void getGattService(BluetoothGattService gattService) {

        if (gattService == null)
            return;
        // Toast.makeText(contxt, "toast", Toast.LENGTH_SHORT).show();

        BluetoothGattCharacteristic characteristic = gattService
                .getCharacteristic(RBLService.UUID_BLE_SHIELD_TX);
        map.put(characteristic.getUuid(), characteristic);

        BluetoothGattCharacteristic characteristicRx = gattService
                .getCharacteristic(RBLService.UUID_BLE_SHIELD_RX);
        BLEservice.setCharacteristicNotification(characteristicRx,
                true);
        BLEservice.readCharacteristic(characteristicRx);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(RBLService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(RBLService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(RBLService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(RBLService.ACTION_DATA_AVAILABLE);

        return intentFilter;
    }

}
