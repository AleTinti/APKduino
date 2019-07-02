package it.aletinti.apkduino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class activity1 extends AppCompatActivity {

    public static String toSend;

    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    ListView btDeviceList;
    String btDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);

        Intent btEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(btEnable, 1);

        btDeviceList = findViewById(R.id.btDeviceList);

        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        List<String> ArrayList = new ArrayList();
        for(BluetoothDevice btAdapter : pairedDevices)
            ArrayList.add(btAdapter.getName() + " (" + btAdapter.getAddress() + ")");
        final ArrayAdapter btDeviceArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList);

        btDeviceList.setAdapter(btDeviceArrayAdapter);

        btDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btDevice = (btDeviceArrayAdapter.getItem(position)).toString();
                toSend = ((btDevice.split(" ")[1]).replace("(", "")).replace(")", "").trim();
                activity2();
            }
        });
    }

    public void activity2(){
        Intent NextActivity = new Intent(this, activity2.class);
        startActivity(NextActivity);
    }
}
