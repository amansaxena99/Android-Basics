package com.example.bluetoothfinder;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button searchButton;
    TextView statusTextView;
    ListView listView;
    ArrayList<String> devicesFound = new ArrayList<>();
    ArrayList<String> devicesAddress = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    BluetoothAdapter bluetoothAdapter;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Result", action);

            if (bluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                statusTextView.setText("Finished");
                searchButton.setEnabled(true);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                String rssi = String.valueOf(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,  Short.MIN_VALUE));
                if (!devicesAddress.contains(address)){
                    devicesAddress.add(address);
                    String deviceString = "";
                    if (name == null || name.equals("")){
                        deviceString = address + " - RSSI " + rssi + "dBm";
                    } else {
                        deviceString = name + " - RSSI " + rssi + "dBm";
                    }
                    devicesFound.add(deviceString);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    public void search(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            statusTextView.setText("Searching...");
            searchButton.setEnabled(false);
            devicesFound.clear();
            devicesAddress.clear();
            bluetoothAdapter.startDiscovery();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.searchButton);
        statusTextView = findViewById(R.id.statusTextView);
        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, devicesFound);
        listView.setAdapter(arrayAdapter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);
    }
}
