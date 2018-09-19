package com.example.asohail4.bluetoothserver;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asohail4.bluetoothserver.Adapters.MyAdapter;
import com.example.asohail4.bluetoothserver.Helper.RecyclerItemClickListener;
import com.example.asohail4.bluetoothserver.Models.MyDevice;
import com.example.asohail4.bluetoothserver.Screens.MessageList;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 12;
    private TextView out;
    private Button btn;
    private static BluetoothAdapter adapter;
    private static BluetoothSocket btSocket = null;
    private static OutputStream outStream = null;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private List<MyDevice> devices = new ArrayList<>();
    // Well known SPP UUID
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn =  findViewById(R.id.btn);

        if (Connection.blueTooth()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else{
            // Write you code here if permission already given.
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBluetoothData();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(devices);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        MyDevice device = devices.get(position);
                        Connect(device.getAddress());
                     }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        setBluetoothData();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
//        out.setText("");
        setBluetoothData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
            }
        }

    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
               // out.append("\nFound device: " + device.getName() + " Add: " + device.getAddress());
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                MyDevice newDevice = new MyDevice(deviceName,deviceHardwareAddress);
                devices.add(newDevice);
                mAdapter.notifyDataSetChanged();

             //   out.append("\n...In onResume...\n...Attempting client connect...");
            }
        }
    };

    private void setBluetoothData() {
        devices.clear();
        mAdapter.notifyDataSetChanged();
      //  out.setText("");
        // Getting the Bluetooth adapter
        adapter = BluetoothAdapter.getDefaultAdapter();

        // Check for Bluetooth support in the first place
        // Emulator doesn't support Bluetooth and will return null

        if (adapter == null) {
            Toast.makeText(this, "Bluetooth NOT supported. Aborting.",
                    Toast.LENGTH_LONG).show();
        } else{
      //      out.append("\nAdapter: " + adapter.toString() + "\n\nName: " + adapter.getName() + "\nAddress: " + adapter.getAddress());
            // Starting the device discovery
           // out.append("\n\nStarting discovery...");
            adapter.startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);
           // out.append("\nDone with discovery...\n");

            // Listing paired devices
            //out.append("\nDevices Pared:");
            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            String address = "00:28:F8:FC:8A:0E";
            for (BluetoothDevice device : devices) {
           //     out.append("\nFound device: " + device.getName() + " Add: " + device.getAddress());
            }
           // Connect(address);

        }

    }

    public static void AlertBox( String title, String message ){
        Log.d("Error: " + title, message);
    }


    private void Connect(String address){
        // Set up a pointer to the remote node using it's address.
        BluetoothDevice remoteDevice = adapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            btSocket = remoteDevice.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            AlertBox("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        adapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        try {
            btSocket.connect();
       //     out.append("\n...Connection established and data link opened...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                AlertBox("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
      //  out.append("\n...Sending message to server...");

       Intent intent = new Intent(this, MessageList.class);
        startActivity(intent);

    }


    public static void sendMessage(String message){
        //   out.append("\n\n...The message that we will send to the server is: "+message);

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            AlertBox("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }

        byte[] msgBuffer = message.getBytes();
        try {
            outStream.write(msgBuffer);
            outStream.flush();
         //   outStream.close();
            //        Toast.makeText(this, message + " Send to " + address, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            AlertBox("Fatal Error", msg);
        }

    }
}
